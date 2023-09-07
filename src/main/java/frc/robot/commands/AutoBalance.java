package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive; 

public class AutoBalance extends CommandBase {
  private final Drive drive = Drive.getInstance();
  private final boolean isBackward;
  public AutoBalance(boolean isBackward) {
    this.isBackward = isBackward;
    addRequirements(drive);
  }

  @Override
  public void initialize() {
    drive.initAutoBalance(isBackward);
  }

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return drive.autoBIsDone(); 
  }
}
