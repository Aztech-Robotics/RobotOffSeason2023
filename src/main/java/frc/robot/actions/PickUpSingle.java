package frc.robot.actions;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.ArmPosition;
import frc.robot.interfaces.ActionClass;
import frc.robot.interfaces.ActionInterface;

public class PickUpSingle extends ActionClass implements ActionInterface {
    private final ParallelCommandGroup actionCone, actionCube;
    public PickUpSingle (){
        actionCone = new ParallelCommandGroup(
            new ArmPosition(Rotation2d.fromDegrees(50))
        );
        actionCube = new ParallelCommandGroup(
            new ArmPosition(Rotation2d.fromDegrees(40))
        );
    }

    @Override
    public Command getActionCommand (){
        return super.selectActionCommand(this);
    }

    @Override
    public Command actionForCone (){
        return actionCone;
    }

    @Override
    public Command actionForCube (){
        return actionCube;
    }
}
