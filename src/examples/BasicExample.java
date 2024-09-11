package examples;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import sampling.Sampler;
import sampling.AliasTable;
import sampling.ProposalArray;

public class BasicExample {
	// Perform an experiment with the given PRNG, sampler and output the results.
	private static void doExperiment(Random random, Sampler sampler) {
		// Produce a number of samples and record the counts.
		int num_samples = 10000;
		HashMap<Integer, Integer> counts = new HashMap<>();
		for (int i = 0; i < num_samples; ++i) {
			int element = sampler.sample(random);
			int old_count = counts.getOrDefault(element, 0);
			counts.put(element, old_count + 1);
		}

		// Output the results.
		String sampler_name = sampler.getName();
		System.out.println(sampler_name);
		counts.entrySet().stream()
			.map(e -> e.getKey() + " : " + e.getValue())
			.forEach(System.out::println);
	}

	public static void main(String[] args) {
		// Initialize PRNG.
		Random random = new Random(); //new Random(0);
		
		// Initialize example weights.
		List<Double> weights = List.of(50.0, 15.0, 1.0, 25.0, 9.0);
		
		// Create a Proposal Array and Alias Table for the example weights.
		Sampler proposal_array = new ProposalArray(weights);
		
		// Perform the experiment with both.
		doExperiment(random, proposal_array);
	}

}
