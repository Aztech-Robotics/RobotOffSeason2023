package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;

public class ArmPosition extends CommandBase {
  private Arm arm = Arm.getInstance();
  private double target_angle;
  public ArmPosition(double target_angle) {
    this.target_angle = target_angle;
    addRequirements(arm); 
  }

  @Override
  public void initialize() {
    arm.setAngle(target_angle);
  }

  @Override
  public boolean isFinished() {
    return Math.abs(target_angle - arm.getPosition()) <= Rotation2d.fromDegrees(2).getRotations();
  }
}
