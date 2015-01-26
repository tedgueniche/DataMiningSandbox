package classifier.KNN;

import java.util.List;

public class KNN_Dist_Euclidean extends KNN_Dist {

	/**
	 * Returns the distance between two instances.
	 * The distance is measured with euclidean space.
	 * d(p,q) = sqrt((q1-p1)^2 + (q2-p2)^2 + ... + (qn-pn)^2)
	 */
	@Override
	public double dist( KNN_Instance instanceA, 
						KNN_Instance instanceB, 
						List<Double> stdDev, 
						List<Double> means ) {
		
		double ret = 0.0;
		for ( int i = 0; i < instanceA.size(); ++i ) {
			ret += Math.pow((instanceA.get(i)-instanceB.get(i)), 2.0);
		}
		ret = Math.sqrt(ret);
		return ( ret );
	}

	/**
	 * If the measure require the stdDev and means statistic
	 */
	@Override
	public boolean requiresStats() {
		return false;
	}

}
