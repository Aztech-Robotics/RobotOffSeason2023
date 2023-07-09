package frc.robot.subsystems;

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
import frc.robot.limelight.Limelight;
import frc.robot.swerve.SwerveModule;

public class Drive extends SubsystemBase {
  private static Drive drive;
  public static SwerveDriveKinematics swerveDriveKinematics = new SwerveDriveKinematics(
    //FrontLeft
    new Translation2d(Constants.trackWidth/2, Constants.wheelBase/2),
    //FrontRight
    new Translation2d(Constants.trackWidth/2, -Constants.wheelBase/2),
    //RearLeft
    new Translation2d(-Constants.trackWidth/2, Constants.wheelBase/2),
    //RearRight 
    new Translation2d(-Constants.trackWidth/2, -Constants.wheelBase/2)
  );
  private SwerveDrivePoseEstimator swerveDrivePoseEstimator = null;
  private ChassisSpeeds desiredChassisSpeeds = null;
  private SwerveMode swerveMode = SwerveMode.Nothing;
  private SwerveModule[] modules = new SwerveModule[4];
  private SwerveModuleState[] modules_states;
  private ADXRS450_Gyro gyro = new ADXRS450_Gyro();
  private ShuffleboardTab tabDrive = Shuffleboard.getTab("DriveData"); 
  private Limelight limelight = new Limelight(); 
  
  private Drive() {
    modules[0] = new SwerveModule(
      Constants.id_steer_fLeft, 
      Constants.id_canCoder_fLeft, 
      Constants.id_drive1_fLeft, 
      Constants.id_drive2_fLeft, 
      Constants.offset_fLeft
    );
    modules[1] = new SwerveModule(
      Constants.id_steer_fRight, 
      Constants.id_canCoder_fRight, 
      Constants.id_drive1_fRight, 
      Constants.id_drive2_fRight, 
      Constants.offset_fRight
    );
    modules[2] = new SwerveModule(
      Constants.id_steer_bLeft, 
      Constants.id_canCoder_bLeft, 
      Constants.id_drive1_bLeft, 
      Constants.id_drive2_bLeft, 
      Constants.offset_bLeft
    );
    modules[3] = new SwerveModule(
      Constants.id_steer_bRight, 
      Constants.id_canCoder_bRight, 
      Constants.id_drive1_bRight, 
      Constants.id_drive2_bRight, 
      Constants.offset_bRight
    ); 
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

      case OpenLoopWithVelocity:
        if (desiredChassisSpeeds != null) {
          modules_states = swerveDriveKinematics.toSwerveModuleStates(desiredChassisSpeeds);
          SwerveDriveKinematics.desaturateWheelSpeeds(modules_states, Constants.maxDriveVel);
          setModulesStatesWithVelocity(modules_states);
        }
        desiredChassisSpeeds = null;
      break;

      case OpenLoopWithVoltage:
      SwerveModuleState[] moduleStatesArray;
        if (desiredChassisSpeeds != null) {
          moduleStatesArray = swerveDriveKinematics.toSwerveModuleStates(desiredChassisSpeeds);
          SwerveDriveKinematics.desaturateWheelSpeeds(moduleStatesArray, Constants.maxDriveVel);
          setModulesStatesWithVoltage(moduleStatesArray);
        }
        desiredChassisSpeeds = null;
      break;

      case Trajectory:
        updateOdometry();
      break;

      case AutoBalance:
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
  
  public void setModulesStatesWithVelocity (SwerveModuleState[] swerveModulesModuleStates){
    for (int i=0; i < modules.length; i++){
      modules[i].setModuleStateWithVelocity(SwerveModuleState.optimize(swerveModulesModuleStates[i], modules[i].getCanCoderAngle()));
    }
  }

  public void setModulesStatesWithVoltage (SwerveModuleState[] swerveModulesModuleStates){
    for (int i=0; i < modules.length; i++){
      modules[i].setModuleStateWithVoltage(SwerveModuleState.optimize(swerveModulesModuleStates[i], modules[i].getCanCoderAngle()));
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
    tabDrive.addDouble("GyroAngle", ()->{return getGyroAngle().getDegrees();}); 
  }
}
