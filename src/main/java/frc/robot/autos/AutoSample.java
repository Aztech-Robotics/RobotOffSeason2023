package frc.robot.autos;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.AutoBase;
import frc.robot.PathBuilder;
import frc.robot.commands.TestCommandTrajectory;
import frc.robot.paths.SamplePath;

public class AutoSample implements AutoBase {
    private List<Command> routine = new ArrayList<Command>();
    private Command samplePath = PathBuilder.getInstance().createCommand(new SamplePath());
    @Override public List<Command> getAutoRoutine (){
        routine.add(new TestCommandTrajectory("Hi", 5));
        routine.add(samplePath);
        return null;
    }
}
