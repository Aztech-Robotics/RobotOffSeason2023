package frc.robot;

import java.util.HashMap;
import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.wpilibj2.command.Command;

public interface PathBase {
    public HashMap<String, Command> getEventMap ();
    public PathPlannerTrajectory getTrajectory (); 
    public Command getCommandBefore ();
    public Command getCommandAfter (); 
    public boolean isFirstPath ();
}
