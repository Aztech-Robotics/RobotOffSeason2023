package frc.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MechanismMode;
import frc.robot.actions.PickUpDouble;
import frc.robot.actions.PickUpFloor;
import frc.robot.actions.PickUpSingle;
import frc.robot.actions.ScoreBottom;
import frc.robot.actions.ScoreMiddle;
import frc.robot.actions.ScoreTop;
import frc.robot.commands.FieldOrientedDrive;
import frc.robot.interfaces.ActionInterface;
import frc.robot.modes.GamePieceMode;
import frc.robot.modes.MechanismActionMode;
import frc.robot.subsystems.Drive;

public class ActionManager extends SubsystemBase {
    private Drive drive = Drive.getInstance();
    private FieldOrientedDrive control_drive = new FieldOrientedDrive(); 
    private MechanismActionMode mechanismActionMode = MechanismActionMode.getInstance();
    private Map<Integer, ActionInterface> scores_map;
    private Map<Integer, ActionInterface> stations_map;
    private List<Command> commandsAwait = new ArrayList<>();
    private int level, station;
    private Command commandRunning;

    public ActionManager (){
        drive.setDefaultCommand(control_drive);
        scores_map = Map.of(1, new ScoreBottom(), 2, new ScoreMiddle(), 3, new ScoreTop());
        stations_map = Map.of(1, new PickUpFloor(), 2, new PickUpSingle(), 3, new PickUpDouble());
        level = 1;
        station = 1;
        commandRunning = null;
        outputTelemetry();
    }

    @Override
    public void periodic (){
        if (commandRunning != null){
            if (!commandRunning.isScheduled()){
                commandRunning.schedule();
            } else {
                if (commandRunning.isFinished()){
                    commandRunning = null;
                }
            }
        }
    }

    public Command requestAction (){
        return runOnce(
            () -> {
                if (commandRunning != null){
                    DriverStation.reportError("A command is already running. Cancel the action!!", false);
                } 
                else {
                    if (!commandsAwait.isEmpty()){
                        commandRunning = commandsAwait.get(0);
                        commandsAwait.remove(0);
                    }
                    else {
                        switch (mechanismActionMode.getMode()){
                            case ManualMode:
                            commandRunning = ActionsSet.manual_mode;
                            break;
                            case SaveMechanism:
                            commandRunning = ActionsSet.save_mechanism;
                            break;
                            case PickUp:
                            commandRunning = stations_map.get(station).getActionCommand();
                            break;
                            case Score:
                            commandRunning = ActionsSet.prepare_high_pos;
                            commandsAwait.add(scores_map.get(level).getActionCommand());
                            break;
                        }
                    }
                }
            }
        );
    }

    public Command requestCancelAction (){
        return runOnce(
            () -> {
                if (commandRunning != null){
                    commandRunning.cancel();
                    commandRunning = null;
                }
                commandsAwait.clear();
            }
        );
    }

    public CommandBase moveLevel (int units){
        return runOnce(
            () -> {
                if (mechanismActionMode.getMode() == MechanismMode.Score && level + units >= 1 && level + units <= 3){
                    level = level + units;
                }
            }
        );
    }

    public CommandBase moveStation (int units){
        return runOnce(
            () -> {
                if (mechanismActionMode.getMode() == MechanismMode.PickUp && station + units >= 1 && station + units <= 3){
                    station = station + units;
                }
            }
        );
    }

    public void outputTelemetry (){
        Telemetry.driverTab.addString("Level", 
        () -> {
            String value = "";
            switch (level){
                case 1:
                value = "Bottom";
                break;
                case 2:
                value = "Middle";
                break;
                case 3:
                value = "Top";
                break;
            }
            return value;
        }).withPosition(2, 0);
        Telemetry.driverTab.addString("Station", 
        () -> {
            String value = "";
            switch (station){
                case 1:
                value = "Floor";
                break;
                case 2:
                value = "Single";
                break;
                case 3:
                value = "Double";
                break;
            }
            return value;
        }).withPosition(3, 0);
    }
}
