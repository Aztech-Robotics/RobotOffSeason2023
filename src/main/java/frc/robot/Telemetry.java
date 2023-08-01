package frc.robot;

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
import frc.robot.modes.GamePieceMode;
import frc.robot.subsystems.Vision;

public class Telemetry {
  private static Telemetry telemetry;
  public static ShuffleboardTab tabDrive = Shuffleboard.getTab("DriveData"); 
  public static ShuffleboardTab tabDriver = Shuffleboard.getTab("DriverTab"); 
  private GamePieceMode gamePieceMode = GamePieceMode.getInstance();
  private SendableChooser<Command> chooserAuto = new SendableChooser<>(); 
  private AutoSample autoSample = new AutoSample(); 
  private boolean[][] array_grid = new boolean[9][3]; 
  private boolean[] array_stations = new boolean[4];
  private int [] activeNode = new int[2]; 
  private int activeStation = 0;
  
  private Telemetry() {
    tabDriver.getComponents().clear(); 
    displayCamera();
    displayGrid();
    displayStations();
    displayAutoCommands();
    activeGamePieceMode();
  }

  public static Telemetry getInstance (){
    if (telemetry == null){
      telemetry = new Telemetry();
    }
    return telemetry;
  }

  private void displayCamera (){
    tabDriver.addCamera("Limelight", "limelight", Vision.getInstance().getURL())
    .withSize(4, 4)
    .withPosition(0, 0);
  }

  private void displayGrid (){
    ShuffleboardLayout grid = tabDriver.getLayout("Grid", BuiltInLayouts.kGrid)
    .withSize(6, 2)
    .withPosition(4, 0)
    .withProperties(Map.of("Number of columns",9,"Number of rows",3,"Label position","HIDDEN"));
    for (int i=0; i<9; i++){
      for (int j=0; j<3; j++){
        int i_copy = i;
        int j_copy = j;
        if (i == 0 && j == 0){
          array_grid[i][j] = true;
          activeNode[0] = 0;
          activeNode[1] = 0;
        } else {
          array_grid[i][j] = false;
        }
        if (i%2 == 0){
          grid.addBoolean("("+i+","+j+")", ()-> getNodeValue(i_copy, j_copy)).withPosition(i, i).withProperties(Map.of("Color when true","#00ff00","Color when false","#ffff00")); 
        } else {
          grid.addBoolean("("+i+","+j+")", ()-> getNodeValue(i_copy, j_copy)).withPosition(i, i).withProperties(Map.of("Color when true","#00ff00","Color when false","#8066cc")); 
        }
      }
    }
  }

  private boolean getNodeValue (int i, int j){
    return array_grid[i][j];
  }

  public void moveActiveNode (int move_x, int move_y){
    //i -> move_x
    //j -> move_y
    int new_i = activeNode[0] + move_x;
    int new_j = activeNode[1] + move_y;
    if (new_i >=0 && new_i <= 8 && new_j >= 0 && new_j <=2){
      array_grid[activeNode[0]][activeNode[1]] = false;
      array_grid[new_i][new_j] = true;
      activeNode[0] = new_i;
      activeNode[1] = new_j; 
    }
  }

  public int getNode (){
    return activeNode[0] + 1;
  }

  public int getNodeLevel (){
    return activeNode[1] + 1; 
  }

  private void displayStations (){
    ShuffleboardLayout stations = tabDriver.getLayout("Stations", BuiltInLayouts.kGrid)
    .withSize(3, 1)
    .withPosition(4, 2)
    .withProperties(Map.of("Number of columns", 4, "Number of rows", 1, "Label position", "BOTTOM"));
    for (int i=0; i<4; i++){
      int i_copy = i;
      if (i == 0){
        array_stations[i] = true; 
        activeStation = 0;
      } else {
        array_stations[i] = false;
      }
      String selected_name = "";
      switch (i){
        case 0:
        selected_name = "Floor";
        break;
        case 1:
        selected_name = "Single";
        break;
        case 2:
        selected_name = "Left";
        break;
        case 3:
        selected_name = "Right";
        break; 
      }
      stations.addBoolean(selected_name, ()-> getStationValue(i_copy));
    }
  }

  private boolean getStationValue (int i){
    return array_stations[i];
  }

  public void moveActiveStation (int move_x){
    //i -> move_x
    int new_i = activeStation + move_x;
    if (new_i >=0 && new_i <= 3){
      array_stations[activeStation] = false;
      array_stations[new_i] = true;
      activeStation = new_i;
    }
  }

  public int getActiveStation (){
    return activeStation;
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
    tabDriver.add("Auto Selected", chooserAuto).withSize(3, 1).withPosition(7, 2); 
  }

  public Command getAutonomousCommand (){
    return chooserAuto.getSelected();
  }

  public void activeGamePieceMode (){
    gamePieceMode.outputTelemetry();
  }
}
