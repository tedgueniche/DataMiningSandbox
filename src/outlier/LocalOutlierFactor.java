package outlier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

import utilities.SV;

public class LocalOutlierFactor {

	
	/**
	 * How many neighbors are considered
	 */
	public int k;
	
	/**
	 * Distance matrix (supposed to be triangular)
	 */
	//public Double[][] distances;
	
	/***/
	public HashMap<SV, List<SV>> kNeighbors = new HashMap<>();
	
	private final BiFunction<SV, SV, Double> distanceMeasure;
	
	/**
	 * Dataset used for LOF.
	 */
	public List<SV> dataset;

	/**
	 * 
	 * @param k Neighbour count to use for kNN
	 * @param dataset Assumed non-empty and that all LOFPoint have the same number and quality of dimensions
	 * @param distanceFunction The function to use to determine the distance between two points
	 */
	public LocalOutlierFactor(int k, List<SV> dataset, BiFunction<SV, SV, Double> distanceFunction) {
		
		this.k = k;
		this.dataset = dataset;
		this.distanceMeasure = distanceFunction;
		//this.distances = new Double[dataset.size()][dataset.size()];
		final int dimensionCount = dataset.get(0).getCount();
	}
	
	public HashMap<SV, Double> LOF() {
		
		HashMap<SV, Double> results = new HashMap<SV, Double>();
		
		for(SV point : dataset) {
			results.put(point, LOF(point));
		}
		
		return results;
	}
	
	
	/**
	 * Calculate the local outlier factor of a given point
	 */
	private Double LOF(SV point) {
	
		//Calculating the total local reachability density of the k nearest neighbors of this point
		Double lof = 1d;
		for(SV neighbor : nearestNeighbors(k, point, dataset)) {
			
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
	private Double localReachabilityDensity(SV point) {
		
		//Calculate the total reachability based on the k nearest neighbors
		Double lrd = 0d;
		for(SV neighbor : nearestNeighbors(k, point, dataset)) {
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
	private Double reachabilityDistance(SV A, SV B) {
		
		Double kDistanceB = kDistance(B, nearestNeighbors(k, B, dataset));
		Double distAB = distanceMeasure.apply(A, B);
		
		return Math.max(kDistanceB, distAB);
	}
	
	
	/**
	 * Return the K distance of sv in this dataset
	 */
	private Double kDistance(SV point, List<SV> kneighbors) {
		return distanceMeasure.apply(point,  kneighbors.get(kneighbors.size() - 1));
	}
	
	
	/**
	 * Gets the K nearest neighbors of sv
	 */
	public List<SV> nearestNeighbors(int k, SV point, List<SV> dataset) {
		
		//If the kneighbors have not been recorded, calculate
		if(!kNeighbors.containsKey(point)) {
			
			List<SV> curNeighbors = new ArrayList<SV>(dataset);
			curNeighbors.remove(point);
			
			//Calculate and recorded distances. Sort points based on their distance to the current point.
			HashMap<SV, Double> thisToOtherDistance = new HashMap<SV, Double>();
			for(SV pt : curNeighbors) {	
				thisToOtherDistance.put(pt, distanceMeasure.apply(point, pt));
			}
			Collections.sort(curNeighbors, (l1, l2) -> (int) Math.signum(thisToOtherDistance.get(l1) - thisToOtherDistance.get(l2)));
			curNeighbors.subList(k, curNeighbors.size()).clear();
			
			//Now put the kneighbors in the map
			kNeighbors.put(point, curNeighbors);
		}
		
		return kNeighbors.get(point);
	}
	
}
