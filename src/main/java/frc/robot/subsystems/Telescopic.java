package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxLimitSwitch.Type;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Telemetry;

public class Telescopic extends SubsystemBase {
  private static Telescopic telescopic;
  private final CANSparkMax tel_master = new CANSparkMax(Constants.id_tel_master, MotorType.kBrushless);
  private final CANSparkMax tel_sleeve = new CANSparkMax(Constants.id_tel_sleeve, MotorType.kBrushless);
  private final RelativeEncoder enc_tel = tel_master.getEncoder(); 
  private final SparkMaxPIDController  tel_controller = tel_master.getPIDController();
  private final SparkMaxLimitSwitch limit = tel_master.getReverseLimitSwitch(Type.kNormallyClosed); 
  private boolean isBrakeMode = false; 

  private Telescopic() {
    tel_master.enableVoltageCompensation(12);
    tel_master.setSmartCurrentLimit(50);
    tel_sleeve.setSmartCurrentLimit(50);
    enc_tel.setPositionConversionFactor(0);
    tel_controller.setP(0);
    tel_controller.setI(0);
    tel_controller.setD(0);
    tel_controller.setSmartMotionMaxVelocity(0, 0);
    tel_controller.setSmartMotionMaxAccel(0, 0);
    tel_controller.setSmartMotionAllowedClosedLoopError(0, 0); 
    setNeutralMode(IdleMode.kCoast);
  }

  public static Telescopic getInstance (){
    if (telescopic == null){
      telescopic = new Telescopic();
    }
    return telescopic;
  }

  @Override
  public void periodic() {
    if (RobotState.isEnabled()){
      if (!isBrakeMode) setNeutralMode(IdleMode.kBrake);
    } else {
      if (isBrakeMode) setNeutralMode(IdleMode.kCoast);
    }
    if (limit.isPressed()){
      enc_tel.setPosition(0);
    }
  }

  public void setNeutralMode (IdleMode mode){
    isBrakeMode = mode == IdleMode.kBrake ? true : false;
    tel_master.setIdleMode(mode);
    tel_sleeve.setIdleMode(mode);
  }

  public void enableLimits (){
    limit.enableLimitSwitch(true);
    tel_master.enableSoftLimit(SoftLimitDirection.kForward, true);
    tel_master.setSoftLimit(SoftLimitDirection.kForward, 0); 
  }

  public void disableLimits (){
    limit.enableLimitSwitch(false);
    tel_master.enableSoftLimit(SoftLimitDirection.kForward, false);
  }

  public void setVelocity (double velocity){
    tel_controller.setReference(velocity, CANSparkMax.ControlType.kDutyCycle);
    tel_sleeve.follow(tel_master, true);
  }

  public void setTargetPos (double rotations){
    tel_controller.setReference(rotations, CANSparkMax.ControlType.kSmartMotion);
    tel_sleeve.follow(tel_master, true);
  }

  public double getPos (){
    return enc_tel.getPosition();
  }

  public void outputTelemetry (){
    Telemetry.mechanismTab.addBoolean("Tel RLimit", () -> limit.isPressed());
    Telemetry.mechanismTab.addDouble("Tel Pos", () -> getPos());
  }
}
