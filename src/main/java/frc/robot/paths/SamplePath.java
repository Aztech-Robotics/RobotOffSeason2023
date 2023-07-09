package frc.robot.paths;

import java.util.HashMap;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.PathBase;

public class SamplePath implements PathBase {
    private PathPlannerTrajectory trajectory = PathPlanner.loadPath("SamplePath", new PathConstraints(4, 3));
    public SamplePath (){
    }

    @Override public HashMap<String, Command> getEventMap (){
        return null;
    }

    @Override public PathPlannerTrajectory getTrajectory (){
        return trajectory;
    }

    @Override public Command getCommandBefore (){
        return null; 
    }

    @Override public Command getCommandAfter (){
        return null;
    }

    @Override public boolean isFirstPath (){
        return true;
    }
}
