package frc.robot.autos;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.TestCommandTrajectory;
import frc.robot.interfaces.AutoInterface;
import frc.robot.paths.SamplePath;
import frc.robot.utils.PathBuilder;

public class AutoSample implements AutoInterface {
    private List<Command> routine = new ArrayList<Command>();
    private Command samplePath = PathBuilder.getInstance().createCommand(new SamplePath());
    @Override public List<Command> getAutoRoutine (){
        routine.add(new TestCommandTrajectory("Hi", 5));
        routine.add(samplePath);
        return routine;
    }
}
