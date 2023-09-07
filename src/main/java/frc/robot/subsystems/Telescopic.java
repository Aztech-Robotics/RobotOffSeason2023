package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
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
  private boolean isBrakeMode = false; 
  private final float max_pos = 10;

  private Telescopic() {
    tel_master.enableVoltageCompensation(12);
    tel_master.setSmartCurrentLimit(40);
    tel_sleeve.setSmartCurrentLimit(40);
    enc_tel.setPositionConversionFactor(1 / Constants.tel_ratio);
    tel_controller.setP(Constants.kp_tel);
    tel_controller.setI(Constants.ki_tel);
    tel_controller.setD(Constants.kd_tel);
    tel_controller.setSmartMotionMaxVelocity(0, 0);
    tel_controller.setSmartMotionMaxAccel(0, 0);
    tel_controller.setSmartMotionAllowedClosedLoopError(0, 0); 
    setNeutralMode(IdleMode.kBrake);
    outputTelemetry();
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
  }

  public void setNeutralMode (IdleMode mode){
    isBrakeMode = mode == IdleMode.kBrake ? true : false;
    tel_master.setIdleMode(mode);
    tel_sleeve.setIdleMode(mode);
  }

  public void enableLimits (){
    tel_master.enableSoftLimit(SoftLimitDirection.kReverse, true);
    tel_master.setSoftLimit(SoftLimitDirection.kReverse, 0); 
    tel_master.enableSoftLimit(SoftLimitDirection.kForward, true);
    tel_master.setSoftLimit(SoftLimitDirection.kForward, max_pos); 
  }

  public void disableLimits (){
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
    Telemetry.mechanismTab.addDouble("Tel Pos", () -> getPos());
  }
}
