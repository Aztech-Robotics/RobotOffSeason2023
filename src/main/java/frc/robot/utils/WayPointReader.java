package frc.robot.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.math.spline.Spline;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import frc.robot.Constants;
import frc.robot.Robot;

public class WayPointReader {
    private static final double PATHWEAVER_Y_OFFSET = 8.0137;

    public static TrajectoryGenerator.ControlVectorList getControlVectors(Path path) throws IOException {
        TrajectoryGenerator.ControlVectorList controlVectors = new TrajectoryGenerator.ControlVectorList(); 
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))){
            boolean skippedFirst = false;
            String line = reader.readLine();
            while (line != null) {
                if (!skippedFirst || !line.contains(",")) {
                    skippedFirst = true;
                    line = reader.readLine();
                    continue;
                }
                String[] split = line.split(",");
                double x = Double.parseDouble(split[0]);
                double x_tan = Double.parseDouble(split[2]);
                // if (Robot.flip_alliance()) {
                //     x = Constants.FieldLayout.kFieldLength - x;
                //     x_tan = - x_tan;
                // }
                controlVectors.add(new Spline.ControlVector(
                        new double[]{x, x_tan, 0},
                        new double[]{Double.parseDouble(split[1]), Double.parseDouble(split[3]), 0}));

                line = reader.readLine();
            }
        }
        return controlVectors;
    }
}
