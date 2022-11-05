import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Learner {

  // 2 ArrayLists, one for ALL the agents, and one for the ACTIVE ones
  private ArrayList<Ship> agents = new ArrayList<Ship>();
  private ArrayList<Ship> activeAgents = new ArrayList<Ship>();
  private Group root; // Used to update UI

  public double MUTATION_THRESHOLD = 0.2; // How much to mutate the neural networks of agents' by

  // Set instance variables, create the agents, and copy those into the active agents
  public Learner(int numAgents, int numAsteroids, Group root) {
    this.root = root;
    for(int i = 0; i < numAgents; i++) createAgent(numAsteroids);
    this.activeAgents = (ArrayList<Ship>)this.agents.clone();
  }

  //Each created agent needs to be added to the agents ArrayList, and to the UI
  private void createAgent(int numAsteroids) {
    Ship agent = new Ship(numAsteroids);
    agents.add(agent);
    this.root.getChildren().add(agent);
  }

  // Genetic Algorithm - essentially sorts the agents by how well they did, keeps the best half, duplicates that half, and mutates the duplicated half
  public void geneticSelect() {
    // Sort agents by survivedFrames; ie., fitness
    Collections.sort(this.agents, (a1, a2) -> (a1.survivedFrames > a2.survivedFrames) ? 1 : 0);
    // Rewrite the neural networks of the worst half of the group
    for(int i = 0; i < 15; i++) {
      this.agents.get(29 - i).network = this.agents.get(i).network;
    }
    // Add random mutations to the lower half
    for(int i = 0; i < 15; i++) this.agents.get(i).network.mutate(MUTATION_THRESHOLD);
    
    //At the end of a generation, center all the agents and reset thier scores/survivedFrames
    for(Ship agent : this.agents) {
      agent.center();
      agent.survivedFrames = 0;
    }
    
    this.activeAgents = (ArrayList<Ship>)this.agents.clone(); // Add all agents to the activeAgents for the next generation
  }

  // ** FRAME UPDATE FOR SHIPS
  public boolean updateAll(ArrayList<Asteroid> asteroids) {
    // Create an ArrayList of agents to remove - removing inside the loop causes an error
    ArrayList<Ship> agentsToRemove = new ArrayList<Ship>();
    
    //For each ACTIVE agent, update it (method in Ship.java), and make sure it stays blue since it is "alive"
    for(Ship agent : this.activeAgents) {
      agent.update(asteroids);
      agent.setFill(Color.BLUE);

      // Check for any collisions, if so, add the agent to the removal list, and set its color to red to show that it is "dead"
      if(agent.getCollisions(asteroids)) {
        agentsToRemove.add(agent);
        agent.setFill(Color.RED);
      }
    }

    // Remove agents that need to be removed
    for(Ship agent : agentsToRemove) this.activeAgents.remove(agent);

    // Return false if all the agents are "dead", so the Ecosystem wrapper class knows to move on to the next generation, otherwise return true
    if(this.activeAgents.size() == 0) return false;
    else return true;
  }
  
}