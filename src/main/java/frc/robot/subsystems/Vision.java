package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.Constants.TypePipeline;
import frc.robot.utils.LimelightHelpers;

public class Vision {
  private static Vision limelight;
  private TypePipeline activePipeline;

  private Vision() {}

  public static Vision getInstance (){
    if (limelight == null){
      limelight = new Vision();
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

  public Pose2d getBotPose (){
    return LimelightHelpers.getBotPose2d(null);
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
      case AllTags:
      LimelightHelpers.setPipelineIndex(null, 0);
      break;
      case RedGridTags:
      LimelightHelpers.setPipelineIndex(null, 1);
      break;
      case BlueStationTag:
      LimelightHelpers.setPipelineIndex(null, 2);
      break;
      case RedStationTag:
      LimelightHelpers.setPipelineIndex(null, 3);
      break;
      case BlueGridTags:
      LimelightHelpers.setPipelineIndex(null, 4);
      break;
      case RetroflectiveTape:
      LimelightHelpers.setPipelineIndex(null, 5);
      break;
    }
  }

  public TypePipeline getActivePipeline (){
    return activePipeline;
  }
}