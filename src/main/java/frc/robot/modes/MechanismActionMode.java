package frc.robot.modes;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Telemetry;
import frc.robot.Constants.MechanismMode;

public class MechanismActionMode extends SubsystemBase {
    private static MechanismActionMode mechanismMode = null;
    private static MechanismMode activeMode;
    private boolean notifier = false;
    private String value = "";

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

    public CommandBase commandSetMode (MechanismMode mode){
        return runOnce(
            () -> {
                notifier = true;
                activeMode = mode; 
            }
        );
    }

    public CommandBase toggleDriverMode (){
        return runOnce(
            () -> {
                notifier = true;
                if (activeMode == MechanismMode.PickUp){
                    activeMode = MechanismMode.Score;
                }
                else {
                    activeMode = MechanismMode.PickUp;
                }
            }
        );
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
        //Telemetry.tabDriver.addString("Mechanism Mode", () -> {return value;}).withSize(1, 1).withPosition(4, 3);
    }
}
