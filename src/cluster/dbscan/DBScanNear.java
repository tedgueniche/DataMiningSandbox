package cluster.dbscan;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * Class the implements the DBScan algorithm, without modifications
 * We introduced the concept of Near point which are noisy by definition
 * but close to a specific cluster. It servers the purpose of reducing the number
 * of noisy points
 */
public class DBScanNear {

	private HashMap<Integer, DBPoint> dataset;
	private double eps;
	private double epsNear;
	private int minPoints;
	
	/**
	 * 
	 * @param dataset  Points to form into clusters. The given set itself will be unchanged but the points in the set will be modified to reflect their clusters.
	 * @param eps The size of the epsilon neighborhood to use.
	 * @param epsNear The size of the epsilon neighborhood for near points
	 * @param minPoints Minimal number of points a point needs in its epsilon neighborhood to be considered a seed.
	 */
	public DBScanNear(HashMap<Integer, DBPoint> dataset, double eps, double epsNear, int minPoints) {
		this.dataset = dataset;
		this.eps = eps;
		this.epsNear = epsNear;
		this.minPoints = minPoints;
	}

	/**
	 * This is the primary DBScan method that discovers seeds.
	 */
	public void scan() {
		//first cluster id
		int clusterID = 1;

		// Clear the points for this fresh run.
		for (DBPoint point : dataset.values()) {
			point.resetSelf();
		}
		
		
		//for each unvisited points in the dataset
		for(DBPoint curPoint : dataset.values()) {
			if(curPoint.isVisited() == false) {
			
				//sets the point to visited
				curPoint.setVisited(true);
				
				//if the point does not qualify we treat it as noise
				if(curPoint.regionQuery(dataset.values(), eps).size() < minPoints) {
					curPoint.setCluster(DBPoint.Undefined);
					curPoint.setType(DBPoint.Noise);
				}
				//else generate a cluster with this point
				else {
					//create a cluster
					curPoint.setCluster(clusterID);
					
					//expand the cluster
					expandCluster(curPoint, clusterID);
					
					//Increment the cluster id for the next cluster
					clusterID++;
				}
			}
		}
		
		//Detect the nearPoints
		setNearPoints();
	}
	
	/**
	 * Implementation for the EXPAND-CLUSTER routine in the DBSCAN algorithm.
	 * 
	 * This takes a seed point for a cluster, collectors points in its epsilon
	 * neighborhood, adds them to the current cluster, and adds any with a dense
	 * neighborhood as seed points.
	 * 
	 * @param seed Seed to use for clustering
	 * @param clusterID The current cluster's number.
	 */
	private void expandCluster(DBPoint seed, int clusterID) {
		
		//For each candidate
		LinkedList<DBPoint> candidates = new LinkedList<DBPoint>(seed.regionQuery(dataset.values(), eps));
		while(candidates.size() > 0) {
			
			//extracting the candidate
			DBPoint candidate = dataset.get(candidates.removeFirst().id);
			
			//if the candidate has not been visited we try to add its neighbors
			if(candidate.isVisited() == false) {
				
				candidate.setVisited(true);
				
				Set<DBPoint> neighbors = candidate.regionQuery(dataset.values(), eps);
				if(neighbors.size() < minPoints) {
					candidate.setType(DBPoint.BorderPoint);
				}
				else {			
					candidate.setType(DBPoint.CorePoint);
					candidates.addAll(neighbors);
				}
				
			}
			
			//Adding this candidate in this cluster only if it does not belong to another cluster
			if(candidate.getCluster() == DBPoint.Undefined) {
				candidate.setCluster(clusterID);
			}
		}
	}
	
	/**
	 * Detects the noisy point that should be considered near of a cluster (nearPoints)
	 */
	private void setNearPoints() {
		
		//for each noisy points in the dataset
		for(DBPoint curPoint : dataset.values()) {
			if(curPoint.getType() == DBPoint.Noise) {
				
				//Get the neighborhood of this noisy point
				Set<DBPoint> neighbors = curPoint.regionQuery(dataset.values(), epsNear);
				
				//Find close clusters from this point
				int clusterId = DBPoint.Undefined;
				for(DBPoint neighbor : neighbors) {
					
					if(neighbor.getType() != DBPoint.Noise) {
						clusterId = neighbor.getCluster();
					}
				}
				
				//Assign a cluster id if it found one
				if(clusterId != DBPoint.Undefined) {
					curPoint.setCluster(clusterId);
					curPoint.setType(DBPoint.NearPoint);
				}
				
			}
		}
		
	}
	
}
