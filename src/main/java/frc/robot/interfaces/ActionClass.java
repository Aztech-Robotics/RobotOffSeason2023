package frc.robot.interfaces;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.GameElement;
import frc.robot.modes.GamePieceMode;

public class ActionClass {
    private GamePieceMode gameElementMode = GamePieceMode.getInstance(); 
    public ActionClass (){}

    protected Command selectActionCommand (ActionInterface action){
        if (gameElementMode.getMode() == GameElement.Cone){
            return action.actionForCone(); 
        }
        else {
            return action.actionForCube();
        }
    }
}
