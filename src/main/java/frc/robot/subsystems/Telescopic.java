package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Telescopic extends SubsystemBase {
  private static Telescopic telescopic;
  private Telescopic() {}

  public static Telescopic getInstance (){
    if (telescopic == null){
      telescopic = new Telescopic();
    }
    return telescopic;
  }

  @Override
  public void periodic() {
  }
}
