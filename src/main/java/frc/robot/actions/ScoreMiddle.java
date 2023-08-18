package frc.robot.actions;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.TestCommand;
import frc.robot.interfaces.ActionClass;
import frc.robot.interfaces.ActionInterface;

public class ScoreMiddle extends ActionClass implements ActionInterface {
    public ScoreMiddle (){
    }

    @Override
    public Command getActionCommand (){
        return super.selectActionCommand(this);
    }

    @Override
    public Command actionForCone (){
        TestCommand command = new TestCommand("Score Middle For Cone", 5);
        return command;
    }

    @Override
    public Command actionForCube (){
        TestCommand command = new TestCommand("Score Middle For Cube", 5);
        return command;
    }
}
