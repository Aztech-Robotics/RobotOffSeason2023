package frc.robot.modes;

import java.util.Map;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Telemetry;
import frc.robot.Constants.GameElement;

public class GamePieceMode extends SubsystemBase {
    private static GamePieceMode generalMode = null;
    private static GameElement activeMode;
    private boolean notifier = false;

    private GamePieceMode (){
        outputTelemetry();
    }

    public static GamePieceMode getInstance (){
        if (generalMode == null){
            generalMode = new GamePieceMode();
            activeMode = GameElement.Cone;
        }
        return generalMode;
    }
    
    public GameElement getMode (){
        return activeMode; 
    }

    public void setMode (GameElement mode){
        notifier = true;
        activeMode = mode;
    }

    public CommandBase commandSetMode (GameElement mode){
        return runOnce(
            () -> {
                notifier = true;
                activeMode = mode; 
            }
        );
    }

    public CommandBase toggleMode (){
        return runOnce(
            () -> {
                notifier = true;
                if (activeMode == GameElement.Cone){
                    activeMode = GameElement.Cube;
                }
               else {
                    activeMode = GameElement.Cone;
                }
            }
        );
    }

    public boolean haveChanged (){
        boolean aux = notifier;
        SmartDashboard.putBoolean("HC", aux);
        notifier = false;
        return aux;
    }

    public void outputTelemetry (){
        Telemetry.driverTab.addBoolean("GamePiece", 
        () -> {
           boolean value = false;
            if (GamePieceMode.getInstance().getMode() == GameElement.Cone){
                value = true;
            }
            return value;
        }).withSize(1, 1).withPosition(4, 0).withProperties(Map.of("Color when true","#ffff00","Color when false","#8066cc"));
    }
}
