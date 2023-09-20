package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Telemetry;

public class Wrist extends SubsystemBase {
  private static Wrist wrist;
  private final CANSparkMax wrist_m = new CANSparkMax(Constants.id_wrist, MotorType.kBrushless);
  private final RelativeEncoder wrist_enc = wrist_m.getEncoder();
  private final SparkMaxPIDController wrist_controller = wrist_m.getPIDController();
  private final Rotation2d max_angle = Rotation2d.fromDegrees(220);
  
  private Wrist() {
    wrist_m.enableVoltageCompensation(12);
    wrist_m.setSmartCurrentLimit(40);
    wrist_enc.setPositionConversionFactor(Constants.wristPositionCoefficient); 
    wrist_controller.setP(Constants.kp_wrist);
    wrist_controller.setI(Constants.ki_wrist);
    wrist_controller.setD(Constants.kd_wrist);
    wrist_controller.setFF(Constants.kff_wrist);
    wrist_controller.setIZone(Constants.kIz_wrist); 
    wrist_controller.setSmartMotionMaxVelocity(2000, 0);
    wrist_controller.setSmartMotionMaxAccel(1500, 0);
    wrist_controller.setOutputRange(-1, 1);
    wrist_controller.setSmartMotionAllowedClosedLoopError(2, 0); 
    setNeutralMode(IdleMode.kBrake);
    outputTelemetry();
    wrist_enc.setPosition(0);
  }

  public static Wrist getInstance (){
    if (wrist == null){
      wrist = new Wrist();
    }
    return wrist;
  }

  public void setNeutralMode (IdleMode mode){
    wrist_m.setIdleMode(mode);
  }

  public void enableLimits (){
    wrist_m.enableSoftLimit(SoftLimitDirection.kReverse, true);
    wrist_m.setSoftLimit(SoftLimitDirection.kReverse, 0); 
    wrist_m.enableSoftLimit(SoftLimitDirection.kForward, true);
    wrist_m.setSoftLimit(SoftLimitDirection.kForward, (float) max_angle.getDegrees()); 
  }

  public void disableLimits (){
    wrist_m.enableSoftLimit(SoftLimitDirection.kReverse, false);
    wrist_m.enableSoftLimit(SoftLimitDirection.kForward, false);
  }

  public void setVelocity (double velocity){
    wrist_controller.setReference(velocity, CANSparkMax.ControlType.kDutyCycle);
  }

  public void setTargetAngle (double angle){
    wrist_controller.setReference(angle, CANSparkMax.ControlType.kSmartMotion);
  }

  public double getPos (){
    return wrist_enc.getPosition();
  }

  public void outputTelemetry (){
    Telemetry.mechanismTab.addDouble("Wrist Pos", () -> getPos());
  }
}
