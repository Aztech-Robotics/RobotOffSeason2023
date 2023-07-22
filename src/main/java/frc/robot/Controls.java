package frc.robot;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class Controls {
    public static CommandXboxController driver1 = new CommandXboxController(0);
    public static CommandXboxController driver2 = new CommandXboxController(1);

    public static DoubleSupplier getLeftYD1 () {
        return () -> MathUtil.applyDeadband(driver1.getLeftY(), 0.2);
    }

    public static DoubleSupplier getLeftXD1 () {
        return () -> MathUtil.applyDeadband(driver1.getLeftX(), 0.2);
    }

    public static DoubleSupplier getRightXD1 () {
        return () -> MathUtil.applyDeadband(driver1.getRightX(), 0.2);
    }

    public static DoubleSupplier getLeftYD2 () {
        return () -> MathUtil.applyDeadband(driver2.getLeftY(), 0.2);
    }

    public static DoubleSupplier getLeftXD2 () {
        return () -> MathUtil.applyDeadband(driver2.getLeftX(), 0.2);
    }

    public static DoubleSupplier getRightYD1 () {
        return () -> MathUtil.applyDeadband(driver2.getRightY(), 0.2);
    }

    public static DoubleSupplier getRightXD2 () {
        return () -> MathUtil.applyDeadband(driver2.getRightX(), 0.2);
    }

    public static Trigger getRBumperD1 (){
        return driver1.rightBumper();
    }

    public static Trigger getRBumperD2 (){
        return driver2.rightBumper();
    }

    public static Trigger getLBumperD2 (){
        return driver2.leftBumper();
    }

    public static Trigger getAD2 (){
        return driver2.a();
    }

    public static Trigger getBD2 (){
        return driver2.b();
    }

    public static Trigger getXD2 (){
        return driver2.x();
    }

    public static Trigger getYD2 (){
        return driver2.y();
    }

    public static Trigger movingLeftJD2 (){
        Trigger movingJoystick = new Trigger(
            () -> {
                return (getLeftXD2().getAsDouble() != 0 || getLeftYD2().getAsDouble() != 0); 
            }
        );
        return movingJoystick;
    }
}
