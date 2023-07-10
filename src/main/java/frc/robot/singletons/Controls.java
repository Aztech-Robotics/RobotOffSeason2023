package frc.robot.singletons;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class Controls {
    private static Controls controls;
    private CommandXboxController driver1 = new CommandXboxController(0);
    private CommandXboxController driver2 = new CommandXboxController(1);

    private Controls (){
        driver1.a().onTrue(null);
    }

    private Controls getInstance (){
        if (controls == null){
            controls = new Controls();
        }
        return controls;
    }
}
