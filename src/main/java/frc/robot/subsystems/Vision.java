package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.Robot;
import frc.robot.Constants.TypePipeline;
import frc.robot.modes.GamePieceMode;
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
      case RetroflectiveTape:
      //LimelightHelpers.setPipelineIndex(null, 0);
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(0);
      break;
      case BlueTags:
      //LimelightHelpers.setPipelineIndex(null, 1);
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(1);
      break;
      case RedTags:
      //LimelightHelpers.setPipelineIndex(null, 2);
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(2);
      break;
    }
  }

  public void setPipelineByMode (){
    switch (GamePieceMode.getInstance().getMode()){
      case Cone:
      setPipeline(TypePipeline.RetroflectiveTape);
      break;
      case Cube:
      if (Robot.flip_alliance()){
        setPipeline(TypePipeline.RedTags);
      } else {
        setPipeline(TypePipeline.BlueTags);
      }
    }
  }

  public TypePipeline getActivePipeline (){
    return activePipeline;
  }
}