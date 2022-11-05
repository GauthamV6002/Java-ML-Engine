import java.util.ArrayList;

// Stores multiple neurons
public class Layer {
	
	public ArrayList<Neuron> neurons = new ArrayList<Neuron>();
	
	public Layer(int size) {
		for(int i = 0; i < size; i++) {
			this.neurons.add(new Neuron(Math.random()));
		}
	}
}
