package frc.robot.autos;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.commands.FollowPath;
import frc.robot.interfaces.AutoInterface;
import frc.robot.utils.AutoTrajectoryReader;

public class TestPW implements AutoInterface {
    private final Trajectory test_traj;
    private final FollowPath test_traj_com;

    public TestPW (){
        TrajectoryConfig config_test = Constants.getTrajConfig(4.2, 4, 0, 0);
        test_traj = AutoTrajectoryReader.generateTrajectoryFromFile(Constants.pathTest, config_test);
        test_traj_com = new FollowPath(test_traj, Robot.flip_alliance() ? Rotation2d.fromDegrees(180) : Rotation2d.fromDegrees(0));
    }

    @Override
    public Command getAutoCommand (){
        return new SequentialCommandGroup(
            test_traj_com
        );
    }

    @Override
    public Pose2d getStartingPose (){
        return new Pose2d(
            test_traj.getInitialPose().getTranslation(), 
            Robot.flip_alliance() ? Rotation2d.fromDegrees(0) : Rotation2d.fromDegrees(180)
        );
    }
}
