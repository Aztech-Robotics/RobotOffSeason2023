package frc.robot.actions;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.interfaces.ActionClass;
import frc.robot.interfaces.ActionInterface;

public class ManualMode extends ActionClass implements ActionInterface {
    public ManualMode (){
    }

    @Override
    public Command getActionCommand (){
        return null;
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
