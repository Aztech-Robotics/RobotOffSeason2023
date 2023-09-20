package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.actions.PickUpFloor;
import frc.robot.actions.PickUpSingle;
import frc.robot.actions.SaveMechanism;
import frc.robot.actions.ScoreBottom;
import frc.robot.actions.ScoreMiddle;
import frc.robot.commands.IntakeVel;
import frc.robot.commands.ManualCommand;

public class ActionsSet {
    public static final ManualCommand manual_mode = new ManualCommand();
    public static final SaveMechanism save_mechanism = new SaveMechanism();
    public static final PickUpFloor pickup_floor = new PickUpFloor();
    public static final PickUpSingle pickup_single = new PickUpSingle();
    public static final ScoreBottom score_bottom = new ScoreBottom();
    public static final ScoreMiddle score_middle = new ScoreMiddle();
    public static final IntakeVel intakeCommand = new IntakeVel();
}
