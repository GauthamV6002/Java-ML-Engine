import javafx.scene.Group;
import java.util.ArrayList;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;

public class Ecosystem {
  private ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>(); // Store all asteroids
  private Learner learner;

  private Group root; //For adding objects to the UI

  private double WIDTH, HEIGHT;
  public int FRAME_UPDATE_DELAY = 100; // Time between each frame
  int generation = 0; // Generation counter

  // Constructor assigns relavant variables, and adds each asteroid
  public Ecosystem(int numAsteroids, Group root, double WIDTH, double HEIGHT) {
    this.WIDTH = WIDTH;
    this.HEIGHT = HEIGHT;
    
    this.root = root;
    this.learner = new Learner(30, numAsteroids, root);
    
    for(int i = 0; i < numAsteroids; i++) addAsteroid();
  }

  // Delay utility function that takes a callback(Runnable), sleeps for a certain amount of time, and runs the callback
  public static void delay(long millis, Runnable continuation) {
    Task<Void> sleeperTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            try { Thread.sleep(millis); }
            catch (InterruptedException e) { }
            return null;
        }
    };
    sleeperTask.setOnSucceeded(event -> continuation.run());
    new Thread(sleeperTask).start();
  }

  // For each asteroid, generate a random velocity, starting position, and add it to the asteroids list AND to the UI
  private void addAsteroid() {
    double[] velo = { (Math.random() * 20) - 10, (Math.random() * 10) - 5 };
    double[] coords = genValidCoords();
    Asteroid asteroid = new Asteroid(velo, coords[0], coords[1]);
    this.asteroids.add(asteroid);

    this.root.getChildren().add(asteroid);
  }

  // Give the asteroid new positions after each generation to avoid overfitting
  private void resetAsteroids() {
    for(Asteroid asteroid : this.asteroids) {
      double[] coords = genValidCoords();
      asteroid.setPosition(coords[0], coords[1]);
    }
  }

  // "Valid Coords" need to be used so that the ship doesn't instantly crash
  private double[] genValidCoords() {
    // Valid Coords => Outside the central {WIDTH/2} by {HEIGHT/2} box
    // Start by generating random values in a 200x200 area
    double x = Math.floor(Math.random() * WIDTH/2); 
    double y = Math.floor(Math.random() * HEIGHT/2);

    // Any values over 100 have 200 added to them, thus creating a 100-wide coords area around the center
    if(x > WIDTH / 4) x += 200;
    if(y > HEIGHT / 4) y += 200;

    double[] coords = {x, y};
    return coords;
  }

  // Prints the next generation & runs the runGeneration function
  public void nextGeneration() {
    System.out.println("\u001b[31m" + "Generation " + this.generation);
    this.runGeneration();
  }

  // ** MAIN UPDATE FUNCTION
  // Update each asteroid, and each agent in the learner
  // Recursively do this with a delay to allow users to see what's happening
  // When the generation ends (ie., all ships crashed, determined in this.learner), reset the asteroids, run the genetic selection algorithm, increment the generation, and update again
  public void runGeneration() {
    for(Asteroid asteroid : this.asteroids) asteroid.update();
    if(this.learner.updateAll(this.asteroids)) {
      delay(this.FRAME_UPDATE_DELAY, () -> runGeneration());
    } else {
      resetAsteroids();
      this.learner.geneticSelect();
      generation++;
      this.nextGeneration();
    }
  }
}