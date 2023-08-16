package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.ForwardLimitValue;
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
  private Rotation2d targetAngle = null;
  private boolean isAtTarget = false;
  private Arm() {
    config.CurrentLimits.StatorCurrentLimitEnable = true;
    config.CurrentLimits.StatorCurrentLimit = 80.0;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;
    config.CurrentLimits.SupplyCurrentLimit = 50.0;
    config.Feedback.SensorToMechanismRatio = Constants.arm_ratio;
    config.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
    config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = 0.0; //Max Angle
    config.HardwareLimitSwitch.ReverseLimitType = ReverseLimitTypeValue.NormallyClosed;
    config.HardwareLimitSwitch.ReverseLimitEnable = true;
    config.HardwareLimitSwitch.ReverseLimitAutosetPositionEnable = true;
    config.HardwareLimitSwitch.ReverseLimitAutosetPositionValue = 0.0; //Zero Pos
    config.Slot0.kP = Constants.kp_arm;
    config.Slot0.kI = Constants.ki_arm;
    config.Slot0.kD = Constants.kd_arm;
    config.Slot0.kS = Constants.ks_arm;
    config.Slot0.kV = Constants.kv_arm;
    arm_master.getConfigurator().apply(config);
    arm_sleeve.getConfigurator().apply(config);
    setNeutralMode(NeutralModeValue.Coast);
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
    if (targetAngle != null && Math.abs(arm_master.getClosedLoopError().getValue()) < 1){
      isAtTarget = true;
      targetAngle = null; 
    }
  }

  public void setAngle (Rotation2d angle){
    PositionDutyCycle requestMaster = new PositionDutyCycle(angle.getRotations(), true, Constants.ff_arm, 0, true);
    Follower requestSleeve = new Follower(arm_master.getDeviceID(), true);
    arm_master.setControl(requestMaster);
    arm_sleeve.setControl(requestSleeve);
    targetAngle = angle;
    isAtTarget = false;
  }

  public void setVelocity (double output){
    DutyCycleOut requestVel = new DutyCycleOut(output);
    arm_master.setControl(requestVel);  
    Follower requestSleeve = new Follower(arm_master.getDeviceID(), true);
    arm_sleeve.setControl(requestSleeve);
  }

  public boolean isAtTargetPosition () {
    return isAtTarget;
  }

  public void setNeutralMode (NeutralModeValue mode){
    MotorOutputConfigs config = new MotorOutputConfigs();
    config.NeutralMode = mode;
    arm_master.getConfigurator().apply(config);
    arm_sleeve.getConfigurator().apply(config);
  }

  public void outputTelemetry (){
    Telemetry.mechanismTab.addDouble("Arm Angle ", () -> Rotation2d.fromRotations(arm_master.getPosition().getValue()).getDegrees());
    Telemetry.mechanismTab.addBoolean("Arm FLimit", () -> arm_master.getForwardLimit().getValue() == ForwardLimitValue.Open ? true : false);
    Telemetry.mechanismTab.addBoolean("Arm RLimit", () -> arm_master.getReverseLimit().getValue() == ReverseLimitValue.Open ? true : false);
  }
}
