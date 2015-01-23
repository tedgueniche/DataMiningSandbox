package outlier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LocalOutlierFactor {

	
	/**
	 * How many neighbors are considered
	 */
	public int k;
	
	/**
	 * Distance matrix (supposed to be triangular)
	 */
	public Double[][] distances;
	
	/**
	 * 
	 */
	public HashMap<LOFPoint, List<LOFPoint>> kNeighbors;
	
	
	/**
	 * Dataset used for LOF
	 */
	public List<LOFPoint> dataset;

	
	public LocalOutlierFactor(int k, List<LOFPoint> dataset) {
		
		this.k = k;
		this.dataset = dataset;
		this.distances = new Double[dataset.size()][dataset.size()];
	}
	
	public HashMap<LOFPoint, Double> LOF() {
		
		HashMap<LOFPoint, Double> results = new HashMap<LOFPoint, Double>();
		
		for(LOFPoint point : dataset) {
			results.put(point, LOF(point));
		}
		
		return results;
	}
	
	
	/**
	 * Calculate the local outlier factor of a given point
	 */
	private Double LOF(LOFPoint point) {
	
		//Calculating the total local reachability density of the k nearest neighbors of this point
		Double lof = 1d;
		for(LOFPoint neighbor : nearestNeighbors(k, point, dataset)) {
			
			lof += localReachabilityDensity(neighbor);
		}
		
		//Dividing by the number of neighbors (usually k)
		lof = lof / nearestNeighbors(k, point, dataset).size();
		
		//Dividing by the local reachability of this point
		lof = lof / localReachabilityDensity(point);
		
		return lof;
	}
	
	
	/**
	 * Local reachability density of a point based on the reachability of this point and its k nearest neighbors
	 */
	private Double localReachabilityDensity(LOFPoint point) {
		
		//Calculate the total reachability based on the k nearest neighbors
		Double lrd = 0d;
		for(LOFPoint neighbor : nearestNeighbors(k, point, dataset)) {
			lrd += reachabilityDistance(point, neighbor);
		}
		
		//divide the total reachability distance by the number of neighbors (usually K)
		lrd = lrd / nearestNeighbors(k, point, dataset).size();
		
		lrd = 1 / lrd;
		
		return lrd;
	}
	
	
	/**
	 * Reachability distance between two points as described in the Local Outlier Factor
	 */
	private Double reachabilityDistance(LOFPoint A, LOFPoint B) {
		
		Double kDistanceB = kDistance(B, nearestNeighbors(k, B, dataset));
		Double distAB = dist("euclidian", A, B);
		
		return Math.max(kDistanceB, distAB);
	}
	
	
	/**
	 * Return the K distance of sv in this dataset
	 */
	private Double kDistance(LOFPoint point, List<LOFPoint> kneighbors) {
		return dist("euclidian", point, kneighbors.get(kneighbors.size() - 1));
	}
	
	
	/**
	 * Gets the K nearest neighbors of sv
	 */
	public List<LOFPoint> nearestNeighbors(int k, LOFPoint point, List<LOFPoint> dataset) {
		
		//If the kneighbors have not been recorded, calculate
		if(kNeighbors.containsKey(point)) {
			
			List<LOFPoint> curNeighbors = new ArrayList<LOFPoint>(dataset);
			curNeighbors.remove(point);
			
			//Calculate and recorded distances. Sort points based on their distance to the current point.
			HashMap<LOFPoint, Double> distances = new HashMap<LOFPoint, Double>();
			for(LOFPoint pt : curNeighbors) {	
				distances.put(pt, dist("euclidian", point, pt));
			}
			Collections.sort(curNeighbors, (l1, l2) -> (int) Math.signum(distances.get(l1) - distances.get(l2)));
			curNeighbors.subList(k, curNeighbors.size()).clear();
			
			//Now put the kneighbors in the map
			kNeighbors.put(point, curNeighbors);
		}
		
		return kNeighbors.get(point);
	}
	
	
	public static Double dist(String distType, LOFPoint A, LOFPoint B) {
		
		if(distType == "euclidian") {
			return distEuclidian(A,B);
		}
		else if(distType ==  "pearson") {
			return distPearson(A,B);
		}
		else {
			return null;
		}
	}
	
	
	public static Double distPearson(LOFPoint A, LOFPoint B) {
		
		return 0d;
		
	}
	
	
	/**
	 * Euclidian distance between A and B
	 */
	public static Double distEuclidian(LOFPoint A, LOFPoint B) {
		
		//if these points are of difference dimensions, the longer is much bigger*/
		int sizeDiff= A.values.size() - B.values.size();
		if(sizeDiff!=0){
			if(sizeDiff>0){
				return Double.MAX_VALUE;
			}else{
				return (-1) * Double.MAX_VALUE;
			}
		}
		
		//else let's do a unit by unit comparison in Euclidean space.
		double distance=0;
		for(int featureId = 0; featureId < A.values.size(); featureId++){
			
			Double diff = A.values.get(featureId) - B.values.get(featureId);
			
			distance = distance + diff * diff;
		}
		
		return Math.sqrt(distance);
	}
	
}
