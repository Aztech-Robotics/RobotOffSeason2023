package frc.robot;

import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drive;

public class PathBuilder {
    private static PathBuilder pathBuilder;
    private final Drive m_swerveDrive = Drive.getInstance(); 
    private PathBase m_autoBase;

    private PathBuilder (){}

    public static PathBuilder getInstance (){
        if (pathBuilder == null){
            pathBuilder = new PathBuilder();
        }
        return pathBuilder;
    }

    public Command createCommand (PathBase autoBase){
        SwerveAutoBuilder autoBuilder = new SwerveAutoBuilder(
            m_swerveDrive::getCurrentPose, 
            m_swerveDrive::setCurrentPose, 
            Drive.swerveDriveKinematics, 
            new PIDConstants(0, 0, 0), 
            new PIDConstants(0, 0, 0), 
            m_swerveDrive::setModulesStatesWithVelocity, 
            m_autoBase.getEventMap(), 
            true, 
            m_swerveDrive
        );
        Command pathCommand = autoBuilder.followPathWithEvents(m_autoBase.getTrajectory());
        Command commandBefore = autoBase.getCommandBefore();
        Command commandAfter = autoBase.getCommandAfter();
        if (m_autoBase.isFirstPath()){
            pathCommand.beforeStarting(
                () -> {
                    m_swerveDrive.resetChassisPosition(m_autoBase.getTrajectory().getInitialPose());
                }, 
                m_swerveDrive
            );
        }
        if (commandBefore != null){
            pathCommand.beforeStarting(commandBefore);
        }
        if (commandAfter != null){
            pathCommand.andThen(commandAfter); 
        }
        return pathCommand; 
    }
}
