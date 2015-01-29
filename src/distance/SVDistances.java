package distance;

import utilities.SV;

/**A class containing a set of distance measures for SV vectors*/
public class SVDistances {
		
	/**
	 * Calculate distance of two points in the dataset <a href="http://bonsai.hgc.jp/~mdehoon/software/cluster/manual/Distance.html">based on the pearson correlation</a>. These assumes that the points are in the dataset.
	 * 
	 * The distance dist(a,b) == dist(b,a)
	 * 
	 * @param A First in comparison
	 * @param B Second in comparison
	 * @return A distance measure between the two given points.
	 * 
	 */
	public static Double distPearson(SV A, SV B) {
		final int dimensionCount = A.size();
		double runningDistance = 0;
		for(int dimension = 0; dimension < dimensionCount; dimension++){
			double xIComponent = (A.get(dimension) - A.getMean())/A.getSD();
			double yIComponent = (B.get(dimension) - B.getMean())/B.getSD();
			runningDistance += (xIComponent * yIComponent);
		}
		
		return runningDistance/dimensionCount;
	}
	
	/**Returns the city-block (manhattan, taxicab) distance of two points in the dataset. 
	 * 
	 * @param A First in comparison
	 * @param B Secodn in comparison
	 * @return The manhattan distance
	 */
	public static Double distManhattan(SV A, SV B) {
		final int dimensionCount = A.size();
		double runningDistance = 0;
		for(int dimension = 0; dimension < dimensionCount; dimension++){
			double xIComponent = A.get(dimension);
			double yIComponent = B.get(dimension);
			runningDistance += Math.abs(xIComponent - yIComponent);
		}
		
		return runningDistance/dimensionCount;
		
	}
	
	
	/**
	 * Euclidian distance between A and B
	 */
	public static Double distEuclidian(SV A, SV B) {
		
		//if these points are of difference dimensions, the longer is much bigger*/
		int sizeDiff= A.size() - B.size();
		if(sizeDiff!=0){
			if(sizeDiff>0){
				return Double.MAX_VALUE;
			}else{
				return (-1) * Double.MAX_VALUE;
			}
		}
		
		//else let's do a unit by unit comparison in Euclidean space.
		double distance=0;
		for(int featureId = 0; featureId < A.size(); featureId++){
			
			Double diff = A.get(featureId) - B.get(featureId);
			
			distance = distance + diff * diff;
		}
		
		return Math.sqrt(distance);
	}
}
