package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Telemetry;

public class Wrist extends SubsystemBase {
  private static Wrist wrist;
  private final CANSparkMax wrist_m = new CANSparkMax(Constants.id_wrist, MotorType.kBrushless);
  private final RelativeEncoder wrist_enc = wrist_m.getEncoder();
  private final SparkMaxPIDController wrist_controller = wrist_m.getPIDController();
  private boolean isBrakeMode = false; 
  private final Rotation2d max_angle = Rotation2d.fromDegrees(250);
  
  private Wrist() {
    wrist_m.enableVoltageCompensation(12);
    wrist_m.setSmartCurrentLimit(50);
  }

  public static Wrist getInstance (){
    if (wrist == null){
      wrist = new Wrist();
    }
    return wrist;
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
    wrist_m.setIdleMode(mode);
  }

  public void enableLimits (){
    wrist_m.enableSoftLimit(SoftLimitDirection.kReverse, true);
    wrist_m.setSoftLimit(SoftLimitDirection.kReverse, 0); 
    wrist_m.enableSoftLimit(SoftLimitDirection.kForward, true);
    wrist_m.setSoftLimit(SoftLimitDirection.kForward, 250); 
  }

  public void disableLimits (){
    wrist_m.enableSoftLimit(SoftLimitDirection.kReverse, false);
    wrist_m.enableSoftLimit(SoftLimitDirection.kForward, false);
  }

  public void setVelocity (double velocity){
    wrist_controller.setReference(velocity, CANSparkMax.ControlType.kDutyCycle);
  }

  public void setTargetPos (double rotations){
    wrist_controller.setReference(rotations, CANSparkMax.ControlType.kSmartMotion);
  }

  public double getPos (){
    return wrist_enc.getPosition();
  }

  public void outputTelemetry (){
    Telemetry.mechanismTab.addDouble("Tel Pos", () -> getPos());
  }
}
