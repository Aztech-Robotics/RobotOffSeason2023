package frc.robot.modes;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants.GameElement;

public class GameElementMode {
    private static GameElementMode generalMode = null;
    private static GameElement activeMode;
    private boolean notifier = false;

    private GameElementMode (){}

    public static GameElementMode getInstance (){
        if (generalMode == null){
            generalMode = new GameElementMode();
            activeMode = GameElement.Cone;
        }
        return generalMode;
    }
    
    public GameElement getMode (){
        return activeMode; 
    }
    public void setMode (GameElement mode){
        notifier = true;
        activeMode = mode;
    }

    public InstantCommand toggleMode (){
        notifier = true;
        InstantCommand command = new InstantCommand(
            () -> {
                if (activeMode == GameElement.Cone){
                    activeMode = GameElement.Cube;
                }
                else {
                    activeMode = GameElement.Cone;
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
}
