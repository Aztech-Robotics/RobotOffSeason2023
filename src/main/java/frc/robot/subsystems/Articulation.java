package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Articulation extends SubsystemBase {
  private static Articulation articulation;
  private Articulation() {}

  public static Articulation getInstance (){
    if (articulation == null){
      articulation = new Articulation();
    }
    return articulation;
  }

  @Override
  public void periodic() {
  }
}
