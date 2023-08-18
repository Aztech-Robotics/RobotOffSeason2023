package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Telescopic extends SubsystemBase {
  private static Telescopic telescopic;
  /*
  private final TalonFX tel_master = new TalonFX(Constants.id_tel_master);
  private final TalonFX tel_sleeve = new TalonFX(Constants.id_tel_sleeve);
   */

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
