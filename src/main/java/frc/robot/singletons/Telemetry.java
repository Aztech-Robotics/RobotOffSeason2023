package frc.robot.singletons;

import java.util.Map;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.autos.AutoSample;
import frc.robot.interfaces.AutoInterface;

public class Telemetry {
  private static Telemetry telemetry;
  SendableChooser<Command> chooserAuto = new SendableChooser<>(); 
  AutoSample autoSample = new AutoSample(); 
  ShuffleboardTab tabTeleopPeriod = Shuffleboard.getTab("DriverTab"); 
  boolean[][] array_grid = new boolean[8][2]; 
  int [][] activeNode = new int[2][1]; 
  private Telemetry() {
    tabTeleopPeriod.getComponents().clear(); 
    displayCamera();
    displayGrid();
    displayAutoCommands();
  }

  public static Telemetry getInstance (){
    if (telemetry == null){
      telemetry = new Telemetry();
    }
    return telemetry; 
  }

  private void displayCamera (){
    tabTeleopPeriod.addCamera("Limelight", "limelight", Limelight.getInstance().getURL());
  }

  private void displayGrid (){
    ShuffleboardLayout grid = tabTeleopPeriod.getLayout("Grid", BuiltInLayouts.kGrid).withSize(6, 2).withPosition(4, 0).withProperties(Map.of("Number of columns",9,"Number of rows",3,"Label position","HIDDEN"));
    for (int i=0; i<9; i++){
      for (int j=0; j<3; j++){
        int i_copy = i;
        int j_copy = j;
        if (i == 0 && j == 0){
          array_grid[i][j] = true;
        } else {
          array_grid[i][j] = false;
        }
        if (i%2 == 0){
          grid.addBoolean("("+i+","+j+")", ()->{return getNodeValue(i_copy, j_copy);}).withPosition(8-i, 2-i).withProperties(Map.of("Color when true","#00ff00","Color when false","#ffff00")); 
        } else {
          grid.addBoolean("("+i+","+j+")", ()->{return getNodeValue(i_copy, j_copy);}).withPosition(8-i, 2-i).withProperties(Map.of("Color when true","#00ff00","Color when false","#8066cc")); 
        }
      }
    }
  }

  private boolean getNodeValue (int i, int j){
    return array_grid[i][j];
  }

  public void setNodeValue (int move_x, int move_y){
    //i -> move_x
    //j -> move_y
    int new_i = activeNode[0][0] + move_x;
    int new_j = activeNode[1][0] + move_y;
    if (new_i >=0 && new_i <= 8 && new_j >= 0 && new_j <=2){
      array_grid[activeNode[0][0]][activeNode[0][1]] = false;
      array_grid[new_i][new_j] = true;
      activeNode[0][0] = new_i;
      activeNode[1][0] = new_j; 
    }
  }

  private Command generateAutoCommand (AutoInterface auto){
    SequentialCommandGroup autoCommand = new SequentialCommandGroup();
    for (Command command : auto.getAutoRoutine()) {
      autoCommand.addCommands(command);
    }
    return autoCommand;
  }

  private void displayAutoCommands (){
    chooserAuto.setDefaultOption("No Auto Selected", null);
    chooserAuto.addOption("AutoSample", generateAutoCommand(autoSample));
    tabTeleopPeriod.add("Auto Selected", chooserAuto); 
  }

  public Command getAutonomousCommand (){
    return chooserAuto.getSelected();
  }
}
