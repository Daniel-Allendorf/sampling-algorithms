package sampling;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * Walker's Alias Table.
 */
public class AliasTable implements Sampler {
	protected class TableEntry {
		int index;
		int alias;
		double threshold;
		
		public TableEntry(int index, int alias, double threshold) {
			this.index = index;
			this.alias = alias;
			this.threshold = threshold;
		}
	}
	
	protected List<TableEntry> table = new ArrayList<>();

	// Construct an alias table for the given weights.
	public AliasTable(List<Double> weights) {
		// Compute basic properties of the given weights.
		double total_weight = weights.stream().reduce(0.0, Double::sum);
		int size = weights.size();
		
		// Initialize lists for below/above average weights.
		List<Integer> below = new ArrayList<>();
		List<Integer> above = new ArrayList<>();
		
		// Partition weights into below/above and initialize table entries.
		for (int i = 0; i < size; ++i) {
			double weight = weights.get(i);
			double threshold = size * (weight / total_weight);
			if (threshold < 1.0) {
				below.add(i);
			} else if (threshold > 1.0) {
				above.add(i);
			}
			// In the rare case that threshold == 1.0, this already sets the table entry correctly.
			table.add(new TableEntry(i, i, threshold));
		}
		
		// Match up below with above average weights to properly set the table entries.
		while ((!below.isEmpty()) && (!above.isEmpty())) {
			// Get entries corresponding to the first indices in above/below.
			TableEntry entry_below = table.get(below.removeFirst());
			TableEntry entry_above = table.get(above.removeFirst());
			
			// Add index_above as alias of index_below and adjust its threshold.
			entry_below.alias = entry_above.index;
			entry_above.threshold += entry_below.threshold - 1.0;
			
			// Re-assign index_above to above/below based on its adjusted threshold.
			if (entry_above.threshold < 1.0) {
				below.add(entry_above.index);
			} else if (entry_above.threshold > 1.0) {
				above.add(entry_above.index);
			}
		}
	}

	@Override
	public int sample(Random random) {
		int index = random.nextInt(0, table.size());
		double coin = random.nextDouble();
		TableEntry entry = table.get(index);
		return (coin < entry.threshold) ? entry.index : entry.alias;
	}

	@Override
	public String getName() {
		return "Alias Table";
	}
}
