package cluster.dbscan;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * Class the implements the DBScan algorithm, without modifications. For a
 * reference the algorithm, refer to Wikipedia's article on <a
 * href="http://en.wikipedia.org/wiki/DBSCAN">DBSCAN</a>
 */
public class DBScan {

	public DBScan() {
	}

	/**
	 * This is the primary DBScan method that discovers seeds.
	 * 
	 * @param points
	 *            Points to form into clusters. The given set itself will be
	 *            unchanged but the points in the set will be modified to
	 *            reflect their clusters.
	 * @param eps
	 *            The size of the epsilon neighborhood to use.
	 * @param minPoints
	 *            Minimal number of points a point needs in its epsilon
	 *            neighborhood to be considered a seed.
	 */
	public void scan(HashMap<Integer, DBPoint> dataset, double eps, int minPoints) {
		int clusterID = 1;

		// Clear the points for this fresh run.
		for (DBPoint point : dataset.values()) {
			point.resetSelf();
		}

		//for each non visited point
		for (DBPoint curPoint : dataset.values()) {
			
			if(curPoint.isVisited() == false) {
				
				//sets the point to visited and extract its neighbors
				curPoint.setVisited(true);
				Set<DBPoint> neighbors = curPoint.regionQuery(dataset.values(), eps);
				
				//if the point does not qualify we treat it as noise
				if (neighbors.size() < minPoints) {
					curPoint.setCluster(DBPoint.Noise); //set point to noise
				} 
				//else generate a cluster
				else {
						expandCluster(dataset, curPoint, neighbors, clusterID, eps, minPoints);
						clusterID++;
				}
			}
		}
	}
	

	/**
	 * Implementation for the EXPAND-CLUSTER routine in the DBSCAN algorithm.
	 * 
	 * This takes a seed point for a cluster, collectors points in its epsilon
	 * neighborhood, adds them to the current cluster, and adds any with a dense
	 * neighborhood as seed points.
	 * 
	 * @param dataset Points to consider adding to the current cluster
	 * @param seed Seed to use for clustering
	 * @param neighbors The seeds' neighborhood (which should include itself)
	 * @param clusterID The current cluster's number.
	 * @param neighborhoodSize The size of a point's neighborhood
	 * @param minPoints Minimal number of points needed in a point's neighborhood needed for that neighborhood to be considered dense.
	 */
	private void expandCluster(HashMap<Integer,DBPoint> dataset, DBPoint seed, Set<DBPoint> neighbors, int clusterID, double eps, int minPoints) {
		
		//Setting the seed's cluster
		seed.setCluster(clusterID);
		
		//For each neightbor
		LinkedList<DBPoint> neighborList = new LinkedList<DBPoint>(neighbors);
		while(neighborList.size() > 0) {
			
			//If the neighbor has not been see yet
			DBPoint neighbor = neighborList.removeFirst();
			neighbor = dataset.get(neighbor.id);

			
			if(neighbor.isVisited() == false) {
				
				neighbor.setVisited(true);
				
				//list the neighbors of this neighbor
				//if it qualitfy then we add them to the list to process
				Set<DBPoint> candidateNeighbors = neighbor.regionQuery(dataset.values(), eps);
				if(candidateNeighbors.size() >= minPoints) {
					neighborList.addAll(candidateNeighbors);
				}
			}
			
			//Adding this candidate to the cluster if is does not belong to an existing cluster
			if(neighbor.getCluster() == DBPoint.Undefined || neighbor.getCluster() == DBPoint.Noise) {
				neighbor.setCluster(clusterID);
			}
		}
	}
}