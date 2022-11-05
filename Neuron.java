import java.util.ArrayList;

public class Neuron {

  public double value;
  public ArrayList<Edge> connectedEdgesForward; // Stores all the edges connected forward from this neuron

  public double rtValue; // Stores the value of a neuron, and used for calculations

  // Set/Initialize instance variables
  public Neuron(double initialValue) {
    this.value = initialValue;
    this.rtValue = 0;
    
    this.connectedEdgesForward = new ArrayList<Edge>();
  }

  // Add a edge that connects forward
  public void addConnectedEdge(Edge e) {
	this.connectedEdgesForward.add(e);  
  }

  // Grab a certain neuron using the index, just a helper for ease of use
  public Edge getEdgeToNode(int nodeIndex) {
	  return this.connectedEdgesForward.get(nodeIndex);
  }

  // Get an arraylist of all the connected edge values
  public ArrayList<Double> getConnectedEdgesValues() {
	  ArrayList<Double> edgeValues = new ArrayList<Double>();
	  for(Edge edge : this.connectedEdgesForward) edgeValues.add(edge.weight);
	  
	  return edgeValues;
  }
}