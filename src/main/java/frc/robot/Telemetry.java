package frc.robot;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class Telemetry {
  public static ShuffleboardTab swerveTab = Shuffleboard.getTab("SwerveData"); 
  public static ShuffleboardTab driverTab = Shuffleboard.getTab("DriverData"); 
  public static ShuffleboardTab mechanismTab = Shuffleboard.getTab("MechanismData");
}
