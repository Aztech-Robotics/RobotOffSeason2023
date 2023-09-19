package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Controls;
import frc.robot.Constants.GameElement;
import frc.robot.Constants.MechanismMode;
import frc.robot.modes.GamePieceMode;
import frc.robot.modes.MechanismActionMode;
import frc.robot.subsystems.Intake;

public class IntakeVel extends CommandBase {
  private final Intake intake = Intake.getInstance();
  public IntakeVel() {
    addRequirements(intake);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    MechanismMode mechanismMode = MechanismActionMode.getInstance().getMode(); 
    GameElement gameElement = GamePieceMode.getInstance().getMode();
    double velocity = Controls.getTD2().getAsDouble();
    if ((mechanismMode == MechanismMode.PickUp && gameElement == GameElement.Cone) || (mechanismMode == MechanismMode.Score && gameElement == GameElement.Cone)){
      velocity = -velocity; 
    }
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
