// An edge connects 2 neurons, and has a weight which is adjusted each generation
// Only the end neuron that the edge touches is stored, as the neuron holds the edges it connects to forward
public class Edge {
	public double weight;
	public Neuron endNeuron;
	
	public Edge(double weight, Neuron endNeuron) {
		this.weight = weight;
		this.endNeuron = endNeuron;
	}
}
