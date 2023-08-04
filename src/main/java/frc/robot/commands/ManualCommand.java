package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Controls;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Articulation;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Telescopic;
import frc.robot.subsystems.Wrist;

public class ManualCommand extends CommandBase {
  private final Arm arm = Arm.getInstance();
  private final Telescopic telescopic = Telescopic.getInstance();
  private final Wrist wrist = Wrist.getInstance();
  private final Articulation articulation = Articulation.getInstance();
  private final Intake intake = Intake.getInstance();
  private final DoubleSupplier armSupplier = Controls.getLeftYD2();
  private final DoubleSupplier telSupplier = Controls.getRightYD2();
  private final DoubleSupplier wrist_art_Supplier1 = Controls.getLeftTD2(); 
  private final DoubleSupplier wrist_art_Supplier2 = Controls.getRightTD2(); 
  private final DoubleSupplier intakeSupplier1 = Controls.getLeftTD1();
  private final DoubleSupplier intakeSupplier2 = Controls.getRightTD1();
  public ManualCommand() {
    addRequirements(arm, telescopic, wrist, articulation, intake);
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
