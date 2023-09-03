package frc.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MechanismMode;
import frc.robot.commands.FieldOrientedDrive;
import frc.robot.interfaces.ActionInterface;
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
    private ActionInterface sticky_action;

    public ActionManager (){
        drive.setDefaultCommand(control_drive);
        scores_map = Map.of(1, ActionsSet.score_bottom, 2, ActionsSet.score_middle, 3, ActionsSet.score_top);
        stations_map = Map.of(1, ActionsSet.pickup_floor, 2, ActionsSet.pickup_single, 3, ActionsSet.pickup_double);
        level = 1;
        station = 1;
        commandRunning = null;
        outputTelemetry();
    }

    @Override
    public void periodic (){
        if (commandRunning != null){
            if (commandRunning.isFinished()){
                commandRunning = null;
            }
        }
    }

    public CommandBase requestAction (){
        return runOnce(
            () -> {
                if (!commandsAwait.isEmpty()){
                    commandRunning = commandsAwait.get(0);
                    commandsAwait.remove(0);
                }
                else {
                    switch (mechanismActionMode.getMode()){
                        case ManualMode:
                        commandRunning = ActionsSet.manual_mode;
                        sticky_action = null;
                        break;
                        case SaveMechanism:
                        commandRunning = ActionsSet.save_mechanism;
                        sticky_action = null;
                        break;
                        case PickUp:
                        ActionInterface action = stations_map.get(station);
                        commandRunning = action.getActionCommand();
                        sticky_action = action; 
                        break;
                        case Score:
                        commandRunning = ActionsSet.prepare_high_pos;
                        action = scores_map.get(level);
                        commandsAwait.add(action.getActionCommand());
                        sticky_action = action;
                        break;
                    }
                }
                if (commandRunning != null){
                    commandRunning.schedule();
                }
            }
        );
    }

    public CommandBase requestCancelAction (){
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
        }).withPosition(6, 0);
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
        }).withPosition(7, 0);
    }
}
