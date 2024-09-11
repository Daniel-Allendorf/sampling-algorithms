package sampling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/*
 * Static data structure for constant-time sampling from an arbitrary probability distribution.
 */
public class ProposalArray implements Sampler {
	protected List<Integer> elements;
	protected List<Double> remainders;
	
	// Construct a proposal array for the given weights.
	public ProposalArray(List<Double> weights) {
		// Compute basic properties of the given weights.
		double total_weight = weights.stream().reduce(0.0, Double::sum);
		int size = weights.size();
		double average_weight = total_weight / size;
		
		// Initialize data structures.
		elements = new ArrayList<>();
		remainders = new ArrayList<Double>(Collections.nCopies(size, 0.0));
		
		// Fill elements with appropriate copies of the elements and compute remainders.
		for (int i = 0; i < size; ++i) {
			double weight = weights.get(i);
			double relative_weight = weight / average_weight;
			int count = (int) Math.floor(relative_weight);
			double remainder = relative_weight - count;
			for (int j = 0; j < count; ++j) {
				elements.add(i);
			}
			remainders.set(i, remainder);
		}
	}

	@Override
	public int sample(Random random) {
		// Keep trying until a sample is accepted.
		int element = 0;
		boolean accepted = false;
		do {
			// Propose an index.
			int proposal = random.nextInt(0, elements.size() + remainders.size());
			if (proposal < remainders.size()) {
				// If the index has a remainder, accept with probability equal to the remainder.
				double coin = random.nextDouble();			
				double remainder = remainders.get(proposal);
				if (coin < remainder) {
					element = proposal;
					accepted = true;
				}
			} else {
				// If the index has no remainder, just accept the corresponding element.
				element = elements.get(proposal - remainders.size());
				accepted = true;
			}
		} while (!accepted);
		return element;
	}

	@Override
	public String getName() {
		return "Proposal Array";
	}
}
