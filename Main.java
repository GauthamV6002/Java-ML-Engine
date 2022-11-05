import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;

import javafx.scene.control.Button;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application 
{ 
  
  @Override
  public void start(Stage primaryStage) {

    // Scanner to get frame delay
    Scanner scanner = new Scanner(System.in);
    
    // Set width and height
    double WIDTH = 400, HEIGHT = 400;

    // Initialize a start button, and create a the root and scene to place elements in
    Button startBtn = new Button("Start!");
    Group root = new Group(startBtn);
    Scene scene = new Scene(root, WIDTH, HEIGHT);

    // Ecosystem class manages all the objects on the scene, 5 is the # of asteroids
    Ecosystem ecosystem = new Ecosystem(5, root, WIDTH, HEIGHT);

    // Get the frame delay
    System.out.print("Enter Frame Update Delay: ");
    ecosystem.FRAME_UPDATE_DELAY = scanner.nextInt();

    // When the start button is clicked, start the simulation with the runGeneration function, and remove the start button from the UI 
    startBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent e) {
        ecosystem.runGeneration();
        root.getChildren().remove(startBtn);
      }
    });

    //Set the title, and display the application
    primaryStage.setTitle("ML Engine");
    primaryStage.setScene(scene);
    primaryStage.show();

  } 
    
  public static void main(String[] args) {
    launch(args);
  }
} 
