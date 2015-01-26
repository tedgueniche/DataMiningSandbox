package cluster.optics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;


/**
 * Class the implements the OPTICS algorithm, without modifications
 * 
 */
public class Optics {
	List<DBPoint> list = new ArrayList<DBPoint>();

	/**
	 * This is the primary optics method that discovers seeds, considering
	 * more densely packed clusters than DBScan
	 * 
	 * @param dataset
	 *            Points to form into clusters. The given set itself will be
	 *            unchanged but the points in the set will be modified to
	 *            reflect their clusters.
	 * @param eps
	 *            The size of the epsilon neighborhood to use.
	 * @param minPoints
	 *            Minimal number of points a point needs in its epsilon
	 *            neighborhood to be considered a seed.
	 */
	public void optics(HashMap<Integer, DBPoint> dataset, double eps, int minPoints) {
		
		// for each point p of DB, set reachable distance to undefined
		for (DBPoint point : dataset.values()) {
			point.setReachabilityDistance(DBPoint.Undefined);
		}

		// for every unprocessed point p of DB
		for (DBPoint pointP: dataset.values()) {
			Set<DBPoint> neighborsN = pointP.getNeighbors(dataset.values(), eps);
			pointP.setProcessedPoint(true);
			list.add(pointP);
			if (pointP.getCoreDistance(list, eps, minPoints) != DBPoint.Undefined) {
				PriorityQueue<DBPoint> seeds = new PriorityQueue<DBPoint>(5, DBPointComparator);
				update(neighborsN, pointP, seeds, eps, minPoints);
				Iterator<DBPoint> iteratorQ = seeds.iterator();
				while (iteratorQ.hasNext()) {
					DBPoint pointQ = iteratorQ.next();
					Set<DBPoint> neighborsNPrime = pointQ.getNeighbors(dataset.values(), eps);
					pointQ.setProcessedPoint(true);
					list.add(pointQ);
					if (pointQ.getCoreDistance(list, eps, minPoints) != DBPoint.Undefined) {
						update(neighborsNPrime, pointQ, seeds, eps, minPoints);
					}
				}
				
			}
		}
	}

	public static Comparator<DBPoint> DBPointComparator = new Comparator<DBPoint>() {

		@Override
		public int compare(DBPoint o1, DBPoint o2) {
			return (int) o1.distanceTo(o2);
		}
		
	};
	
	/**
	 * Implementation for the update routine in the OPTICS algorithm.
	 * 
	 * This looks at neighborly distances and sets new reachability distances,
	 * adding points to the priority queue
	 * 
	 * @param neighborsN The point's neighborhood (which should include itself)
	 * @param pointP Points to consider adding to the current cluster
	 * @param eps The size of the epsilon neighborhood to use.
	 * @param seed Priority queue seeds to add
	 * @param minPoints Minimal number of points needed in a point's neighborhood needed for that neighborhood to be considered dense.
	 */
	private void update(Set<DBPoint> neighborsN, DBPoint pointP, PriorityQueue<DBPoint> seeds,
		double eps, int minPoints) {
		double coreDistance = pointP.getCoreDistance(list, eps, minPoints);
		Iterator<DBPoint> iteratorN = neighborsN.iterator();
		// add new reachable points and seeds in each neighborhood point
		while (iteratorN.hasNext()) {
			DBPoint pointO = iteratorN.next();
			if (!pointO.isProcessedPoint()) {
				double newReachDistance = maxDistance(coreDistance, pointP.distanceTo(pointO));
				if (pointO.getReachabilityDistance() == DBPoint.Undefined) {
					pointO.setReachabilityDistance(newReachDistance);
					seeds.offer(pointO);
				} else {
					if (newReachDistance < pointO.getReachabilityDistance()) {
						pointO.setReachabilityDistance(newReachDistance);
						seeds.add(pointO);
					}
				}
			}
		}
	}

	/**
	 * Calculate which is the maximum distance
	 * 
	 * @param coreDistance first distance
	 * @param distanceBetweenPoints second distance
	 */
	private double maxDistance(double coreDistance, double distanceBetweenPoints) {
		if (distanceBetweenPoints < coreDistance) {
			return coreDistance;
		}
		return distanceBetweenPoints;
	}

}
