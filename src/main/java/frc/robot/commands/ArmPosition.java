package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;

public class ArmPosition extends CommandBase {
  private Arm arm = Arm.getInstance();
  public ArmPosition() {
    addRequirements(arm); 
  }

  @Override
  public void initialize() {
    arm.setAngle(Rotation2d.fromDegrees(90));
  }

  @Override
  public boolean isFinished() {
    return arm.isAtTargetPosition();
  }
}
