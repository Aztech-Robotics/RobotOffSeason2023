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
  private final DoubleSupplier intake_Supplier1 = Controls.getLeftTD2(); 
  private final DoubleSupplier intake__Supplier2 = Controls.getRightTD2(); 

  public ManualCommand() {
    addRequirements(arm, wrist, intake);
  }

  @Override
  public void initialize() {
    wrist.disableLimits();
  }

  @Override
  public void execute() {
    arm.setVelocity(armSupplier.getAsDouble());
    wrist.setVelocity(0.5 * wristSupplier.getAsDouble());
    intake.setVelocity(intake__Supplier2.getAsDouble() - intake_Supplier1.getAsDouble());
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
