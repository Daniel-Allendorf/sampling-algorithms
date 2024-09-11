package sampling;

import java.util.Random;

public interface Sampler {
	public int sample(Random random);
	
	default String getName() {
		return this.getClass().getName();
	}
}
