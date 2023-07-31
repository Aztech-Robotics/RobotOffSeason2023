package frc.robot.modes;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Telemetry;
import frc.robot.Constants.GameElement;

public class GamePieceMode {
    private static GamePieceMode generalMode = null;
    private static GameElement activeMode;
    private boolean notifier = false;

    private GamePieceMode (){}

    public static GamePieceMode getInstance (){
        if (generalMode == null){
            generalMode = new GamePieceMode();
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

    public InstantCommand commandSetMode (GameElement mode){
        InstantCommand command = new InstantCommand(
            () -> {
                notifier = true;
                activeMode = mode; 
            }
        );
        return command;
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

    public void outputTelemetry (){
        Telemetry.tabDriver.addBoolean("GamePiece", 
        () -> {
            boolean value = false;
            if (GamePieceMode.getInstance().getMode() == GameElement.Cone){
                value = true;
            }
            return value;
        }).withSize(1, 1).withPosition(4, 3);
    }
}
