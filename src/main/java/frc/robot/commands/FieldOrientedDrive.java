package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.Controls;
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
  public FieldOrientedDrive() {
    this.translationXSupplier = Controls.getLeftXD1();
    this.translationYSupplier = Controls.getLeftYD1();
    this.rotationSupplier = Controls.getRightXD1();
    addRequirements(m_SwerveDrive);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    m_SwerveDrive.setDesiredChassisSpeeds(
      ChassisSpeeds.fromFieldRelativeSpeeds(
        MathUtil.applyDeadband(translationYSupplier.getAsDouble(), 0.2) * 3.5,
        MathUtil.applyDeadband(translationXSupplier.getAsDouble(), 0.2) * 3.5,
        MathUtil.applyDeadband(rotationSupplier.getAsDouble(), 0.2) * Constants.maxAngVel,
        m_SwerveDrive.getYawAngle()
      )
    );
  }

  @Override
  public void end(boolean interrupted) {
    m_SwerveDrive.setDesiredChassisSpeeds(new ChassisSpeeds());
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
