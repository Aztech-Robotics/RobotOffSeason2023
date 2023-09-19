package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicDutyCycle;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.ReverseLimitTypeValue;
import com.ctre.phoenix6.signals.ReverseLimitValue;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Telemetry;

public class Arm extends SubsystemBase {
  private static Arm arm;
  private final TalonFX arm_master = new TalonFX(Constants.id_arm_master);
  private final TalonFX arm_sleeve = new TalonFX(Constants.id_arm_sleeve);
  private final TalonFXConfiguration config = new TalonFXConfiguration();
  private final Rotation2d max_angle = Rotation2d.fromDegrees(90);
  private MotionMagicDutyCycle reqPosMaster = new MotionMagicDutyCycle(0, false, Constants.ff_arm, 0, true); 
  private Follower reqFollowSleeve = new Follower(arm_master.getDeviceID(), true); 
  private Arm() {
    config.CurrentLimits.StatorCurrentLimitEnable = true;
    config.CurrentLimits.StatorCurrentLimit = 80.0;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;
    config.CurrentLimits.SupplyCurrentLimit = 40.0;
    arm_master.getConfigurator().apply(config);
    arm_sleeve.getConfigurator().apply(config);
    initMotors(); 
    setNeutralMode(NeutralModeValue.Brake);
    outputTelemetry();
    resetArmPos(0);
  }

  public static Arm getInstance (){
    if (arm == null){
      arm = new Arm();
    }
    return arm;
  }

  @Override
  public void periodic() {}

  public void initMotors (){
    TalonFXConfiguration master_configs = new TalonFXConfiguration();
    master_configs.Feedback.SensorToMechanismRatio = Constants.arm_ratio;
    master_configs.Slot0.kP = Constants.kp_arm;
    master_configs.Slot0.kI = Constants.ki_arm;
    master_configs.Slot0.kD = Constants.kd_arm;
    master_configs.Slot0.kS = Constants.ks_arm;
    master_configs.Slot0.kV = Constants.kv_arm;
    master_configs.MotionMagic.MotionMagicCruiseVelocity = 5;
    master_configs.MotionMagic.MotionMagicAcceleration = 10;
    master_configs.MotionMagic.MotionMagicJerk = 50;
    arm_master.getConfigurator().apply(master_configs); 
    arm_sleeve.setControl(reqFollowSleeve);
    //enableLimits(); 
  }

  public void setAngle (Rotation2d angle){
    arm_master.setControl(reqPosMaster.withPosition(angle.getRotations()));
  }

  public void setVelocity (double output){
    DutyCycleOut requestVel = new DutyCycleOut(output);
    arm_master.setControl(requestVel);  
  }

  public void setNeutralMode (NeutralModeValue mode){
    MotorOutputConfigs config = new MotorOutputConfigs();
    config.NeutralMode = mode;
    arm_master.getConfigurator().apply(config);
    arm_sleeve.getConfigurator().apply(config);
  }

  public Rotation2d getPosition (){
    return Rotation2d.fromRotations(arm_master.getPosition().getValue());
  }

  public Rotation2d getError (){
    return Rotation2d.fromRotations(arm_master.getClosedLoopError().getValue());
  }

  public boolean getLimitValue () {
    return arm_master.getReverseLimit().getValue() == ReverseLimitValue.ClosedToGround;
  }

  public void enableLimits (){
    TalonFXConfiguration limits_config = new TalonFXConfiguration();
    limits_config.SoftwareLimitSwitch.ForwardSoftLimitEnable = true; 
    limits_config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = max_angle.getRotations();
    limits_config.HardwareLimitSwitch.ReverseLimitType = ReverseLimitTypeValue.NormallyClosed;
    limits_config.HardwareLimitSwitch.ReverseLimitEnable = true;
    limits_config.HardwareLimitSwitch.ReverseLimitAutosetPositionEnable = true;
    limits_config.HardwareLimitSwitch.ReverseLimitAutosetPositionValue = 0.0;
    arm_master.getConfigurator().apply(limits_config);
  }

  public void disableLimits (){
    TalonFXConfiguration limits_config = new TalonFXConfiguration();
    limits_config.SoftwareLimitSwitch.ForwardSoftLimitEnable = false; 
    limits_config.HardwareLimitSwitch.ReverseLimitEnable = false;
    arm_master.getConfigurator().apply(limits_config);
  }

  public void resetArmPos (double position){
    arm_master.setRotorPosition(position); 
  }

  public void outputTelemetry (){
    Telemetry.mechanismTab.addBoolean("Arm RLimit", () -> getLimitValue());
    Telemetry.mechanismTab.addDouble("Arm Angle ", () -> getPosition().getDegrees());
    Telemetry.mechanismTab.addDouble("Arm Error", () -> getError().getDegrees()); 
  }
}
