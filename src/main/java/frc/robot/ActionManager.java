package frc.robot;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.actions.TrajectoryOnTheFly;
import frc.robot.actions.DropPiece;
import frc.robot.actions.ManualMode;
import frc.robot.actions.PickUpDouble;
import frc.robot.actions.PickUpFloor;
import frc.robot.actions.PickUpSingle;
import frc.robot.actions.SaveMechanism;
import frc.robot.actions.ScoreBottom;
import frc.robot.actions.ScoreMiddle;
import frc.robot.actions.ScoreTop;
import frc.robot.actions.TakePiece;
import frc.robot.interfaces.ActionInterface;
import frc.robot.modes.MechanismActionMode;
import frc.robot.subsystems.Drive;

public class ActionManager {
    private static ActionManager actionManager;
    private Drive drive = Drive.getInstance();
    private ActionInterface sticky_action;
    private MechanismActionMode mechanismActionMode = MechanismActionMode.getInstance();
    private Telemetry telemetry = Telemetry.getInstance();
    private ManualMode manualMode;
    private SaveMechanism saveMechanism;
    private PickUpDouble pickUpDouble;
    private PickUpFloor pickUpFloor;
    private PickUpSingle pickUpSingle;
    private ScoreBottom scoreBottom;
    private ScoreMiddle scoreMiddle;
    private ScoreTop scoreTop;
    private TrajectoryOnTheFly trajectoryOnTheFly;
    private TakePiece takePiece;
    private DropPiece dropPiece; 
    private boolean isCommandAwait = false;
    private List<Command> commandsAwait = new ArrayList<>();

    private ActionManager (){
        trajectoryOnTheFly = new TrajectoryOnTheFly();
        manualMode = new ManualMode();
        saveMechanism = new SaveMechanism();
        pickUpFloor = new PickUpFloor();
        pickUpSingle = new PickUpSingle();
        pickUpDouble = new PickUpDouble();
        scoreBottom = new ScoreBottom();
        scoreMiddle = new ScoreMiddle();
        scoreTop = new ScoreTop();
        sticky_action = saveMechanism;
    }

    public static ActionManager getInstance (){
        if (actionManager == null){
            actionManager = new ActionManager();
        }
        return actionManager;
    }

    public Command requestAction (){
        Command action = null;
        Command driveCommand = null;
        Command mechanismCommand = null;
        ActionInterface mechanismAction = null;
        if (isCommandAwait){
            action = commandsAwait.get(0);
            commandsAwait.remove(0);
            if (commandsAwait.size() == 0){
                isCommandAwait = false;
            }
        } 
        else {
            switch (mechanismActionMode.getMode()){
                case ManualMode:
                mechanismAction = manualMode;
                action = manualMode.getActionCommand();
                break;
                case SaveMechanism:
                mechanismAction = saveMechanism;
                action = saveMechanism.getActionCommand();
                break;
                case PickUp:
                int station = telemetry.getActiveStation();
                if (drive.isTagSearchActive() && drive.isReadyForCorrectionPose() && station != 0){
                    driveCommand = trajectoryOnTheFly.getStationActionCommand(station);
                    drive.toggleTagSearch();
                }
                switch (station){
                    case 0:
                    mechanismAction = pickUpFloor; 
                    break;
                    case 1:
                    mechanismAction = pickUpSingle;
                    break;
                    default:
                    mechanismAction = pickUpDouble;
                    break;
                }
                mechanismCommand = new SequentialCommandGroup(mechanismAction.getActionCommand(), takePiece.getActionCommand());
                if (driveCommand != null){
                    action = new ParallelCommandGroup(mechanismCommand, new SequentialCommandGroup(new WaitCommand(2), driveCommand)); 
                } else {
                    action = mechanismCommand;
                }
                break;
                case Score:
                int level = telemetry.getNodeLevel();
                int node = telemetry.getNode();
                if (drive.isTagSearchActive() && drive.isReadyForCorrectionPose()){
                    driveCommand = trajectoryOnTheFly.getGridActionCommand(node);
                    drive.toggleTagSearch();
                }
                switch (level){
                    case 3:
                    mechanismAction = scoreTop; 
                    break;
                    case 2:
                    mechanismAction = scoreMiddle;
                    break;
                    default:
                    mechanismAction = scoreBottom; 
                    break;
                }
                mechanismCommand = mechanismAction.getActionCommand();
                isCommandAwait = true;
                if (driveCommand != null){
                    action = driveCommand;
                    commandsAwait.add(mechanismCommand);
                } else {
                    action = mechanismCommand;
                }
                commandsAwait.add(dropPiece.getActionCommand());
                break; 
            }
            sticky_action = mechanismAction;
        }
        return action;
    }

    public Command requestStickyAction (){
        if (sticky_action != null){
            return sticky_action.getActionCommand();
        } else {
            return new InstantCommand();
        }
    }

    public Command requestPISearching (){
        return new InstantCommand(() -> {drive.toggleTagSearch();});
    }

}
