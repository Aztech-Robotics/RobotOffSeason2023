package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.actions.DropPiece;
import frc.robot.actions.PickUpDouble;
import frc.robot.actions.PickUpFloor;
import frc.robot.actions.PickUpSingle;
import frc.robot.actions.SaveMechanism;
import frc.robot.actions.ScoreBottom;
import frc.robot.actions.ScoreMiddle;
import frc.robot.actions.ScoreTop;
import frc.robot.actions.TakePiece;
import frc.robot.commands.ArmPosition;

public class ActionsSet {
    public static final ParallelCommandGroup manual_mode = new ParallelCommandGroup();
    public static final SequentialCommandGroup save_mechanism = new SequentialCommandGroup();
    public static final PickUpFloor pickup_floor = new PickUpFloor();
    public static final PickUpSingle pickup_single = new PickUpSingle();
    public static final PickUpDouble pickup_double = new PickUpDouble();
    public static final ScoreBottom score_bottom = new ScoreBottom();
    public static final ScoreMiddle score_middle = new ScoreMiddle();
    public static final ScoreTop score_top = new ScoreTop();
    public static final TakePiece take_piece = new TakePiece();
    public static final DropPiece drop_piece = new DropPiece();
    public static final ArmPosition prepare_high_pos = new ArmPosition(Rotation2d.fromDegrees(0));
}
