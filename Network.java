import java.util.ArrayList;

public class Network {
	
	public ArrayList<Layer> layers; // List of all layers
  private double EDGE_START_RAND_THRESHOLD = 1.25; // Constant to adjust the starting values of the edge weights

  // Constructor sets up each layer by adding it to the layers ArayList and generating edges forward from it
	public Network(int[] layerSizes) {
		this.layers = new ArrayList<Layer>();
		for(int size : layerSizes) layers.add(new Layer(size));
		this.generateEdges();
	}

  // Write a set of new layers to this object
	public void writeNetwork(Layer[] newLayers) {
		for(int i = 0; i < this.layers.size(); i++) {
			this.layers.set(i, newLayers[i]);
		}
	}

  // For each layer, and then for each start neuron, and then for each end neuron, generate an edge with a random weight
	private void generateEdges() {
		// Iterate to SECOND LAST layer
		for(int i = 0; i < this.layers.size() - 1; i++) {
			for(Neuron startNeuron : this.layers.get(i).neurons) {
				for(Neuron endNeuron : this.layers.get(i+1).neurons) {
					Edge edge = new Edge(Math.random() * this.EDGE_START_RAND_THRESHOLD, endNeuron);
 					startNeuron.addConnectedEdge(edge); // Store the edge in the startNeuron
				}
			}
		}
	}

	// Run the network, storing the calculated values into each node's rtValue (real-time value)
  // Each neuron's rtValue is the sum of the products of all the neurons and edge weights connected to it
	public void calcNetwork() {
		for(int i = 0; i < this.layers.size() - 1; i++) {
			for(Neuron startNeuron : this.layers.get(i).neurons) {
				for(int endIndex = 0; endIndex < this.layers.get(i+1).neurons.size(); endIndex++) {
					Neuron endNeuron = this.layers.get(i+1).neurons.get(endIndex);
					endNeuron.rtValue = (startNeuron.rtValue * startNeuron.getEdgeToNode(endIndex).weight);
				}
			}
		}
	}

  // For each edge weight in the network, offset it slightly by a random # * some threshold
	public void mutate(double threshold) {
		for(int i = 0; i < this.layers.size() - 1; i++) {
			for(Neuron neuron : this.layers.get(i).neurons) {
				for(Edge edge : neuron.connectedEdgesForward) {
					edge.weight += (Math.random() * 2 - 1) * threshold;
				}
			}
		}
	}

  // Setter method to set the inputs of the network
  public void setInputs(ArrayList<Double> inputs) {
    for(int i = 0; i < inputs.size(); i++) {
			this.layers.get(0).neurons.get(i).rtValue = inputs.get(i);
		}
  }
	
	// Result Helpers -----------------------------

  // Just returns the "move", or the action that the neural network intends to use
  public int getMove() {
    double nodeValue = (this.layers.get(layers.size() - 1).neurons.get(1).rtValue);
    if(nodeValue >= -0.5 && nodeValue <= 0.5) { // Depending on the value of the final node, clamp that to three options: 0, -1, or 1 
      return 0;
    } else if(nodeValue <= -0.5) {
      return -1;
    } else {
      return 1;
    }
  }
	
	// DEBUG USE ONLY ------------------------------

  // Prints the network the console for debug usage
	public void printNetwork() {
    System.out.println("--------------------------------------------------");
		for(Layer layer : this.layers) {
			for(Neuron neuron : layer.neurons) {
				System.out.print(neuron.rtValue + ",\t");
			}
			System.out.println("");
			for(Neuron neuron : layer.neurons) {
				System.out.print(neuron.getConnectedEdgesValues() + "\t");
			}
			System.out.println("");
		}
    System.out.println("--------------------------------------------------");
	}

  // Also for debugging, just generates random inputs for the network
	public void genRandInputs() {
		for(Neuron neuron : this.layers.get(0).neurons) {
			neuron.rtValue = Math.random();
		}
	}
}
