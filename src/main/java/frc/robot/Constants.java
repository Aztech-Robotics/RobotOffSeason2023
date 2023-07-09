package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

public final class Constants {
  public static final double trackWidth = Units.inchesToMeters(20);
  public static final double wheelBase = Units.inchesToMeters(20);
  public static final double wheelDiameter = 0.1;
  public static final double max_rpm_neo = 5676;
  public static final double max_rpm_talon = 6380;
  public static final double drive_gear_ratio = 6.75;
  public static final double steer_gear_ratio = 12.8;
  
  public static final double maxDriveVel = ((max_rpm_neo / 60) / drive_gear_ratio) * Math.PI * wheelDiameter; 
  public static final double maxAngVel = ((max_rpm_talon / 60) / steer_gear_ratio) * Math.PI * 2; 

  public static final double drivePositionCoefficient = (Math.PI * wheelDiameter) / drive_gear_ratio;
  public static final double driveVelocityCoefficient = drivePositionCoefficient / 60;
  public static final double steerPositionCoefficient = (Math.PI * 2) / steer_gear_ratio;
  public static final double steerVelocityCoefficient = steerPositionCoefficient / 60; 

  public static final int id_steer_fLeft = 1;
  public static final int id_canCoder_fLeft = 2;
  public static final int id_drive1_fLeft = 3;
  public static final int id_drive2_fLeft = 4;
  public static final Rotation2d offset_fLeft = Rotation2d.fromDegrees(0);

  public static final int id_steer_fRight = 5;
  public static final int id_canCoder_fRight = 6;
  public static final int id_drive1_fRight = 7;
  public static final int id_drive2_fRight = 8;
  public static final Rotation2d offset_fRight = Rotation2d.fromDegrees(0);

  public static final int id_steer_bLeft = 9;
  public static final int id_canCoder_bLeft = 10;
  public static final int id_drive1_bLeft = 11;
  public static final int id_drive2_bLeft = 12;
  public static final Rotation2d offset_bLeft = Rotation2d.fromDegrees(0);

  public static final int id_steer_bRight = 13;
  public static final int id_canCoder_bRight = 14;
  public static final int id_drive1_bRight = 15;
  public static final int id_drive2_bRight = 16;
  public static final Rotation2d offset_bRight = Rotation2d.fromDegrees(0); 

  public static final double kp_steerController = 0.0;
  public static final double ki_steerController = 0.0;
  public static final double kd_steerController = 0.0;
  public static final double kv_steerController = 0.0;
  public static final double ks_steerController = 0.0;

  public static final double kp_speedController = 0.0;
  public static final double ki_speedController = 0.0;
  public static final double kd_speedController = 0.0;
  public static final double kf_speedController = 0.0;
  public static final double kIz_speedController = 0.0;

  public static enum SwerveMode {
    Nothing,
    OpenLoopWithVoltage,
    OpenLoopWithVelocity,
    Trajectory,
    AutoBalance
  }

  public static enum GeneralModeEnum {
    Cone,
    Cube
  }

  public static enum TypePipeline {
    AprilTag,
    RetroflectiveTape
  }
}
