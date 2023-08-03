package frc.robot;

import edu.wpi.first.wpilibj.event.BooleanEvent;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.MechanismMode;
import frc.robot.modes.GamePieceMode;
import frc.robot.modes.MechanismActionMode;

public class RobotContainer {
    private GamePieceMode gamePieceMode = GamePieceMode.getInstance();
    private MechanismActionMode mechanismMode = MechanismActionMode.getInstance();
    private Telemetry telemetry;
    
    public RobotContainer (){
        telemetry = Telemetry.getInstance();
    }

    public void modeBindings (EventLoop loop){
    }

    public void controlBindings (){
        Controls.getAD2().toggleOnTrue(gamePieceMode.toggleMode());
        Controls.getBD2().toggleOnTrue(mechanismMode.toggleDriverMode());
        Controls.getXD2().toggleOnTrue(mechanismMode.commandSetMode(MechanismMode.ManualMode));
        Controls.getYD2().toggleOnTrue(mechanismMode.commandSetMode(MechanismMode.SaveMechanism));
        InstantCommand moveActiveBox = new InstantCommand(
            () -> {
                int up = Controls.driver2.povUp().getAsBoolean()? 1 : 0;
                int down = Controls.driver2.povDown().getAsBoolean()? -1 : 0;
                int left = Controls.driver2.povLeft().getAsBoolean()? -1 : 0;
                int right = Controls.driver2.povRight().getAsBoolean()? 1 : 0;
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
        Controls.movingLeftJD2().whileTrue(new RepeatCommand(new SequentialCommandGroup(moveActiveBox, new WaitCommand(0.3))));
        Controls.getRBumperD2().toggleOnTrue(moveActiveBox2);
    }

    public Command getAutonomousCommand (){
        return telemetry.getAutonomousCommand();
    }
}

/*
 * Driver 1
 * Joysticks - Swerve
 * Right Bumper - When pressed will chase point of interest
 * Driver 2
 * Left Joystick - Movement on interface
 * A - Toggle game piece mode
 * B - Toggle mechanism mode (PickUp / Score)
 * X - Change mechanism to manual mode
 * Y - To save mechanism
 * Right Bumper - It'll launch the action
 * Left Bumper - It'll interrupt the current action
 */