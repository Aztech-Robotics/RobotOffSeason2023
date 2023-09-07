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
import frc.robot.commands.IntakeVel;
import frc.robot.interfaces.AutoInterface;
import frc.robot.utils.AutoTrajectoryReader;

public class AutoBalance implements AutoInterface {
    private final IntakeVel leavePiece;
    private final Trajectory traj;
    private final FollowPath traj_com;
    private final AutoBalance auto_balance;

    public AutoBalance (){
        leavePiece = new IntakeVel(0.5);
        TrajectoryConfig config_test = Constants.getTrajConfig(2, 1.5, 0, 0);
        traj = AutoTrajectoryReader.generateTrajectoryFromFile(Constants.pathToCS, config_test);
        traj_com = new FollowPath(traj, Rotation2d.fromDegrees(0));
        auto_balance = new AutoBalance(); 
    }

    @Override
    public Command getAutoCommand (){
        return new SequentialCommandGroup(
            leavePiece.withTimeout(1),
            traj_com
            //auto_balance
        );
    }

    @Override
    public Pose2d getStartingPose (){
        return new Pose2d(
            traj.getInitialPose().getTranslation(), 
            Rotation2d.fromDegrees(0)
        );
    }
}
