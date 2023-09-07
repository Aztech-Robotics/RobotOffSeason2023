package frc.robot.actions;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.IntakeVel;
import frc.robot.interfaces.ActionClass;
import frc.robot.interfaces.ActionInterface;

public class TakePiece extends ActionClass implements ActionInterface {
    public TakePiece (){
    }

    @Override
    public Command getActionCommand (){
        return super.selectActionCommand(this);
    }

    @Override
    public Command actionForCone (){
        return new IntakeVel(0.5);
    }

    @Override
    public Command actionForCube (){
        return new IntakeVel(-0.5);
    }
}
