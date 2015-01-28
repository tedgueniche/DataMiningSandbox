package classifier.KNN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * K Nearest Neighbors algorithm for classifying
 */
public class KNN {

	/**
	 * KNN Measures
	 */
	private int K;
	private KNNDist distance;
	private List<Double> stdDev;
	private List<Double> mean;
	
	/**
	 * The training set
	 */
	private List<KNNInstance> trainingInstances;
	
	/**
	 * Distance comparator for the KNN_Instances
	 */
	private Comparator<KNNInstance> distComparator = new Comparator<KNNInstance>() {
		@Override public int compare(KNNInstance o1, KNNInstance o2) {
			return (Double.compare(o1.getDistance(), o2.getDistance()));
		}
	};
	
	/**
	 * CTor with distance measure
	 */
	public KNN( KNNDist distance, int K ) {
		this.distance = distance;
		this.K = K;
	}
	
	/**
	 * Train the KNN Algorithm
	 * Input :	A List of KNN_Feature
	 * Output :	If training successful
	 */
	public void train( List<KNNInstance> trainingSet ) {
		
		// calculate statistics measures
		if ( distance.requiresStats() ) {
			getStats( trainingSet );
		} else {
			stdDev = null;
			mean = null;
		}
		
		// copy the instances
		trainingInstances = new ArrayList<KNNInstance>(trainingSet);
	}
	
	/**
	 * Find the closest neighbors
	 */
	public List<KNNInstance> findKNN( KNNInstance instance ) {
		
		// get all distances
		ArrayList<KNNInstance> distances = new ArrayList<KNNInstance>();
		double minDistance = Double.MAX_VALUE;
		boolean sortList = true;
		
		// check all neighbors
		for ( KNNInstance neighbor : trainingInstances ) {
			
			// Calculate distance form this object
			double dist = distance.dist(instance, neighbor, stdDev, mean);
			neighbor.setDistance(dist);
			
			// insertion sort on the distances
			if ( distances.size() < K ) {
				distances.add(neighbor);
			} else if ( dist < minDistance ) {
				sortList = false;
				distances.add(neighbor);
				Collections.sort(distances, distComparator);
				distances.remove(distances.size()-1);
				minDistance = distances.get(distances.size()-1).getDistance();
			}
		}
		
		// if we need to sort. Case is when if always land in ( distances.size() < K )
		if ( sortList == true ) {
			Collections.sort(distances, distComparator);
		}
		
		// return the list of neighbors sorted to distance
		return (distances);
	}
	
	/**
	 * calculate statistics on the training set
	 */
	private void getStats( List<KNNInstance> trainingSet ) {
			
		// init the arrays
		if ( trainingSet.size() == 0 ) {
			return;
		}
		int size = trainingSet.get(0).size();
		stdDev = new ArrayList<Double>(size);
		mean = new ArrayList<Double>(size);
			
		// For each feature get the stats
		for ( int feature = 0; feature < size; ++feature ) {
			
			// mean
			double calcMean = 0.0;
			for ( int instance = 0; instance < trainingSet.size(); ++instance ) {
				calcMean += trainingSet.get(instance).get(feature);
			}
			calcMean /= (double) trainingSet.size();
			mean.add(calcMean);
			
			// stdDev
			double variance = 0.0;
			for ( int instance = 0; instance < trainingSet.size(); ++instance ) {
				double meanValDiff = trainingSet.get(instance).get(feature) - mean.get(feature);
				variance += Math.pow(meanValDiff,2.0);
			}
			variance /= (double) trainingSet.size();
			double calcStdDev = Math.sqrt(variance);
			stdDev.add(calcStdDev);
		}
	}
	
}
