package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Wrist;

public class WristPosition extends CommandBase {
  private final Wrist wrist = Wrist.getInstance(); 
  private final double target_angle;
  public WristPosition(double target_angle) {
    this.target_angle = target_angle; 
    addRequirements(wrist);
  }

  @Override
  public void initialize() {
    wrist.setTargetAngle(target_angle); 
  }

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {
    wrist.setVelocity(0);
  }

  @Override
  public boolean isFinished() {
    return Math.abs(target_angle - wrist.getPos()) <= Rotation2d.fromDegrees(2).getRotations();
  }
}
