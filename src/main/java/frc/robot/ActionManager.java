package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.actions.ScoreTop;
import frc.robot.interfaces.ActionInterface;
import frc.robot.modes.MechanismActionMode;

public class ActionManager {
    private static ActionManager actionManager;
    private ActionInterface sticky_action;
    private MechanismActionMode mechanismActionMode = MechanismActionMode.getInstance();
    private ScoreTop scoreTop = new ScoreTop();
    private ActionManager (){}

    public static ActionManager getInstance (){
        if (actionManager == null){
            actionManager = new ActionManager();
        }
        return actionManager;
    }

    public Command requestMechanismAction (){
        Command a;
        switch (mechanismActionMode.getMode()){
            case ManualMode:
            break;
            case SaveMechanism:
            break;
            case PickUp:
            break;
            case Score:
            break; 
            default:
            break;
        }
        return null;

    }

    public Command requestStickyAction (){
        if (sticky_action != null){
            return sticky_action.getActionCommand();
        } else {
            return new InstantCommand();
        }
    }

    public void stopMechanismAction (){}

    public void requestPISearching (){}
}
