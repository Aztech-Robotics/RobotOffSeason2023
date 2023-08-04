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
import frc.robot.Constants.MechanismMode;
import frc.robot.modes.GamePieceMode;
import frc.robot.modes.MechanismActionMode;

public class Robot extends TimedRobot {
  private GamePieceMode gamePieceMode;
  private MechanismActionMode mechanismMode;
  private Telemetry telemetry;
  private Command m_autonomousCommand;
  private EventLoop loop_modes = new EventLoop();
  
  public static boolean flip_alliance (){
    return DriverStation.getAlliance() == Alliance.Red ? true : false;
  }

  public void modesBindings (){
    BooleanEvent gamePieceChanged = new BooleanEvent(loop_modes, () -> gamePieceMode.haveChanged());

  }

  public void controlBindings (){
    Controls.getAD2().toggleOnTrue(gamePieceMode.toggleMode());
    Controls.getBD2().toggleOnTrue(mechanismMode.toggleDriverMode());
    Controls.getXD2().toggleOnTrue(mechanismMode.commandSetMode(MechanismMode.ManualMode));
    Controls.getYD2().toggleOnTrue(mechanismMode.commandSetMode(MechanismMode.SaveMechanism));
    InstantCommand moveActiveBox = new InstantCommand(
        () -> {
            int up = Controls.getUpD2().getAsBoolean()? 1 : 0;
            int down = Controls.getDownD2().getAsBoolean()? -1 : 0;
            int left = Controls.getLeftD2().getAsBoolean()? -1 : 0;
            int right = Controls.getRightD2().getAsBoolean()? 1 : 0;
            int moveOverY = up + down;
            int moveOverX = left + right;
            if (mechanismMode.getMode() == MechanismMode.PickUp){
                telemetry.moveActiveStation(moveOverX);
            } else if (mechanismMode.getMode() == MechanismMode.Score){
                telemetry.moveActiveNode(moveOverX, moveOverY);
            }
        }
    );
    InstantCommand moveActiveBox2 = new InstantCommand(
        () -> {
            double x = Controls.getLeftXD2().getAsDouble();
            double y = Controls.getLeftYD2().getAsDouble();
            int moveOverX = 0, moveOverY = 0;
            double m = 2;
            if (x != 0){
                m = y/x;
            }
            if (Math.abs(m) >= 1 || m == 2){
                moveOverY = (int)(y / Math.abs(y));
            } else {
                moveOverX = (int)(x / Math.abs(x));
            }
            if (mechanismMode.getMode() == MechanismMode.PickUp){
                telemetry.moveActiveStation(moveOverX);
            } else if (mechanismMode.getMode() == MechanismMode.Score){
                telemetry.moveActiveNode(moveOverX, moveOverY);
            }
        }
    );
    Controls.movingLeftJD2().whileTrue(new RepeatCommand(new SequentialCommandGroup(moveActiveBox2, new WaitCommand(0.3))));
}

  @Override
  public void robotInit() {
    telemetry = Telemetry.getInstance(); 
    gamePieceMode = GamePieceMode.getInstance();
    mechanismMode = MechanismActionMode.getInstance();
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
    m_autonomousCommand = telemetry.getAutonomousCommand();
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {
    
  }

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
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
