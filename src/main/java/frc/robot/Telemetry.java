package frc.robot;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.autos.TwoPiecesAuto;
import frc.robot.interfaces.AutoInterface;

public class Telemetry {
  public static ShuffleboardTab swerveTab = Shuffleboard.getTab("SwerveData"); 
  public static ShuffleboardTab driverTab = Shuffleboard.getTab("DriverData"); 
  public static ShuffleboardTab mechanismTab = Shuffleboard.getTab("MechanismData");
  public static SendableChooser<AutoInterface> auto_chooser = new SendableChooser<>(); 

  public static void displayAutos() {
    TwoPiecesAuto twoPiecesAuto = new TwoPiecesAuto();
    auto_chooser.setDefaultOption("None ", null);
    auto_chooser.addOption("TwoPiecesAuto", twoPiecesAuto); 
    driverTab.add(auto_chooser).withSize(2, 1).withPosition(4, 1);
  }
}
