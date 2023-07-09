package frc.robot.paths;

import java.util.ArrayList;
import java.util.HashMap;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.PathBase;
import frc.robot.commands.TestCommandTrajectory;

public class SamplePathWithEvents implements PathBase {
    private PathPlannerTrajectory trajectory = PathPlanner.loadPath("SamplePathWithEvents", new PathConstraints(0.5, 0.5));
    public SamplePathWithEvents (){
    }

    @Override public HashMap<String, Command> getEventMap (){
        HashMap<String, Command> eventMap = new HashMap<>();
        eventMap.put("Evento1", new TestCommandTrajectory("Evento1", 2));
        eventMap.put("Evento2", new TestCommandTrajectory("Evento2", 2));
        eventMap.put("Evento3", new TestCommandTrajectory("Evento3", 2));
        eventMap.put("Evento4", new TestCommandTrajectory("Evento4", 2));
        eventMap.put("Evento5", new TestCommandTrajectory("Evento5", 2));

        return eventMap;
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
