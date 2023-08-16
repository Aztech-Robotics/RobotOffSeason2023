package frc.robot.interfaces;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;

public interface AutoInterface {
    public Command getAutoCommand (); 
    public Pose2d getStartingPose ();
}
