package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class IntakeVel extends CommandBase {
  private final Intake intake = Intake.getInstance();
  private final double velocity;
  public IntakeVel(double velocity) {
    this.velocity = velocity;
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    intake.setVelocity(velocity);
  }

  @Override
  public void end(boolean interrupted) {
    intake.setVelocity(0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
