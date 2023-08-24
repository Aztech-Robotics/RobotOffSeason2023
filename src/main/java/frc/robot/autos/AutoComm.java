package frc.robot.autos;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.interfaces.AutoInterface;
import frc.robot.subsystems.Drive;

public class AutoComm implements AutoInterface {
    private final Drive drive = Drive.getInstance();
    private final PathPlannerTrajectory traj1, traj2, traj3;

    public AutoComm (){
        traj1 = PathPlanner.loadPath("AutoDPath1", new PathConstraints(3, 3));
        traj2 = PathPlanner.loadPath("AutoDPath2", new PathConstraints(4, 3));
        traj3 = PathPlanner.loadPath("PathOutOfComm", new PathConstraints(4, 3));
    }

    @Override
    public Command getAutoCommand (){
        return new SequentialCommandGroup(
            drive.getPathFollowingCommand(traj1),
            drive.getPathFollowingCommand(traj2),
            drive.getPathFollowingCommand(traj3)
        );
    }

    @Override
    public Pose2d getStartingPose (){
        return traj1.getInitialHolonomicPose();
    }
}
