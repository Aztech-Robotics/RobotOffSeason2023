package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Controls;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Wrist;

public class ManualCommand extends CommandBase {
  private final Arm arm = Arm.getInstance();
  private final Wrist wrist = Wrist.getInstance(); 
  private final Intake intake = Intake.getInstance(); 
  private final DoubleSupplier armSupplier = Controls.getLeftYD2(); 
  private final DoubleSupplier wristSupplier = Controls.getRightYD2(); 
  private final DoubleSupplier intakeSupplier = Controls.getTD2(); 

  public ManualCommand() {
    addRequirements(arm, wrist, intake); 
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    arm.setVelocity(armSupplier.getAsDouble() * 0.5);
    wrist.setVelocity(wristSupplier.getAsDouble() * 0.3);
    intake.setVelocity(intakeSupplier.getAsDouble() * 0.5);
  }

  @Override
  public void end(boolean interrupted) {
    arm.setVelocity(0);
    wrist.setVelocity(0);
    intake.setVelocity(0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
