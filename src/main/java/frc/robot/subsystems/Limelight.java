package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.Constants.TypePipeline;
import frc.robot.utils.LimelightHelpers;

public class Limelight {
  private static Limelight limelight;
  private TypePipeline activePipeline;

  private Limelight() {}

  public static Limelight getInstance (){
    if (limelight == null){
      limelight = new Limelight();
    }
    return limelight;
  }

  public String getURL (){
    return LimelightHelpers.getLimelightURLString("limelight", "").getPath();
  }

  public double getTagID (){
    return LimelightHelpers.getFiducialID(null);
  }

  public boolean sawTag (){
    return getTagID() != -1? true : false; 
  }

  public Pose2d getBotPoseBlueAlliance (){
    return LimelightHelpers.getBotPose2d_wpiBlue(null);
  }

  public Pose2d getBotPoseRedAlliance (){
    return LimelightHelpers.getBotPose2d_wpiRed(null);
  }

  public double getX (){
    return LimelightHelpers.getTX(null);
  }

  public double getY (){
    return LimelightHelpers.getTY(null);
  }

  public double getLatencyPipeline (){
    return LimelightHelpers.getLatency_Pipeline(null);
  }

  public double getLatencyCapture (){
    return LimelightHelpers.getLatency_Capture(null);
  }

  public void setPipeline(TypePipeline pipeline){
    activePipeline = pipeline;
    switch (pipeline){
      case RetroflectiveTape:
      LimelightHelpers.setPipelineIndex(null, 0);
      break;
      case GridsAprilTags:
      LimelightHelpers.setPipelineIndex(null, 1);
      break;
      case LeftDSubAprilTag:
      LimelightHelpers.setPipelineIndex(null, 2);
      break;
      case RightDSubAprilTag:
      LimelightHelpers.setPipelineIndex(null, 3);
      break;
      case GameElementDetection:
      LimelightHelpers.setPipelineIndex(null, 4);
      break;
    }
  }

  public TypePipeline getActivePipeline (){
    return activePipeline;
  }
}