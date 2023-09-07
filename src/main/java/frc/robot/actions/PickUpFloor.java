package frc.robot.actions;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.WristPosition;
import frc.robot.interfaces.ActionClass;
import frc.robot.interfaces.ActionInterface;

public class PickUpFloor extends ActionClass implements ActionInterface{
    public PickUpFloor (){}

    @Override
    public Command getActionCommand (){
        return super.selectActionCommand(this);
    }

    @Override
    public Command actionForCone (){
        return new WristPosition(-150);
    }

    @Override
    public Command actionForCube (){
        return new WristPosition(-100);
    }
}
