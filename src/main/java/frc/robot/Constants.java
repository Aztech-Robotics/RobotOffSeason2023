package frc.robot;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

public final class Constants {
  public static final double trackWidth = Units.inchesToMeters(25);
  public static final double wheelBase = Units.inchesToMeters(25);
  public static final double wheelDiameter = 0.1;
  public static final double max_rpm_neo = 5676;
  public static final double drive_gear_ratio = 6.75;
  public static final double steer_gear_ratio = 12.8;
  
  public static final double maxDriveVel = ((max_rpm_neo / 60) / drive_gear_ratio) * Math.PI * wheelDiameter; 
  public static final double maxSteerVel = ((max_rpm_neo / 60) / steer_gear_ratio) * Math.PI * wheelDiameter; 
  public static final double maxAngVel = (maxDriveVel / (Math.PI * Math.hypot(trackWidth, wheelBase))) * 2 * Math.PI;   

  public static final double drivePositionCoefficient = (Math.PI * wheelDiameter) / drive_gear_ratio;
  public static final double driveVelocityCoefficient = drivePositionCoefficient / 60;
  public static final double steerPositionCoefficient = (Math.PI * 2) / steer_gear_ratio;
  public static final double steerVelocityCoefficient = steerPositionCoefficient / 60; 

  public static final int id_speed_fLeft = 1;
  public static final int id_steer_fLeft = 2;
  public static final int id_canCoder_fLeft = 3;
  public static final Rotation2d offset_fLeft = Rotation2d.fromDegrees(0);

  public static final int id_speed_fRight = 5;
  public static final int id_steer_fRight = 6;
  public static final int id_canCoder_fRight = 7;
  public static final Rotation2d offset_fRight = Rotation2d.fromDegrees(0);

  public static final int id_speed_bLeft = 9;
  public static final int id_steer_bLeft = 10;
  public static final int id_canCoder_bLeft = 11;
  public static final Rotation2d offset_bLeft = Rotation2d.fromDegrees(0);

  public static final int id_speed_bRight = 13;
  public static final int id_steer_bRight = 14;
  public static final int id_canCoder_bRight = 15;
  public static final Rotation2d offset_bRight = Rotation2d.fromDegrees(0); 
  
  public static final int id_arm_master = 17;
  public static final int id_arm_sleeve = 18;
  public static final double arm_reduction = 0;
  public static final double kp_arm = 0;
  public static final double ki_arm = 0;
  public static final double kd_arm = 0;
  public static final double kf_arm = 0;

  public static final int id_tel_master = 19;
  public static final int id_tel_sleeve = 20;

  public static final int id_wrist = 21;

  public static final int id_art = 22;

  public static final int id_intake = 23;

  public static final double kp_steerController = 0.8;
  public static final double ki_steerController = 0.0;
  public static final double kd_steerController = 1;
  public static final double kf_steerController = 0.0;
  public static final double kIz_steerController = 0.0;

  public static final double kp_speedController = 0.0004;
  public static final double ki_speedController = 0.00002;
  public static final double kd_speedController = 0.0;
  public static final double kf_speedController = 0.0;
  public static final double kIz_speedController = 0.0;

  public static final PathPlannerTrajectory pickUp2ndPiece = PathPlanner.loadPath("PickUp2ndPiece", 4, 3.8);
  public static final PathPlannerTrajectory drop2ndPiece = PathPlanner.loadPath("Drop2ndPiece", 4, 3.8);
  public static final PathPlannerTrajectory pickUp3rdPiece = PathPlanner.loadPath("PickUp3rdPiece", 4, 3.8);
  public static final PathPlannerTrajectory parking = PathPlanner.loadPath("Parking", 4, 3.8);


  public static enum SwerveMode {
    Nothing,
    OpenLoop,
  }

  public static enum GameElement {
    Cone,
    Cube
  }

  public static enum MechanismMode {
    PickUp,
    Score,
    SaveMechanism,
    ManualMode
  }

  public static enum TypePipeline {
    GridsAprilTags,
    RetroflectiveTape,
    LeftDSubAprilTag,
    RightDSubAprilTag
  }
}
