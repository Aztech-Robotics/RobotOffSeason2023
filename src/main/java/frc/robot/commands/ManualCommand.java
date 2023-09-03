package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Controls;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Telescopic;
import frc.robot.subsystems.Wrist;

public class ManualCommand extends CommandBase {
  private final Arm arm = Arm.getInstance();
  private final Telescopic telescopic = Telescopic.getInstance();
  private final Wrist wrist = Wrist.getInstance();
  private final Intake intake = Intake.getInstance();
  private final DoubleSupplier armSupplier = Controls.getLeftYD2();
  private final DoubleSupplier telSupplier = Controls.getRightYD2();
  private final DoubleSupplier wrist_Supplier1 = Controls.getLeftTD2(); 
  private final DoubleSupplier wrist__Supplier2 = Controls.getRightTD2(); 
  public ManualCommand() {
    addRequirements(arm, telescopic, wrist, intake);
  }

  @Override
  public void execute() {
    arm.setVelocity(armSupplier.getAsDouble());
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
