package cluster.dbscan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * A grouping of points that constitute a keyboard measurement.
 */
public class DBPoint {	
	
	
	public static final int Undefined = -2;
	
	/**
	 * Types of point
	 */
	public static final int Noise = -1;
	public static final int CorePoint = 1;
	public static final int BorderPoint = 2;
	public static final int NearPoint = 3;
	
	public Integer id = -1;
	
	/**
	 * For testing purposes!!!
	 */
	public Integer real_clusterNum = null;
	
	/**
	 * Used by DBScan
	 */
	private boolean visited = false;
	
	/**
	 * Cluster ID, Undefined by default
	 */
	private int clusterNum = Undefined;
	
	/**
	 * Point type, can by a core, border, near or noise  
	 */
	private int type = Noise;
	
	/**
	 * Raw data points
	 */
	public ArrayList<Double> values = new ArrayList<>();
	
	/**
	 * Contains the neighborhood of this DBPoint
	 */
	private Set<DBPoint> neighborhood = new HashSet<>();
	
	
	public DBPoint(Integer id) {
		this.id = id;
	}
	
	public DBPoint(Integer id, String written){
		
		this.id = id;
		
		String[] stringValues = written.split("\t");
		
		for(String value : stringValues) {
			values.add(new Double(value));
		}
	}
	
	@Override
	public int hashCode(){
		return values.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		return o.hashCode() == this.hashCode();
	}
	
	/**Tells this point what cluster it belongs to, wiping any memory it has of a previous cluster membership*/
	public void setCluster(int newCluster) {
		clusterNum=newCluster;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	/**Tells this point it has been visited*/
	public void setVisited(boolean setTo) {
		visited=setTo;		
	}

	public boolean isVisited() {
		return visited;
	}

	/**
	 * Asks the point to reset its internal condition so that it may be used for fresh clustering.
	 * 
	 */
	public void resetSelf() {
		this.setVisited(false);
		this.setCluster(Undefined);
	}
	
	/**Get's the cluster this point has been assigned to*/
	public int getCluster() {
		return this.clusterNum;
	}
	
	public int getType() {
		return this.type;
	}

	/**
	 * Returns all points, excluding oneself, that are in the epsilon neighborhood of this point.
	 * 
	 * @param dataset Points to consider
	 * @return
	 * 
	 * @see #distanceTo(DBPoint) Note the distance metric has a direct impact on the choice of the neighborhood size that should be submitted
	 */
	public Set<DBPoint> regionQuery(Collection<DBPoint> dataset, double eps) {
		
		if(neighborhood.size() > 0) {
			return neighborhood;
		}
		
		//Brute-force search. Check every point to see if they are close.
		//The original implementation and others optimizations so that only log(n) checks need to be made, not n like below.
		for(DBPoint point : dataset){
			
			if(this != point && eps >= this.distanceTo(point)){
				neighborhood.add(point);
			}
		}
		return neighborhood;
	}

	/**Returns the distance between this point and another
	 * @see #regionQuery(Set, double) Note the distance metric has a direct impact on the choice of the neighborhood size that should be submitted
	 * */
	public double distanceTo(DBPoint point) {
		
		//if these points are of difference dimensions, the longer is much bigger*/
		int sizeDiff= point.values.size() - this.values.size();
		if(sizeDiff!=0){
			if(sizeDiff>0){
				return Double.MAX_VALUE;
			}else{
				return (-1) * Double.MAX_VALUE;
			}
		}
		
		//else let's do a unit by unit comparison in Euclidean space.
		double distance=0;
		for(int curField=0;curField<values.size();curField++){
			Double selfPart=this.values.get(curField);
			Double compPart=point.values.get(curField);
			double diff=(selfPart-compPart);
			distance=distance+diff*diff;
		}
		
		return Math.sqrt(distance);
	}
	
	
	public String toString() {
//		return "id: "+ id + ", clusterNum: "+ clusterNum + ", near: "+ neighborhood.toString() + ", points: "+ values.toString();
		return "id: "+ id + ", type: "+ type + ", clusterNum: "+ clusterNum + ", points: "+ values.toString();
	}
	
	
	/**
	 * Output this DBPoint as a JSON string
	 */
	public String toJsonString() {
		
		String json = "{\"patternId\": \""+ id + "\", \"type\": \""+ getType() +"\", \"pts\" :[";
		
		for(Double value : values) {
			json += value.toString() + ",";
		}
		
		json = json.substring(0,json.length() -1);
		json += "]}";
		
		return json;
	}
}
