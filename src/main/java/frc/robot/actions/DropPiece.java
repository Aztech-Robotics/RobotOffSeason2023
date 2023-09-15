package frc.robot.actions;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.IntakeVel;
import frc.robot.interfaces.ActionClass;
import frc.robot.interfaces.ActionInterface;

public class DropPiece extends ActionClass implements ActionInterface {
    public DropPiece (){
    }

    @Override
    public Command getActionCommand (){
        return super.selectActionCommand(this); 
    }

    @Override
    public Command actionForCone (){
        return null;
    }

    @Override
    public Command actionForCube (){
        return null;
    }
}
