package frc.robot.subsystems;

import edu.wpi.first.math.MatBuilder;
import edu.wpi.first.math.Nat;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.SwerveMode;
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
  private ADXRS450_Gyro gyro = new ADXRS450_Gyro();
  private SwerveDrivePoseEstimator swerveDrivePoseEstimator = null;
  private ChassisSpeeds desiredChassisSpeeds = null;
  private SwerveMode swerveMode = SwerveMode.Nothing;
  private ShuffleboardTab tabDrive = Shuffleboard.getTab("DriveData"); 
  private Limelight limelight = Limelight.getInstance(); 
  
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
    swerveDrivePoseEstimator = new SwerveDrivePoseEstimator(swerveDriveKinematics, 
    getGyroAngle(), 
    getModulesPosition(), 
    new Pose2d(), 
    new MatBuilder<>(Nat.N3(), Nat.N1()).fill(0, 0, 0), 
    new MatBuilder<>(Nat.N3(), Nat.N1()).fill(0, 0, 0));
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
    switch (swerveMode){
      case Nothing:
        if (RobotState.isEnabled()){
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
        if (desiredChassisSpeeds != null) {
          SwerveModuleState[] modules_states = new SwerveModuleState[4]; 
          if (desiredChassisSpeeds.vxMetersPerSecond == 0 && desiredChassisSpeeds.vyMetersPerSecond == 0 && desiredChassisSpeeds.omegaRadiansPerSecond == 0){
            modules_states[0] = new SwerveModuleState(0, Rotation2d.fromDegrees(45));
            modules_states[1] = new SwerveModuleState(0, Rotation2d.fromDegrees(45));
            modules_states[2] = new SwerveModuleState(0, Rotation2d.fromDegrees(45));
            modules_states[3] = new SwerveModuleState(0, Rotation2d.fromDegrees(45));
          }
          else {
            modules_states = swerveDriveKinematics.toSwerveModuleStates(desiredChassisSpeeds);
            SwerveDriveKinematics.desaturateWheelSpeeds(modules_states, Constants.maxDriveVel);
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
  
  public Pose2d getCurrentPose (){
    return swerveDrivePoseEstimator.getEstimatedPosition();
  }
  
  public void setCurrentPose (Pose2d pose){
    swerveDrivePoseEstimator.resetPosition(getGyroAngle(), getModulesPosition(), pose);
  }
  
  public void setDesiredChassisSpeeds (ChassisSpeeds chassisSpeeds){
    desiredChassisSpeeds = chassisSpeeds;
    SwerveModuleState[] modules_states = new SwerveModuleState[4]; 
    if (desiredChassisSpeeds.vxMetersPerSecond == 0 && desiredChassisSpeeds.vyMetersPerSecond == 0 && desiredChassisSpeeds.omegaRadiansPerSecond == 0){
      modules_states[0] = new SwerveModuleState(0, Rotation2d.fromDegrees(45));
      modules_states[1] = new SwerveModuleState(0, Rotation2d.fromDegrees(45));
      modules_states[2] = new SwerveModuleState(0, Rotation2d.fromDegrees(45));
      modules_states[3] = new SwerveModuleState(0, Rotation2d.fromDegrees(45));
    }
    else {
      modules_states = swerveDriveKinematics.toSwerveModuleStates(desiredChassisSpeeds);
      SwerveDriveKinematics.desaturateWheelSpeeds(modules_states, Constants.maxDriveVel);
    }
    setModulesStates(modules_states);
  }

  public void resetChassisPosition (Pose2d initialPose){
    resetGyroAngle();
    for (SwerveModule module : modules){
      module.setPositionSpeedMotor(0);
    }
    swerveDrivePoseEstimator = new SwerveDrivePoseEstimator(swerveDriveKinematics, getGyroAngle(), getModulesPosition(), initialPose);
  }
  
  public void resetGyroAngle (){
    gyro.reset();
  }
  
  public Rotation2d getGyroAngle (){
    return gyro.getRotation2d();
  }
  
  public void updateOdometry (){
    if (limelight.sawTag()){
      Alliance alliance = DriverStation.getAlliance();
      switch (alliance){
        case Blue:
          swerveDrivePoseEstimator.addVisionMeasurement(limelight.getBotPoseBlueAlliance(), Timer.getFPGATimestamp() - (limelight.getLatencyPipeline()/1000.0) - (limelight.getLatencyCapture()/1000.0));
        break;
        case Red:
          swerveDrivePoseEstimator.addVisionMeasurement(limelight.getBotPoseRedAlliance(), Timer.getFPGATimestamp() - (limelight.getLatencyPipeline()/1000.0) - (limelight.getLatencyCapture()/1000.0));
        break;
        case Invalid:
        break;
      }
    } else {
      swerveDrivePoseEstimator.update(getGyroAngle(), getModulesPosition());
    }
  }

  public void outputTelemetry (){
    tabDrive.addDouble("X Pose Odometry", ()->{return getCurrentPose().getX();}).withPosition(8, 0);
    tabDrive.addDouble("Y Pose Odometry", ()->{return getCurrentPose().getY();}).withPosition(9, 0);
    tabDrive.addDouble("GyroAngle", ()->{return getGyroAngle().getDegrees();}).withPosition(8, 1); 
    tabDrive.addDouble("TAG ID", ()->{return limelight.getTagID();}).withPosition(9, 1);
  }
}
