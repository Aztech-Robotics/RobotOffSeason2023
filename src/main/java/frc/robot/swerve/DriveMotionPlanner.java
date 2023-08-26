package frc.robot.swerve;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Telemetry;

public class DriveMotionPlanner {
    private final PIDController y_controller;
    private final PIDController x_controller;
    private final ProfiledPIDController rotation_controller;
    private final HolonomicDriveController drive_controller;
    private Trajectory current_trajectory;
    private Rotation2d target_rotation;
    private Double start_time;
    private boolean isTrajectoryFinished = false;

    public DriveMotionPlanner (){
        x_controller = new PIDController(Constants.kp_x, Constants.ki_x, Constants.kd_x);
        y_controller = new PIDController(Constants.kp_y, Constants.ki_y, Constants.kd_y);
        rotation_controller = new ProfiledPIDController(Constants.kp_rot, Constants.ki_rot, Constants.kd_rot, Constants.rot_constraints);
        rotation_controller.enableContinuousInput(0, 2 * Math.PI);
        rotation_controller.setTolerance(0.15);
        drive_controller = new HolonomicDriveController(y_controller, x_controller, rotation_controller);
        Telemetry.swerveTab.addBoolean("TrajectoryIsFinished", () -> isTrajectoryFinished); 
    }

    public void setTrajectory (Trajectory trajectory, Rotation2d heading, Pose2d current_pose){
        current_trajectory = trajectory;
        isTrajectoryFinished = false; 
        setTargetRotation(heading);
        start_time = Double.NaN;
        x_controller.reset();
        y_controller.reset();
        rotation_controller.reset(current_pose.getRotation().getRadians());
    }

    public void setTargetRotation (Rotation2d target_rotation){
        this.target_rotation = target_rotation;
    }

    public ChassisSpeeds update (Pose2d current_pose, double current_time){
        ChassisSpeeds desired_ChassisSpeeds = null;
        if (current_trajectory != null){
            if (start_time.isNaN()){
                start_time = Timer.getFPGATimestamp();
            }
            double seconds = current_time - start_time;
            SmartDashboard.putNumber("TrajTime", seconds);
            SmartDashboard.putNumber("Running Traj of ", current_trajectory.getTotalTimeSeconds());
            Trajectory.State desired_state;
            if (seconds < current_trajectory.getTotalTimeSeconds()){
                desired_state = current_trajectory.sample(seconds);
                desired_ChassisSpeeds = drive_controller.calculate(current_pose, desired_state, target_rotation);
            } else {
                isTrajectoryFinished = true;
                current_trajectory = null;
                desired_ChassisSpeeds = new ChassisSpeeds();
            }
        } 
        return desired_ChassisSpeeds;
    }

    public Translation2d getTranslationalError (Pose2d current_pose, double current_time){
        if (current_trajectory != null && !start_time.isNaN()){
            return current_pose.relativeTo(current_trajectory.sample(current_time - start_time).poseMeters).getTranslation();
        }
        else {
            return new Translation2d();
        }
    }

    public Rotation2d getRotationalError (Rotation2d current_rotation){
        if (target_rotation != null){
            return target_rotation.minus(current_rotation);
        } else {
            return new Rotation2d();
        }
    }

    public boolean isTrajectoryFinished() {
        return isTrajectoryFinished;
    }
}
