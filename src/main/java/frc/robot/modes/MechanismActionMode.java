package frc.robot.modes;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Telemetry;
import frc.robot.Constants.MechanismMode;

public class MechanismActionMode extends SubsystemBase {
    private static MechanismActionMode mechanismMode = null;
    private static MechanismMode activeMode = MechanismMode.ManualMode;
    private boolean notifier = false;

    private MechanismActionMode (){
        outputTelemetry();
    }

    public static MechanismActionMode getInstance (){
        if (mechanismMode == null){
            mechanismMode = new MechanismActionMode();
            activeMode = MechanismMode.ManualMode;
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

    public String getValue (){
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
        return value;
    }

    public void outputTelemetry (){
        Telemetry.driverTab.addString("Mechanism Mode", () -> {return getValue();}).withPosition(5, 0);
    }
}
