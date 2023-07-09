package frc.robot.swerve;

import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Constants;

public class SwerveModule {
    private TalonFX steerMotor;
    private CANSparkMax speedMotor1, speedMotor2;
    private RelativeEncoder encoder_speedMotor;
    private SparkMaxPIDController controller_speedMotor;
    private CANcoder cancoder;
    ShuffleboardTab tabMotorsData = Shuffleboard.getTab("MotorsData");
    public boolean isBrakeMode = false;

    
    public SwerveModule (int id_steerMotor, int id_cancoder, int id_speedMotor1, int id_speedMotor2, Rotation2d steerOffset){
        //Variables Initialization 
        steerMotor = new TalonFX(id_steerMotor);
        speedMotor1 = new CANSparkMax(id_speedMotor1, MotorType.kBrushless);
        speedMotor2 = new CANSparkMax(id_speedMotor2, MotorType.kBrushless);
        encoder_speedMotor = speedMotor1.getEncoder();
        controller_speedMotor = speedMotor1.getPIDController();
        cancoder = new CANcoder(id_cancoder);
        //CanCoder Configuration
        MagnetSensorConfigs canCoderConfig = new MagnetSensorConfigs();
        canCoderConfig.AbsoluteSensorRange = AbsoluteSensorRangeValue.Signed_PlusMinusHalf;
        canCoderConfig.MagnetOffset = 0;
        canCoderConfig.SensorDirection = SensorDirectionValue.Clockwise_Positive;
        cancoder.getConfigurator().apply(canCoderConfig);
        //SteerMotor Configuration
        TalonFXConfiguration talonConfig = new TalonFXConfiguration();
        talonConfig.ClosedLoopGeneral.ContinuousWrap = true;
        talonConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        talonConfig.CurrentLimits.StatorCurrentLimit = 100;
        talonConfig.CurrentLimits.SupplyCurrentLimitEnable = false;
        talonConfig.Feedback.FeedbackRemoteSensorID = cancoder.getDeviceID();
        talonConfig.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RemoteCANcoder;
        talonConfig.Slot0.kS = Constants.ks_steerController;
        talonConfig.Slot0.kV = Constants.kv_steerController;
        talonConfig.Slot0.kP = Constants.kp_steerController;
        talonConfig.Slot0.kI = Constants.ki_steerController;
        talonConfig.Slot0.kD = Constants.kd_steerController;
        steerMotor.getConfigurator().apply(talonConfig);
        //SpeedMotor Configuration
        speedMotor1.enableVoltageCompensation(12);
        speedMotor2.enableVoltageCompensation(12);
        speedMotor2.follow(speedMotor1);
        encoder_speedMotor.setPositionConversionFactor(Constants.drivePositionCoefficient);
        encoder_speedMotor.setVelocityConversionFactor(Constants.driveVelocityCoefficient);
        controller_speedMotor.setP(Constants.kp_speedController);
        controller_speedMotor.setI(Constants.ki_speedController);
        controller_speedMotor.setD(Constants.ki_speedController);
        controller_speedMotor.setFF(Constants.kf_speedController);
        controller_speedMotor.setIZone(Constants.kIz_speedController);
        controller_speedMotor.setFeedbackDevice(encoder_speedMotor); 
        outputTelemetry(); 
    }

    public void setCoastMode (){
        var neutralMode = new MotorOutputConfigs();
        neutralMode.NeutralMode = NeutralModeValue.Coast;
        steerMotor.getConfigurator().apply(neutralMode);
        speedMotor1.setIdleMode(IdleMode.kCoast);
        speedMotor2.setIdleMode(IdleMode.kCoast);
        isBrakeMode = false;
    }

    public void setBrakeMode (){
        var neutralMode = new MotorOutputConfigs();
        neutralMode.NeutralMode = NeutralModeValue.Brake;
        steerMotor.getConfigurator().apply(neutralMode);
        speedMotor1.setIdleMode(IdleMode.kBrake);
        speedMotor2.setIdleMode(IdleMode.kBrake);
        isBrakeMode = true;
    }

    public Rotation2d getCanCoderAngle (){
        return Rotation2d.fromRotations(cancoder.getAbsolutePosition().getValue());
    }

    public void setAngleMotor (Rotation2d angle){
        PositionTorqueCurrentFOC positionWithTorque = new PositionTorqueCurrentFOC(0, 0, 0, false);
        steerMotor.setControl(positionWithTorque.withPosition(angle.getRotations()));
    }

    public double getPositionSpeedMotor (){
        return encoder_speedMotor.getPosition();
    }

    public void setPositionSpeedMotor (double position){
        encoder_speedMotor.setPosition(position);
    }

    public double getVelocitySpeedMotor (){
        return encoder_speedMotor.getVelocity();
    }

    public void setVelocitySpeedMotor (double velocity){
        controller_speedMotor.setReference(velocity, ControlType.kVelocity, 0, 0);
    }

    public void setVoltageSpeedMotor (double voltage){
        controller_speedMotor.setReference(voltage, ControlType.kDutyCycle, 0, 0);
    }

    public SwerveModuleState getModuleState (){
        return new SwerveModuleState(getVelocitySpeedMotor(), getCanCoderAngle());
    }

    public void setModuleStateWithVelocity (SwerveModuleState moduleState){
        setAngleMotor(moduleState.angle);
        setVelocitySpeedMotor(moduleState.speedMetersPerSecond);
    }

    public void setModuleStateWithVoltage (SwerveModuleState moduleState){
        setAngleMotor(moduleState.angle);
        setVoltageSpeedMotor(moduleState.speedMetersPerSecond / Constants.maxDriveVel);
    }

    public SwerveModulePosition getModulePosition (){
        return new SwerveModulePosition(getPositionSpeedMotor(), getCanCoderAngle());
    }

    public void outputTelemetry (){
        ShuffleboardLayout motorsData = tabMotorsData.getLayout("Module " + speedMotor1.getDeviceId() + "-" + steerMotor.getDeviceID(), BuiltInLayouts.kList).withSize(2, 3);
        motorsData.addDouble("SpeedMotorPosition", () -> {return getPositionSpeedMotor();});
        motorsData.addDouble("SpeedMotorVelocity", () -> {return getVelocitySpeedMotor();});
        motorsData.addDouble("CanCoderAngle", () -> {return getCanCoderAngle().getRotations();});
    }
}
