package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class TestCommand extends CommandBase {
  String message, updateMessage = "";
  boolean flag = false;
  double startTime, duration;
  public TestCommand (String message, double duration) {
    this.message = message;
    this.duration = duration;
  }

  @Override
  public void initialize() {
    flag = false;
    startTime = Timer.getFPGATimestamp();
  }

  @Override
  public void execute() {
    if (getSeconds() >= duration){
      flag = true;
    }
    updateMessage = "Seconds" + getSeconds();
    SmartDashboard.putString("Running " + message, updateMessage);
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return flag;
  }

  public double getSeconds (){
    return (Timer.getFPGATimestamp() - startTime);
  }
}
