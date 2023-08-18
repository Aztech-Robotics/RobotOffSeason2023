package frc.robot.actions;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.TestCommand;
import frc.robot.interfaces.ActionClass;
import frc.robot.interfaces.ActionInterface;

public class PickUpFloor extends ActionClass implements ActionInterface{
    public PickUpFloor (){
    }

    @Override
    public Command getActionCommand (){
        return super.selectActionCommand(this);
    }

    @Override
    public Command actionForCone (){
        TestCommand command = new TestCommand("PickUp Floor For Cone", 5);
        return command;
    }

    @Override
    public Command actionForCube (){
        TestCommand command = new TestCommand("PickUp Floor For Cube", 5);
        return command;
    }
}
