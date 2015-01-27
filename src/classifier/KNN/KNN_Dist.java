package classifier.KNN;

import java.util.List;

/**
 * Distance measure for features
 */
public abstract class KNN_Dist {

	/**
	 * Returns the distance between two instances.
	 */
	public abstract double dist( KNN_Instance instanceA, 
								 KNN_Instance instanceB, 
								 List<Double> stdDev, 
								 List<Double> means );
	
	/**
	 * If the measure require the stdDev and means statistic
	 */
	public abstract boolean requiresStats();
}
