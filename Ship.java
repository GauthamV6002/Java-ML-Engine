import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.paint.Color;
import javafx.geometry.Bounds;

import java.util.ArrayList;

public class Ship extends Circle {

  private final double WIDTH = 400, HEIGHT = 400;

  // Constants for the ship
  private final double radius = 15; //Size of the suhip
  private final double maxRaycastDist = 200; // Distance that the ship can "see"
  private final double rotationSpeed = 30; // degrees to rotate per rotation-move
  private final double linearSpeed = 5; // Speed that the ship moves per frame
  private double dirAngle = 0; // Angle that the ship travels at

  public int survivedFrames = 0; // Counter that indicates fitness

  public Network network; // Has a network instance for the output calculation

  // Check for any collisions, by getting the intersection of the asteroids & ship per frame
  public boolean getCollisions(ArrayList<Asteroid> asteroids) {
    for(Asteroid asteroid : asteroids) {
      if (this.getBoundsInParent().intersects(asteroid.getBoundsInParent())) {
        return true;
      }
    }
    return false;
  }

  // Offsets the ship's position, unless it would go out of bounds
  public void translate(double x, double y) {

    Bounds bounds = this.getBoundsInLocal();

    // Bounds checking
    if((bounds.getMinX() + x >= 0) && (bounds.getMaxX() + x <= this.WIDTH) && (bounds.getMinY() + y >= 0) && (bounds.getMaxY() + y <= this.HEIGHT)) {      
      this.setCenterX(this.getCenterX() + x);
      this.setCenterY(this.getCenterY() + y);
    }
    
  }

  // Position Setter Function
  public void setPosition(double x, double y){
    this.setCenterX(x);
    this.setCenterY(y);
  }

  // Utility function to center the ship on the screen
  public void center() {
    this.setPosition(this.WIDTH / 2 , this.HEIGHT / 2);
  }

  // Constructor sets the color, radius, and centers the ship
  public Ship() {
    this.setFill(Color.BLUE);
    this.center();
    this.setRadius(radius);

    // Neural Network Setup
    int[] layerSizes = {5, 8, 6, 2};
		this.network = new Network(layerSizes);
  } 

  // Keeps the angle from going above 360 or below 0 -> so it stays in Q1 - Q4, for simpler trig
  private void normalizeAngle() {
    if(this.dirAngle > 360) this.dirAngle -= 360;
    else if(this.dirAngle < 0) this.dirAngle += 360;
  }

  // Distance between two points
  private double getDistance(double x1, double y1, double x2, double y2) {
    return Math.sqrt(((y2 - y1) * (y2 - y1)) + ((x2 - x1) * (x2 - x1)));
  }

  // Raycasting function takes the distance between each asteroid and the ship, and if it exceeds the max distance, the input is set to -1; otherwise, its set to the distance between the ship and asteroid
  private ArrayList<Double> getRaycasts(ArrayList<Asteroid> asteroids) {
    ArrayList<Double> rayDists = new ArrayList<Double>();
    
    for(Asteroid asteroid : asteroids) {
      double dist = getDistance(this.getCenterX(), this.getCenterY(), asteroid.getCenterX(), asteroid.getCenterY());
      if(dist <= this.maxRaycastDist) rayDists.add(dist / this.maxRaycastDist); // Normalize distances so the network doesn't implode from massive values
      else rayDists.add(-1.0);
    }
    return rayDists;
  }

  // **UPDATE FUNCTION
  // Using the raycasts, set inputs, calculate the neural network, get the result move, and take an action based on that
  public void update(ArrayList<Asteroid> asteroids) { // -1: left, 0: thurst, 1: right 

    network.setInputs(this.getRaycasts(asteroids));
    network.calcNetwork();
    // network.printNetwork();

    int move = network.getMove();
    
    if(move == 0){
      // Trig is used to convert polar coordinates into cartesian ones, to add in the x & y components.
      this.translate(Math.cos(Math.toRadians(this.dirAngle)) * this.linearSpeed, Math.sin(Math.toRadians(this.dirAngle)) * this.linearSpeed);
    } else {
      // If the move is not a 0, then the ship rotates
      this.dirAngle += (move * this.rotationSpeed);
      this.normalizeAngle();
    }

    this.survivedFrames++; // Increment the survived frames
  }
  
}