package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.SwerveSubMode;
import frc.robot.subsystems.Drive;

public class FollowPath extends CommandBase {
  private Drive drive = Drive.getInstance();
  private final Trajectory trajectory;
  private final Rotation2d target_rotation;
  public FollowPath(Trajectory trajectory, Rotation2d target_rotation) {
    this.trajectory = trajectory;
    this.target_rotation = target_rotation;
    addRequirements(drive); 
  }

  @Override
  public void initialize() {
    if (drive.isReadyForAuto()){
      System.out.println("Starting Trajectory of " + trajectory.getTotalTimeSeconds() + " seconds");
      drive.setTrajectory(trajectory, target_rotation);
    }
  }

  @Override
  public void end(boolean interrupted) {
    drive.setSubMode(SwerveSubMode.Nothing);
  }

  @Override
  public boolean isFinished() {
    return drive.isDoneWithTrajectory();
  }
}
