package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.event.BooleanEvent;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.MechanismMode;
import frc.robot.modes.GamePieceMode;
import frc.robot.modes.MechanismActionMode;

public class Robot extends TimedRobot {
  private GamePieceMode gamePieceMode;
  private MechanismActionMode mechanismMode;
  private Telemetry telemetry;
  private ActionManager actionManager;
  private Command m_autonomousCommand;
  private EventLoop loop_modes = new EventLoop();
  private Command currentCommand = null;
  private boolean currCommandScheduled = false;
  
  public static boolean flip_alliance (){
    return DriverStation.getAlliance() == Alliance.Red ? true : false;
  }

  @Override
  public void robotInit() {
    telemetry = Telemetry.getInstance(); 
    gamePieceMode = GamePieceMode.getInstance();
    mechanismMode = MechanismActionMode.getInstance();
    actionManager = ActionManager.getInstance();
    currentCommand = null;
    loop_modes.clear();
  }

  public void teleopBindings (){
    Controls.driver2.a().onTrue(gamePieceMode.toggleMode());
    Controls.driver2.b().onTrue(mechanismMode.toggleDriverMode());
    Controls.driver2.x().onTrue(mechanismMode.commandSetMode(MechanismMode.ManualMode));
    Controls.driver2.y().onTrue(mechanismMode.commandSetMode(MechanismMode.SaveMechanism));
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    loop_modes.poll();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = null;
    //m_autonomousCommand = telemetry.getAutonomousCommand();
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {
    /*
    if (currentCommand != null){
      if (currCommandScheduled ){
        if (currentCommand.isFinished()){
          currentCommand = null;
          currCommandScheduled = false;
        }
      } else {
        currentCommand.schedule();
        currCommandScheduled = true; 
      }
    }
    */
  }

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
