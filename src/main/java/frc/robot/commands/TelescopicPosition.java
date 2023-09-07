package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Telescopic;

public class TelescopicPosition extends CommandBase {
  private final Telescopic telescopic = Telescopic.getInstance(); 
  private final double target_pos;
  public TelescopicPosition(double target_pos) {
    this.target_pos = target_pos;
    addRequirements(telescopic);
  }

  @Override
  public void initialize() {
    telescopic.setTargetPos(target_pos);
  }

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {
    telescopic.setVelocity(0);
  }

  @Override
  public boolean isFinished() {
    return Math.abs(target_pos - telescopic.getPos()) <= 0.5;
  }
}
