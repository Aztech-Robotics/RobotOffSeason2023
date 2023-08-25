package frc.robot.autos;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.interfaces.AutoInterface;
import frc.robot.subsystems.Drive;

public class TestPP implements AutoInterface {
    private final Drive drive = Drive.getInstance();
    private final PathPlannerTrajectory traj1;

    public TestPP (){
        traj1 = PathPlanner.loadPath("TestTraj", new PathConstraints(3, 3));
    }

    @Override
    public Command getAutoCommand (){
        return new SequentialCommandGroup(
            drive.getPathFollowingCommand(traj1)
        );
    }

    @Override
    public Pose2d getStartingPose (){
        return traj1.getInitialHolonomicPose();
    }
}
