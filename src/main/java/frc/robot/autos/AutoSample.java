package frc.robot.autos;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.TestCommandTrajectory;
import frc.robot.interfaces.AutoInterface;

public class AutoSample implements AutoInterface {
    private List<Command> routine = new ArrayList<Command>();
    @Override public List<Command> getAutoRoutine (){
        routine.add(new TestCommandTrajectory("Hi", 5));
        return routine;
    }
}
