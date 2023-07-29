package frc.robot.utils;

import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.interfaces.PathInterface;
import frc.robot.subsystems.Drive;

public class PathBuilder {
    private static PathBuilder pathBuilder;
    private final Drive m_swerveDrive = Drive.getInstance(); 
    private PathInterface m_autoBase;

    private PathBuilder (){}

    public static PathBuilder getInstance (){
        if (pathBuilder == null){
            pathBuilder = new PathBuilder();
        }
        return pathBuilder;
    }

    public Command createCommand (PathInterface autoBase){
        
        return null; 
    }
}
