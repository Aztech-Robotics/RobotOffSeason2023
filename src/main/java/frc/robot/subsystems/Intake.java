package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
  private static Intake intake; 
  private final CANSparkMax intake_m = new CANSparkMax(Constants.id_intake, MotorType.kBrushless);
  private Intake() {}

  public static Intake getInstance (){
    if (intake == null){
      intake = new Intake();
    }
    return intake;
  }

  @Override
  public void periodic() {
  }

  public void setVelocity (double velocity){
    intake_m.set(velocity);
  }
}
