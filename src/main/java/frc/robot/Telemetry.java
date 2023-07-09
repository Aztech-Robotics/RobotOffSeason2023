package frc.robot;

import javax.crypto.spec.RC2ParameterSpec;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.autos.AutoSample;

public class Telemetry extends SubsystemBase {
  ShuffleboardTab tabAutoPeriod = Shuffleboard.getTab("AutoPeriod");
  SendableChooser<Command> chooserAuto = new SendableChooser<>();
  AutoSample autoSample = new AutoSample();
  public Telemetry() {
    displayAutoCommands();
  }

  @Override
  public void periodic() {
  }

  private void displayAutoCommands (){
    chooserAuto.setDefaultOption("No Auto Selected", null);
    switch (DriverStation.getLocation()){
      case 1:
      chooserAuto.addOption("AutoSample", generateAutoCommand(autoSample));
      break;
      case 2:
      break;
      case 3:
      break;
      default:
      break;
    }
    tabAutoPeriod.add("Auto Selected", chooserAuto); 
  }

  private Command generateAutoCommand (AutoBase auto){
    SequentialCommandGroup autoCommand = new SequentialCommandGroup();
    for (Command command : auto.getAutoRoutine()) {
      autoCommand.addCommands(command);
    }
    return autoCommand;
  }

  public Command getAutonomousCommand (){
    return chooserAuto.getSelected();
  }
}
