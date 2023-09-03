package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.ReverseLimitTypeValue;
import com.ctre.phoenix6.signals.ReverseLimitValue;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Telemetry;

public class Arm extends SubsystemBase {
  private static Arm arm;
  private final TalonFX arm_master = new TalonFX(Constants.id_arm_master);
  private final TalonFX arm_sleeve = new TalonFX(Constants.id_arm_sleeve);
  private final TalonFXConfiguration config = new TalonFXConfiguration();
  private final Rotation2d max_angle = Rotation2d.fromDegrees(80);
  private boolean isBrakeMode = false;
  private Arm() {
    config.CurrentLimits.StatorCurrentLimitEnable = true;
    config.CurrentLimits.StatorCurrentLimit = 80.0;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;
    config.CurrentLimits.SupplyCurrentLimit = 50.0;
    config.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive; 
    arm_master.getConfigurator().apply(config);
    arm_sleeve.getConfigurator().apply(config);
    initMaster(); 
    setNeutralMode(NeutralModeValue.Brake);
    outputTelemetry();
  }

  public static Arm getInstance (){
    if (arm == null){
      arm = new Arm();
    }
    return arm;
  }

  @Override
  public void periodic() {
    arm_master.getClosedLoopError().refresh();
    arm_master.getPosition().refresh();
    if (RobotState.isEnabled()){
      if (!isBrakeMode) setNeutralMode(NeutralModeValue.Brake);
    } else {
      if (isBrakeMode) setNeutralMode(NeutralModeValue.Coast);
    }
  }

  public void initMaster (){
    TalonFXConfiguration master_configs = new TalonFXConfiguration();
    master_configs.Feedback.SensorToMechanismRatio = Constants.arm_ratio;
    master_configs.Slot0.kP = Constants.kp_arm;
    master_configs.Slot0.kI = Constants.ki_arm;
    master_configs.Slot0.kD = Constants.kd_arm;
    master_configs.Slot0.kS = Constants.ks_arm;
    master_configs.Slot0.kV = Constants.kv_arm;
    arm_master.getConfigurator().apply(master_configs); 
    enableLimits(); 
  }

  public void setAngle (double angle){
    PositionDutyCycle requestMaster = new PositionDutyCycle(angle, false, Constants.ff_arm, 0, true);
    Follower requestSleeve = new Follower(arm_master.getDeviceID(), true);
    arm_master.setControl(requestMaster);
    arm_sleeve.setControl(requestSleeve);
  }

  public void setVelocity (double output){
    DutyCycleOut requestVel = new DutyCycleOut(output);
    arm_master.setControl(requestVel);  
    Follower requestSleeve = new Follower(arm_master.getDeviceID(), true);
    arm_sleeve.setControl(requestSleeve);
  }

  public void setNeutralMode (NeutralModeValue mode){
    isBrakeMode = mode == NeutralModeValue.Brake ? true : false;
    MotorOutputConfigs config = new MotorOutputConfigs();
    config.NeutralMode = mode;
    arm_master.getConfigurator().apply(config);
    arm_sleeve.getConfigurator().apply(config);
  }

  public double getPosition (){
    return arm_master.getPosition().getValue();
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

  public void outputTelemetry (){
    Telemetry.mechanismTab.addBoolean("Arm RLimit", () -> arm_master.getReverseLimit().getValue() == ReverseLimitValue.ClosedToGround ? true : false);
    Telemetry.mechanismTab.addDouble("Arm Angle ", () -> Rotation2d.fromRotations(getPosition()).getDegrees());
  }
}
