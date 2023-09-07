package frc.robot.subsystems;

import edu.wpi.first.math.MatBuilder;
import edu.wpi.first.math.Nat;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.Telemetry;
import frc.robot.Constants.AutoBalanceStages;
import frc.robot.Constants.SwerveMode;
import frc.robot.Constants.SwerveSubMode;
import frc.robot.swerve.DriveMotionPlanner;
import frc.robot.swerve.SwerveModule;

public class Drive extends SubsystemBase  {
  private static Drive drive;
  public static SwerveDriveKinematics swerveDriveKinematics = new SwerveDriveKinematics(
    new Translation2d(Constants.trackWidth/2, Constants.wheelBase/2),
    new Translation2d(Constants.trackWidth/2, -Constants.wheelBase/2),
    new Translation2d(-Constants.trackWidth/2, Constants.wheelBase/2),
    new Translation2d(-Constants.trackWidth/2, -Constants.wheelBase/2)
  );
  private SwerveModule[] modules = new SwerveModule[4];
  private ADXRS450_Gyro yaw_gyro = new ADXRS450_Gyro();
  private Vision vision = Vision.getInstance();
  private SwerveDrivePoseEstimator swerveDrivePoseEstimator;
  private DriveMotionPlanner motionPlanner;
  private ChassisSpeeds desiredChassisSpeeds;
  private SwerveMode swerveMode = SwerveMode.Nothing;
  private SwerveSubMode swerveSubMode = SwerveSubMode.Nothing;
  private AutoBalanceStages autoB_stage;
  private boolean odometryReseted = false; 
  private boolean orientModulesOnZero = false;
  private Double time_update_mod = Double.NaN;
  
  private Drive() {
    modules[0] = new SwerveModule(
      Constants.id_speed_fLeft,
      Constants.id_steer_fLeft, 
      Constants.id_canCoder_fLeft, 
      Constants.offset_fLeft
    );
    modules[1] = new SwerveModule(
      Constants.id_speed_fRight, 
      Constants.id_steer_fRight, 
      Constants.id_canCoder_fRight, 
      Constants.offset_fRight
    );
    modules[2] = new SwerveModule(
      Constants.id_speed_bLeft, 
      Constants.id_steer_bLeft, 
      Constants.id_canCoder_bLeft, 
      Constants.offset_bLeft
    );
    modules[3] = new SwerveModule(
      Constants.id_speed_bRight, 
      Constants.id_steer_bRight, 
      Constants.id_canCoder_bRight, 
      Constants.offset_bRight
    ); 
    swerveDrivePoseEstimator = new SwerveDrivePoseEstimator(swerveDriveKinematics, getYawAngle(), getModulesPosition(), new Pose2d(), 
    new MatBuilder<>(Nat.N3(), Nat.N1()).fill(0, 0, 0), 
    new MatBuilder<>(Nat.N3(), Nat.N1()).fill(0, 0, 0));
    motionPlanner = new DriveMotionPlanner();
    yaw_gyro.calibrate();
    outputTelemetry();
  }

  public static Drive getInstance (){
    if (drive == null){
      drive = new Drive();
    }
    return drive;
  }

  @Override
  public void periodic() {
    if (time_update_mod.isNaN()){
      time_update_mod = Timer.getFPGATimestamp();
    }
    switch (swerveMode){
      case Nothing:
        if (RobotState.isEnabled()){
          swerveMode = SwerveMode.OpenLoop;
          if (!modules[0].isBrakeMode){
            setBrakeMode();
          }
        } else {
          if (modules[0].isBrakeMode){
            setCoastMode();
          }
        }
      break; 
      case OpenLoop:
        double now = Timer.getFPGATimestamp();
        if (now - time_update_mod >= 1){
          for (SwerveModule swerveModule : modules){
            swerveModule.setAngleCanCoderToPositionMotor();
          }
          time_update_mod = now;
        }
        switch (swerveSubMode){
          case Trajectory:
          desiredChassisSpeeds = motionPlanner.update(getCurrentPose(), now); 
          break;
          case AutoBalance:
          desiredChassisSpeeds = updateAutoBalance(now);
          break;
          case Nothing: 
          break;
        }
        if (desiredChassisSpeeds != null) {
          SwerveModuleState[] modules_states = new SwerveModuleState[4]; 
          if (orientModulesOnZero && desiredChassisSpeeds.vxMetersPerSecond == 0 && desiredChassisSpeeds.vyMetersPerSecond == 0 && desiredChassisSpeeds.omegaRadiansPerSecond == 0){
            modules_states[0] = new SwerveModuleState(0, Rotation2d.fromDegrees(-45));
            modules_states[1] = new SwerveModuleState(0, Rotation2d.fromDegrees(45));
            modules_states[2] = new SwerveModuleState(0, Rotation2d.fromDegrees(45));
            modules_states[3] = new SwerveModuleState(0, Rotation2d.fromDegrees(-45));
          }
          else {
            modules_states = swerveDriveKinematics.toSwerveModuleStates(desiredChassisSpeeds);
          }
          setModulesStates(modules_states);
        }
        desiredChassisSpeeds = null;
        updateOdometry();
      break;
    }
  }

  public void setMode (SwerveMode swerveMode){
    this.swerveMode = swerveMode;
  }

  public void setSubMode (SwerveSubMode swerveSubMode){
    this.swerveSubMode = swerveSubMode;
  }

  public void setBrakeMode (){
    for (SwerveModule swerveModule : modules){
      swerveModule.setBrakeMode();
    }
  }

  public void setCoastMode (){
    for (SwerveModule swerveModule : modules){
      swerveModule.setCoastMode();
    }
  }
  
  public SwerveModuleState[] getModulesStates (){
    SwerveModuleState[] swerveModulesStates = new SwerveModuleState[4];
    for (int i=0; i < modules.length; i++){
      swerveModulesStates[i] = modules[i].getModuleState();
    }
    return swerveModulesStates;
  }
  
  public void setModulesStates (SwerveModuleState[] swerveModuleStates){
    for (int i=0; i < modules.length; i++){
      modules[i].setModuleState(swerveModuleStates[i]);
    }
  }
  
  public SwerveModulePosition[] getModulesPosition(){
    SwerveModulePosition[] modulesPosition = new SwerveModulePosition[4];
    for (int i=0; i < modules.length; i++){
      modulesPosition[i] = modules[i].getModulePosition();
    }
    return modulesPosition;
  }
  
  public void resetYawAngle (){
    yaw_gyro.reset();
  }
  
  public Rotation2d getYawAngle (){
    return Rotation2d.fromDegrees(Math.IEEEremainder(yaw_gyro.getAngle(), 360));
  }

  public void resetPitchAngle (){

  }
  
  public Rotation2d getPitchAngle (){
    return new Rotation2d();
  }
  
  public Pose2d getCurrentPose (){
    return swerveDrivePoseEstimator.getEstimatedPosition();
  }
  
  public void setCurrentPose (Pose2d pose){
    swerveDrivePoseEstimator.resetPosition(getYawAngle(), getModulesPosition(), pose);
  }

  public void resetChassisPosition (Pose2d initialPose){
    resetYawAngle();
    for (SwerveModule module : modules){
      module.setPositionSpeedMotor(0);
    }
    swerveDrivePoseEstimator = new SwerveDrivePoseEstimator(swerveDriveKinematics, getYawAngle(), getModulesPosition(), initialPose);
  }
  
  public void resetOdometry (Pose2d starting_pose){
    odometryReseted = true; 
    resetChassisPosition(starting_pose);
    setCurrentPose(starting_pose);
  }

  public CommandBase resetGyroComm (){
    return runOnce(
      () -> {
        resetYawAngle();
      }
    );
  }

  public void setDesiredChassisSpeeds (ChassisSpeeds chassisSpeeds){
    desiredChassisSpeeds = chassisSpeeds;
  }
  
  public void updateOdometry (){
    if (vision.sawTag()){
      if (Robot.flip_alliance()){
        swerveDrivePoseEstimator.addVisionMeasurement(vision.getBotPoseRedAlliance(), Timer.getFPGATimestamp() - (vision.getLatencyPipeline()/1000.0) - (vision.getLatencyCapture()/1000.0));
      } else {
        swerveDrivePoseEstimator.addVisionMeasurement(vision.getBotPoseBlueAlliance(), Timer.getFPGATimestamp() - (vision.getLatencyPipeline()/1000.0) - (vision.getLatencyCapture()/1000.0));
      }
    } else {
      swerveDrivePoseEstimator.update(getYawAngle(), getModulesPosition());
    }
  }

  public void setTrajectory (Trajectory trajectory, Rotation2d target_rotation){
    motionPlanner.setTrajectory(trajectory, target_rotation, getCurrentPose());
    swerveSubMode = SwerveSubMode.Trajectory;
  }

  public boolean isReadyForAuto (){
    return odometryReseted;
  }

  public boolean isDoneWithTrajectory (){
    return swerveSubMode == SwerveSubMode.Trajectory && motionPlanner.isTrajectoryFinished();
  }

  private double vel_first_stage = 0.0;
  private double angle_1st_stage = 0.0;
  private double vel_scnd_stage = 0.0;
  private Double timeout = Double.NaN;
  private Double starter_time = Double.NaN;
  private PIDController pitch_controller = new PIDController(Constants.kp_autoB, Constants.ki_autoB, Constants.kd_autoB);
  private Debouncer angle_debouncer = new Debouncer(0.5, DebounceType.kBoth);
  private boolean autoBalanceIsDone = false;

  public void initAutoBalance (boolean isBackward){
    autoB_stage = AutoBalanceStages.BreakTheResistance; 
    if (isBackward){
      vel_first_stage = -vel_first_stage;
      vel_scnd_stage = -vel_scnd_stage; 
    }
    pitch_controller.setSetpoint(0.0);
    pitch_controller.setTolerance(0.2);
    pitch_controller.reset(); 
    setSubMode(SwerveSubMode.AutoBalance);
    autoBalanceIsDone = false;
    orientModules();
  }

  public ChassisSpeeds updateAutoBalance (double current_time){
    if (autoB_stage == null){
      System.out.println("Variable Auto Balance Not Initialized");
      return new ChassisSpeeds();
    }
    if (starter_time.isNaN()){
      starter_time = Timer.getFPGATimestamp();
    }
    ChassisSpeeds output = new ChassisSpeeds();
    switch (autoB_stage){
      case BreakTheResistance:
      output = new ChassisSpeeds(vel_first_stage, 0, 0);
      boolean isOverLimitAngle = angle_1st_stage > 0 ? getPitchAngle().getDegrees() > angle_1st_stage : getPitchAngle().getDegrees() < angle_1st_stage; 
      if (timeout.isNaN()){
        timeout = 1.0;
      }
      if (isOverLimitAngle || current_time - starter_time > timeout){
        autoB_stage = AutoBalanceStages.DriveEstimatedDistance;
        timeout = Double.NaN;
        starter_time = Double.NaN;
      }
      break;
      case DriveEstimatedDistance:
      output = new ChassisSpeeds(vel_scnd_stage, 0, 0);
      if (timeout.isNaN()){
        timeout = 1.5;
      }
      if (current_time - starter_time > timeout){
        autoB_stage = AutoBalanceStages.MaintainBalance;
        timeout = Double.NaN;
        starter_time = Double.NaN;
      }
      break;
      case MaintainBalance:
      boolean outOfTolerance = angle_debouncer.calculate(!pitch_controller.atSetpoint());
      if (outOfTolerance){
        output = new ChassisSpeeds(pitch_controller.calculate(getPitchAngle().getDegrees()), 0, 0); 
      } else {
        autoBalanceIsDone = true;
      }
      break;
    }
    return output;
  }

  public boolean autoBIsDone (){
    return autoBalanceIsDone;
  }

  public void orientModules (){
    orientModulesOnZero = true;
  }

  public void outputTelemetry (){
    Telemetry.swerveTab.addDouble("X Pose Odometry", () -> getCurrentPose().getX()).withPosition(8, 0);
    Telemetry.swerveTab.addDouble("Y Pose Odometry", () -> getCurrentPose().getY()).withPosition(9, 0);
    Telemetry.swerveTab.addDouble("Yaw Angle", () -> getYawAngle().getDegrees()).withPosition(8, 1); 
    Telemetry.swerveTab.addDouble("Pitch Angle", () -> getPitchAngle().getDegrees()).withPosition(9, 1);
    Telemetry.swerveTab.addBoolean("Odometry Reseted", () -> isReadyForAuto()).withPosition(0, 3);
    Telemetry.swerveTab.addDouble("X Error", () -> motionPlanner.getTranslationalError(getCurrentPose(), Timer.getFPGATimestamp()).getX()).withPosition(1, 3);
    Telemetry.swerveTab.addDouble("Y Error", () -> motionPlanner.getTranslationalError(getCurrentPose(), Timer.getFPGATimestamp()).getY()).withPosition(2, 3);
    Telemetry.swerveTab.addDouble("Theta Error", () -> motionPlanner.getRotationalError(getYawAngle()).getDegrees()).withPosition(3, 3);
    Telemetry.swerveTab.addBoolean("TrajectoryIsFinished", () -> isDoneWithTrajectory()); 
  }
}
