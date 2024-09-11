This repository contains implementations of two constant-time sampling algorithms in Java.

src/sampling/AliasTable.java - Implementation of Walker's Alias Table
src/sampling/ProposalArray.java - Implementation of the static Proposal Array

# Usage Example
```
// Initialize PRNG.
Random random = new Random();

// Initialize example weights.
List<Double> weights = List.of(50.0, 15.0, 1.0, 25.0, 9.0);

// Create samplers for the example weights.
Sampler proposal_array = new ProposalArray(weights);
Sampler alias_table = new AliasTable(weights);

// Perform an experiment with both samplers and output the results.
for (Sampler sampler : List.of(proposal_array, alias_table)) {
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
    System.out.println();
}
```

Sample output:
```
Proposal Array
0 : 5047
1 : 1488
2 : 86
3 : 2481
4 : 898

Alias Table
0 : 5012
1 : 1553
2 : 101
3 : 2435
4 : 899
```

See also: src/examples/BasicExample.java