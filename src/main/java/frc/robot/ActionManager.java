package frc.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MechanismMode;
import frc.robot.commands.FieldOrientedDrive;
import frc.robot.interfaces.ActionInterface;
import frc.robot.modes.MechanismActionMode;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;

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
        scores_map = Map.of(1, ActionsSet.score_bottom, 2, ActionsSet.score_middle);
        stations_map = Map.of(1, ActionsSet.pickup_floor, 2, ActionsSet.pickup_single);
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
                switch (mechanismActionMode.getMode()){
                    case ManualMode:
                    commandRunning = ActionsSet.manual_mode;
                    break;
                    case SaveMechanism:
                    commandRunning = ActionsSet.save_mechanism.getActionCommand();
                    break;
                    case PickUp:
                    ActionInterface action = stations_map.get(station);
                    commandRunning = action.getActionCommand();
                    break;
                    case Score:
                    action = scores_map.get(level);
                    commandRunning = action.getActionCommand(); 
                    break;
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
                if (mechanismActionMode.getMode() == MechanismMode.Score && level + units == 1 || level + units == 2){
                    level = level + units;
                }
            }
        );
    }

    public CommandBase moveStation (int units){
        return runOnce(
            () -> {
                if (mechanismActionMode.getMode() == MechanismMode.PickUp && station + units == 1 || station + units == 2){
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
            }
            return value;
        }).withPosition(7, 0);
    }
}
