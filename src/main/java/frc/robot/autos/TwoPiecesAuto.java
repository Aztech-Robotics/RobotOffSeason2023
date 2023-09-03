package frc.robot.autos;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.commands.FollowPath;
import frc.robot.interfaces.AutoInterface;
import frc.robot.utils.AutoTrajectoryReader;

public class TwoPiecesAuto implements AutoInterface {
    private final Trajectory test_traj;
    private final FollowPath test_traj_com;

    public TwoPiecesAuto (){
        TrajectoryConfig config_test = Constants.getTrajConfig(1, 1, 0, 0);
        test_traj = AutoTrajectoryReader.generateTrajectoryFromFile(Constants.autoDPath1, config_test);
        test_traj_com = new FollowPath(test_traj, Rotation2d.fromDegrees(0));
    }

    @Override
    public Command getAutoCommand (){
        return test_traj_com;
    }

    @Override
    public Pose2d getStartingPose (){
        return new Pose2d(
            test_traj.getInitialPose().getTranslation(), 
            Rotation2d.fromDegrees(0)
        );
    }
}
