package frc.robot.actions;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.interfaces.ActionClass;
import frc.robot.interfaces.ActionInterface;

public class PickUpSingle extends ActionClass implements ActionInterface {
    public PickUpSingle (){
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
