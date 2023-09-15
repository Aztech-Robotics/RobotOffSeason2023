package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;

public class ArmPosition extends CommandBase {
  private Arm arm = Arm.getInstance();
  private Rotation2d target_angle;
  public ArmPosition(Rotation2d target_angle) {
    this.target_angle = target_angle;
    addRequirements(arm); 
  }

  @Override
  public void initialize() {
    arm.setAngle(target_angle);
  }
  
  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {
    arm.setVelocity(0);
  }

  @Override
  public boolean isFinished() {
    return false; //Math.abs(target_angle.getDegrees()) - Math.abs(Rotation2d.fromRotations(arm.getPosition()).getDegrees()) <= 2;
  }
}
