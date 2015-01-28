package classifier.KNN;

import java.util.List;

public class KNNDistZScore extends KNNDist {

	/**
	 * Returns the distance between two instances.
	 * The distance is measured with the z score statistic.
	 */
	@Override
	public double dist( KNNInstance instanceA, 
						KNNInstance instanceB, 
						List<Double> stdDev, 
						List<Double> means ) {
		
		double distance = 0.0;
		for ( int feature = 0; feature < instanceA.size(); ++feature ) {
			double zScoreA = (instanceA.get(feature)-means.get(feature))/stdDev.get(feature);
			double zScoreB = (instanceB.get(feature)-means.get(feature))/stdDev.get(feature);
			distance += Math.abs(zScoreA-zScoreB);
		}
		distance /= (double) instanceA.size();
		return ( distance );
	}
	
	/**
	 * If the measure require the stdDev and means statistic
	 */
	@Override
	public boolean requiresStats() {
		return true;
	}
}
