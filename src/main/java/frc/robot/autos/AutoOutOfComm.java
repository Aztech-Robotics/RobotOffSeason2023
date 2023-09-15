package frc.robot.autos;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.commands.FollowPath;
import frc.robot.interfaces.AutoInterface;
import frc.robot.utils.AutoTrajectoryReader;

public class AutoOutOfComm implements AutoInterface {
    private final Trajectory traj;
    private final FollowPath traj_com;

    public AutoOutOfComm (){
        TrajectoryConfig config_test = Constants.getTrajConfig(1, 1, 0, 0);
        traj = AutoTrajectoryReader.generateTrajectoryFromFile(Constants.pathOutOfComm, config_test);
        traj_com = new FollowPath(traj, Rotation2d.fromDegrees(0));
    }

    @Override
    public Command getAutoCommand (){
        return traj_com;
    }

    @Override
    public Pose2d getStartingPose (){
        return new Pose2d(
            traj.getInitialPose().getTranslation(), 
            Rotation2d.fromDegrees(0)
        );
    }
}
