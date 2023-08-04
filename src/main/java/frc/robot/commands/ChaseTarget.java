package frc.robot.commands;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Vision;

public class ChaseTarget extends CommandBase {
  private final Drive drive = Drive.getInstance(); 
  private final Vision vision = Vision.getInstance(); 
  private final ProfiledPIDController controller = new ProfiledPIDController(Constants.kp_chase, Constants.ki_chase, Constants.kd_chase, Constants.chase_constraints);
  private boolean flag;
  public ChaseTarget() {
    addRequirements(drive);
  }

  @Override
  public void initialize() {
    controller.setGoal(0);
    controller.setTolerance(0);
    controller.reset(vision.getX());
    flag = false;
  }

  @Override
  public void execute() {
    if (vision.sawTag()){
      drive.setDesiredChassisSpeeds(new ChassisSpeeds(controller.calculate(vision.getX()), 0, 0));
      flag = controller.atSetpoint();
    } else {
      flag = true;
      System.out.println("Command finished because of no target"); 
    }
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return flag;
  }
}
