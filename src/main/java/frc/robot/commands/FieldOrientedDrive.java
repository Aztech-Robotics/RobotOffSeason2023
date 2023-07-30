package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.Constants.SwerveMode;
import frc.robot.subsystems.Drive;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class FieldOrientedDrive extends CommandBase {
  private final Drive m_SwerveDrive = Drive.getInstance();
  private final DoubleSupplier translationXSupplier;
  private final DoubleSupplier translationYSupplier;
  private final DoubleSupplier rotationSupplier;
  public FieldOrientedDrive(DoubleSupplier translationXSupplier, DoubleSupplier translationYSupplier, DoubleSupplier rotationSupplier) {
    this.translationXSupplier = translationXSupplier;
    this.translationYSupplier = translationYSupplier;
    this.rotationSupplier = rotationSupplier;
    addRequirements(m_SwerveDrive);
  }

  @Override
  public void initialize() {
    m_SwerveDrive.setMode(SwerveMode.OpenLoop);
  }

  @Override
  public void execute() {
    m_SwerveDrive.setDesiredChassisSpeeds(
      ChassisSpeeds.fromFieldRelativeSpeeds(
        MathUtil.applyDeadband(translationXSupplier.getAsDouble(), 0.2) * Constants.DriveTrain.maxDriveVel,
        MathUtil.applyDeadband(translationYSupplier.getAsDouble(), 0.2) * Constants.DriveTrain.maxDriveVel,
        MathUtil.applyDeadband(rotationSupplier.getAsDouble(), 0.2) * Constants.DriveTrain.maxAngVel,
        m_SwerveDrive.getGyroAngle()
      )
    );
  }

  @Override
  public void end(boolean interrupted) {
    m_SwerveDrive.setDesiredChassisSpeeds(new ChassisSpeeds());
    m_SwerveDrive.setMode(SwerveMode.Nothing);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
