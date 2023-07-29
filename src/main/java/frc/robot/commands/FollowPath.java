package frc.robot.commands;

import java.util.ArrayList;
import java.util.List;

import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPlannerTrajectory.PathPlannerState;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.Trajectory.State;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.SwerveMode;
import frc.robot.subsystems.Drive;

public class FollowPath extends CommandBase {
  private Drive drive = Drive.getInstance();
  private PathPlannerTrajectory trajectory;
  private HolonomicDriveController controller = new HolonomicDriveController(
    new PIDController(0, 0, 0), 
    new PIDController(0, 0, 0), 
    new ProfiledPIDController(0, 0, 0, new Constraints (0,0)));
  private List<State> states = new ArrayList<State>();
  private boolean isFirstPath;
  private int i;
  private double startTime, currentTime;
  public FollowPath(PathPlannerTrajectory trajectory, boolean isFirstPath) {
    this.trajectory = trajectory;
    this.isFirstPath = isFirstPath;
    addRequirements(drive); 
  }

  @Override
  public void initialize() {
    if (isFirstPath){
      drive.resetChassisPosition(trajectory.getInitialPose());
      drive.setCurrentPose(trajectory.getInitialHolonomicPose());
    }
    drive.setMode(SwerveMode.OpenLoop);
    states = trajectory.getStates();
    i = 0;
    startTime = Timer.getFPGATimestamp();
  }

  @Override
  public void execute() {
    currentTime = Timer.getFPGATimestamp() - startTime;
    PathPlannerState state = trajectory.getState(i);
    double lagTime = state.timeSeconds - currentTime;
    drive.setDesiredChassisSpeeds(
      controller.calculate(drive.getCurrentPose(), state, state.holonomicRotation)
    );
    i++;
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return i > states.size()? true : false;
  }
}
