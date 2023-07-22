package frc.robot.modes;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Telemetry;
import frc.robot.Constants.MechanismMode;

public class MechanismActionMode {
    private static MechanismActionMode mechanismMode = null;
    private static MechanismMode activeMode;
    private boolean notifier = false;

    private MechanismActionMode (){}

    public static MechanismActionMode getInstance (){
        if (mechanismMode == null){
            mechanismMode = new MechanismActionMode();
            activeMode = MechanismMode.SaveMechanism;
        }
        return mechanismMode;
    }
    
    public MechanismMode getMode (){
        return activeMode; 
    }

    public void setMode (MechanismMode mode){
        notifier = true;
        activeMode = mode;
    }

    public InstantCommand commandSetMode (MechanismMode mode){
        InstantCommand command = new InstantCommand(
            () -> {
                notifier = true;
                activeMode = mode; 
            }
        );
        return command;
    }

    public InstantCommand toggleDriverMode (){
        notifier = true;
        InstantCommand command = new InstantCommand(
            () -> {
                if (activeMode == MechanismMode.PickUp){
                    activeMode = MechanismMode.Score;
                }
                else {
                    activeMode = MechanismMode.PickUp;
                }
            }
        );
        return command;
    }

    public boolean haveChanged (){
        if (notifier){
            notifier = false;
            return true;
        }
        else {
            return false;
        }
    }

    public void outputTelemetry (){
        String value = "";
        switch (activeMode){
            case ManualMode:
            value = "ManualMode";
            break;
            case SaveMechanism:
            value = "SaveMechanism";
            break;
            case PickUp:
            value = "PickUp";
            break;
            case Score:
            value = "Score";
            break;
        }
        Telemetry.tabDriver.add("Mechanism Mode", value).withSize(1, 1).withPosition(4, 3);
    }
}
