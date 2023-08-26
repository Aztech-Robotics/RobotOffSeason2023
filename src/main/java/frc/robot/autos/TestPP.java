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
    private final PathPlannerTrajectory test_traj;
    private final Command test_traj_comm;

    public TestPP (){
        test_traj = PathPlanner.loadPath("AutoDPath1", new PathConstraints(2, 1));
        test_traj_comm = drive.getPathFollowingCommand(test_traj);
    }

    @Override
    public Command getAutoCommand (){
        return test_traj_comm;
    }

    @Override
    public Pose2d getStartingPose (){
        return test_traj.getInitialHolonomicPose();
    }
}
