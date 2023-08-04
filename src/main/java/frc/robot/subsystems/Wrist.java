package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Wrist extends SubsystemBase {
  private static Wrist wrist;
  private Wrist() {}

  public static Wrist getInstance (){
    if (wrist == null){
      wrist = new Wrist();
    }
    return wrist;
  }

  @Override
  public void periodic() {
  }
}
