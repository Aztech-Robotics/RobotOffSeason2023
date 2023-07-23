package frc.robot.interfaces;

import edu.wpi.first.wpilibj2.command.Command;

public interface ActionInterface {
    public Command getActionCommand();
    public Command actionForCone ();
    public Command actionForCube ();
}
