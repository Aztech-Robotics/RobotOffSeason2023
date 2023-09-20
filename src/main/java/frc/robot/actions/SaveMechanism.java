package frc.robot.actions;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.ArmPosition;
import frc.robot.commands.WristPosition;
import frc.robot.interfaces.ActionClass;
import frc.robot.interfaces.ActionInterface;

public class SaveMechanism extends ActionClass implements ActionInterface {
    private final ParallelCommandGroup action;
    public SaveMechanism (){
        action = new ParallelCommandGroup(
            new ArmPosition(Rotation2d.fromDegrees(0)),
            new WristPosition(0)
        ); 
    }

    @Override
    public Command getActionCommand (){
        return action;
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
