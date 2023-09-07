package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants.MechanismMode;
import frc.robot.Constants.TypePipeline;
import frc.robot.interfaces.AutoInterface;
import frc.robot.modes.GamePieceMode;
import frc.robot.modes.MechanismActionMode;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Vision;

public class Robot extends TimedRobot {
  private ActionManager actionManager;
  private GamePieceMode gamePieceMode;
  private MechanismActionMode mechanismMode;
  private Drive drive  = Drive.getInstance();
  private Vision vision = Vision.getInstance();
  private Command autonomousCommand;
  
  public static boolean flip_alliance (){
    return DriverStation.getAlliance() == Alliance.Red ? true : false;
  }

  @Override
  public void robotInit() {
    actionManager = new ActionManager();
    gamePieceMode = GamePieceMode.getInstance();
    mechanismMode = MechanismActionMode.getInstance();
    Telemetry.displayAutos();
  }

  public void teleopBindings (){
    Controls.driver1.a().onTrue(drive.resetGyroComm());
    //Controls.driver2.rightBumper().whileTrue(ActionsSet.vel_pos);
    //Controls.driver2.leftBumper().whileTrue(ActionsSet.vel_neg);
    Controls.driver2.a().onTrue(gamePieceMode.toggleMode()); 
    Controls.driver2.b().onTrue(mechanismMode.toggleDriverMode());
    Controls.driver2.x().onTrue(mechanismMode.commandSetMode(MechanismMode.ManualMode));
    Controls.driver2.y().onTrue(mechanismMode.commandSetMode(MechanismMode.SaveMechanism));
    Controls.driver2.povUp().onTrue(actionManager.moveLevel(1));
    Controls.driver2.povDown().onTrue(actionManager.moveLevel(-1));
    Controls.driver2.povRight().onTrue(actionManager.moveStation(1));
    Controls.driver2.povLeft().onTrue(actionManager.moveStation(-1));
    Controls.driver2.rightBumper().onTrue(actionManager.requestAction());
    Controls.driver2.leftBumper().onTrue(actionManager.requestCancelAction());
  }
  
  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {}
  
  @Override
  public void disabledPeriodic() {}
  
  @Override
  public void autonomousInit() {
    AutoInterface auto_selected = Telemetry.auto_chooser.getSelected();
    if (auto_selected != null) {
      drive.resetOdometry(auto_selected.getStartingPose());
      vision.setPipeline(flip_alliance() ? TypePipeline.RedTags : TypePipeline.BlueTags);
      autonomousCommand = auto_selected.getAutoCommand();
      autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}
  
  @Override
  public void teleopInit() {
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }
    teleopBindings();
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
