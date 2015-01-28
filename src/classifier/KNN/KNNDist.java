package classifier.KNN;

import java.util.List;

/**
 * Distance measure for features
 */
public abstract class KNNDist {

	/**
	 * Returns the distance between two instances.
	 */
	public abstract double dist( KNNInstance instanceA, 
								 KNNInstance instanceB, 
								 List<Double> stdDev, 
								 List<Double> means );
	
	/**
	 * If the measure require the stdDev and means statistic
	 */
	public abstract boolean requiresStats();
}
