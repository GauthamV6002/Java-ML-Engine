import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

import javafx.geometry.Bounds;

public class Asteroid extends Circle {

  // Velocity is the displacement that the asteroid travels per frame in the x and y
  private double[] velocity = {1, 0}; // {x, y}
  private boolean justTurned = false; // So that turning doesn't repeat on an edge

  private final double WIDTH = 400, HEIGHT = 400;

  // Method to quickly set the position of the asteroid
  public void setPosition(double x, double y) {
    this.setCenterX(x); 
    this.setCenterY(y);
  }

  // Move the asteroid by a offset
  public void translate(double x, double y) {
    this.setCenterX(this.getCenterX() + x);
    this.setCenterY(this.getCenterY() + y);
  }

  // Contrustor sets positions & velocity, and randomizes radius
  public Asteroid(double[] velocity, double x, double y) {
     // Reset Positions
    this.setPosition(x, y);
    this.setFill(Color.GREY);

    this.velocity = velocity;
     
    this.setRadius((Math.random() * 20) + 15);
   }

  public void update() {
    // Boundary Detection - Get the bounds, and if the minimum bounds are less than 0 or the maximum bounds are greater than the width or height, have the asteroid bounce by reversing one of the velocity components 
    Bounds bounds = this.getBoundsInLocal();
    if((bounds.getMinX() <= 0 || bounds.getMaxX() >= this.WIDTH) && !justTurned){
      velocity[0] *= -1; // Reverse x component
      this.justTurned = true;
    } else if ((bounds.getMinY() <= 0 || bounds.getMaxY() >= this.HEIGHT) && !justTurned) {
      velocity[1] *= -1; // Reverse y component
      this.justTurned = true;
    } else {
      this.translate(this.velocity[0], this.velocity[1]);
      this.justTurned = false;
    }
    
  }
  
}