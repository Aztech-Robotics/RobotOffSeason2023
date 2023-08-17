package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class TestCommand extends CommandBase {
  String message;
  ShuffleboardTab tabDrive = Shuffleboard.getTab("DriveData");
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
    tabDrive.addString("Test Command Running " + message, ()->{return ("Seconds: " + getSeconds());});
  }

  @Override
  public void execute() {
    if (getSeconds() >= duration){
      flag = true;
    }
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
