package frc.robot.actions;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.WristPosition;
import frc.robot.interfaces.ActionClass;
import frc.robot.interfaces.ActionInterface;

public class SaveMechanism extends ActionClass implements ActionInterface {
    private final Command save_mech;
    public SaveMechanism (){
        save_mech = new WristPosition(0); 
    }

    @Override
    public Command getActionCommand (){
        return save_mech;
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
