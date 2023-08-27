package frc.robot;

import java.util.Map;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.math.util.Units;

public final class Constants {
  //ENUMS
  public static enum SwerveMode {
    OpenLoop,
    Nothing
  }
  public static enum SwerveSubMode {
    Trajectory,
    AutoBalance,
    Nothing
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
    RetroflectiveTape,
    BlueTags,
    RedTags 
  }
  
  public static enum AutoBalanceStages {
    BreakTheResistance, 
    DriveEstimatedDistance,
    MaintainBalance 
  }

  //DriveTrain
  public static final double trackWidth = Units.inchesToMeters(25);
  public static final double wheelBase = Units.inchesToMeters(25);
  public static final double wheelDiameter = 0.1;
  public static final double max_rpm_neo = 5676;
  public static final double drive_gear_ratio = 6.75;
  public static final double steer_gear_ratio = 12.8;
  
  public static final double maxDriveVel = 4.4; 
  public static final double maxAngVel = Math.PI;   

  public static final double drivePositionCoefficient = (Math.PI * wheelDiameter) / drive_gear_ratio;
  public static final double driveVelocityCoefficient = drivePositionCoefficient / 60;
  public static final double steerPositionCoefficient = (Math.PI * 2) / steer_gear_ratio;
  public static final double steerVelocityCoefficient = steerPositionCoefficient / 60; 

  //SwerveModules 
  public static final int id_speed_fLeft = 1;
  public static final int id_steer_fLeft = 2;
  public static final int id_canCoder_fLeft = 3;
  public static final Rotation2d offset_fLeft = Rotation2d.fromDegrees(240);

  public static final int id_speed_fRight = 4;
  public static final int id_steer_fRight = 5;
  public static final int id_canCoder_fRight = 6;
  public static final Rotation2d offset_fRight = Rotation2d.fromDegrees(230);

  public static final int id_speed_bLeft = 7;
  public static final int id_steer_bLeft = 8;
  public static final int id_canCoder_bLeft = 9;
  public static final Rotation2d offset_bLeft = Rotation2d.fromDegrees(5.8);

  public static final int id_speed_bRight = 10;
  public static final int id_steer_bRight = 11;
  public static final int id_canCoder_bRight = 12;
  public static final Rotation2d offset_bRight = Rotation2d.fromDegrees(94); 

  public static final double kp_steerController = 0.8;
  public static final double ki_steerController = 0.0;
  public static final double kd_steerController = 1;
  public static final double kf_steerController = 0.0;
  public static final double kIz_steerController = 0.0;

  public static final double kp_speedController = 0.008;
  public static final double ki_speedController = 0;
  public static final double kd_speedController = 0;
  public static final double kf_speedController = 0;
  public static final double kIz_speedController = 0;
  
  //Arm 
  public static final int id_arm_master = 13;
  public static final int id_arm_sleeve = 14;
  public static final double arm_ratio = 137.5;
  public static final double kp_arm = 0;
  public static final double ki_arm = 0;
  public static final double kd_arm = 0;
  public static final double ks_arm = 0;
  public static final double kv_arm = 0;
  public static final double ff_arm = 0;

  //Telescopic
  public static final int id_tel_master = 15;
  public static final int id_tel_sleeve = 16;

  //Wrist
  public static final int id_wrist = 17;

  //Intake
  public static final int id_intake = 18;
  
  public static final CentripetalAccelerationConstraint cent_accel_traj = new CentripetalAccelerationConstraint(0);
  //Trajectories Paths
  public static final String autoDPath1 = "paths/AutoDPath1.path";
  public static final String autoDPath2 = "paths/AutoDPath2.path";
  public static final String pathOutOfComm = "paths/PathOutOfComm.path";
  public static final String pathToCS = "paths/PathToCS.path";


  public static final double kp_x = 1;
  public static final double ki_x= 0.0;
  public static final double kd_x = 0.0;

  public static final double kp_y = 1;
  public static final double ki_y = 0.0;
  public static final double kd_y = 0.0;
  
  public static final double kp_rot = 0.095;
  public static final double ki_rot = 0.0;
  public static final double kd_rot = 0.0;
  public static final Constraints rot_constraints = new Constraints(maxAngVel, Math.pow(maxAngVel, 2));

  public static TrajectoryConfig getTrajConfig (double maxVel, double maxAccel, double startVel, double endVel){
    TrajectoryConfig config = new TrajectoryConfig(maxVel, maxAccel);
    config.setStartVelocity(startVel);
    config.setEndVelocity(endVel);
    //config.addConstraint(new CentripetalAccelerationConstraint(Math.PI)); 
    return config;
  }

  public static final double kp_autoB = 0.0;
  public static final double ki_autoB = 0.0;
  public static final double kd_autoB = 0.0;

  public static final double kp_chase = 0.0;
  public static final double ki_chase = 0.0;
  public static final double kd_chase = 0.0;
  public static final Constraints chase_constraints = new Constraints(0, 0); 
  

  public static class FieldLayout {
    public static final double kFieldLength = Units.inchesToMeters(651.25);
    public static final double kFieldWidth = Units.inchesToMeters(315.5);
    public static final double kTapeWidth = Units.inchesToMeters(2.0);
  
    public static class blueGridPoses {
      public static final Map<Integer, Pose2d> map = Map.of(
        1, new Pose2d(0, 0, null),
        2, new Pose2d(0, 0, null),
        3, new Pose2d(0, 0, null),
        4, new Pose2d(0, 0, null),
        5, new Pose2d(0, 0, null), 
        6, new Pose2d(0, 0, null),
        7, new Pose2d(0, 0, null),
        8, new Pose2d(0, 0, null),
        9, new Pose2d(0, 0, null)
      );
  
      public static Pose2d getPose (int node){
        return map.get(node);
      }
    }
  
    public static class blueStationPoses {
      public static final Map<Integer, Pose2d> map = Map.of(
        1, new Pose2d(0, 0, null),
        2, new Pose2d(0, 0, null),
        3, new Pose2d(0, 0, null)
      );
  
      public static Pose2d getPose (int station){
        return map.get(station);
      }
    }
  
    public static class redGridPoses {
      public static final Map<Integer, Pose2d> map = Map.of(
        1, new Pose2d(0, 0, null),
        2, new Pose2d(0, 0, null),
        3, new Pose2d(0, 0, null),
        4, new Pose2d(0, 0, null),
        5, new Pose2d(0, 0, null), 
        6, new Pose2d(0, 0, null),
        7, new Pose2d(0, 0, null),
        8, new Pose2d(0, 0, null),
        9, new Pose2d(0, 0, null)
      );
  
      public static Pose2d getPose (int node){
        return map.get(node);
      }
    }
  
    public static class redStationPoses {
      public static final Map<Integer, Pose2d> map = Map.of(
        1, new Pose2d(0, 0, null),
        2, new Pose2d(0, 0, null),
        3, new Pose2d(0, 0, null)
      );
  
      public static Pose2d getPose (int station){
        return map.get(station);
      }
    }
  }
}
