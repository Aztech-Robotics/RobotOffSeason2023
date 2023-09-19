package frc.robot;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class Controls {
    public static CommandXboxController driver1 = new CommandXboxController(0);
    public static CommandXboxController driver2 = new CommandXboxController(1);

    //Traccion
    public static DoubleSupplier getLeftYD1 () {
        return () -> MathUtil.applyDeadband(driver1.getLeftY(), 0.2);
    }

    public static DoubleSupplier getLeftXD1 () {
        return () -> MathUtil.applyDeadband(driver1.getLeftX(), 0.2);
    }

    public static DoubleSupplier getRightYD1 () {
        return () -> MathUtil.applyDeadband(driver2.getRightY(), 0.2);
    }

    public static DoubleSupplier getRightXD1 () {
        return () -> MathUtil.applyDeadband(driver1.getRightX(), 0.2);
    }

    //Mechanism
    public static DoubleSupplier getLeftYD2 () {
        return () -> MathUtil.applyDeadband(-driver2.getLeftY(), 0.2);
    }

    public static DoubleSupplier getLeftXD2 () {
        return () -> MathUtil.applyDeadband(driver2.getLeftX(), 0.2);
    }

    public static DoubleSupplier getRightYD2 () {
        return () -> MathUtil.applyDeadband(-driver2.getRightY(), 0.2);
    }

    public static DoubleSupplier getRightXD2 () {
        return () -> MathUtil.applyDeadband(driver2.getRightX(), 0.2);
    }

    public static DoubleSupplier getLeftTD1 (){
        return () -> driver1.getLeftTriggerAxis();
    }

    public static DoubleSupplier getRightTD1 (){
        return () -> driver1.getRightTriggerAxis();
    }

    public static DoubleSupplier getTD2 (){
        return () -> driver2.getRightTriggerAxis() - driver2.getLeftTriggerAxis();
    }
}
