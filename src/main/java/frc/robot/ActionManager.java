package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.actions.ScoreTop;
import frc.robot.modes.MechanismActionMode;

public class ActionManager {
    private static ActionManager actionManager;
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

    public void stopMechanismAction (){}

    public void requestPISearching (){}
}
