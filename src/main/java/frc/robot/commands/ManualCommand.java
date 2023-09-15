package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Controls;
import frc.robot.subsystems.Arm;

public class ManualCommand extends CommandBase {
  private final Arm arm = Arm.getInstance();
  private final DoubleSupplier armSupplier = Controls.getLeftYD2();

  public ManualCommand() {
    addRequirements(arm);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    arm.setVelocity(armSupplier.getAsDouble());
  }

  @Override
  public void end(boolean interrupted) {
    arm.setVelocity(0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
