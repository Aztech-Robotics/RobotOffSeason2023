package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Arm extends SubsystemBase {
  private static Arm arm;
  private CANSparkMax arm_master = new CANSparkMax(Constants.id_arm_master, MotorType.kBrushless);
  private RelativeEncoder encoder = arm_master.getEncoder();
  private SparkMaxPIDController pid_controller = arm_master.getPIDController();
  private SparkMaxLimitSwitch limit_reverse = arm_master.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
  private CANSparkMax arm_sleeve = new CANSparkMax(Constants.id_arm_sleeve, MotorType.kBrushless);
  private ArmFeedforward feedforward = new ArmFeedforward(0, 0, 0);
  private double desiredPosition = 0;
  private Arm() {
    arm_master.enableVoltageCompensation(12);
    arm_master.setInverted(false);
    limit_reverse.enableLimitSwitch(true);
    arm_master.enableSoftLimit(SoftLimitDirection.kForward, true);
    arm_master.setSoftLimit(SoftLimitDirection.kForward, 0);
    encoder.setPositionConversionFactor(Constants.arm_reduction);
    pid_controller.setP(0, 0);
    pid_controller.setI(0, 0);
    pid_controller.setD(0, 0);
    pid_controller.setFF(0, 0);
    pid_controller.setOutputRange(0, 1, 0);
    arm_sleeve.follow(arm_master, true);
  }

  public static Arm getInstance (){
    if (arm == null){
      arm = new Arm();
    }
    return arm;
  }

  public void setNeutralMode (IdleMode idleMode) {
    arm_master.setIdleMode(idleMode);
    arm_sleeve.setIdleMode(idleMode);
  }

  @Override
  public void periodic() {
  }

  public void setAngle (Rotation2d angle){
  }

  public boolean isAtTargetPosition () {
    return false; 
  }
}
