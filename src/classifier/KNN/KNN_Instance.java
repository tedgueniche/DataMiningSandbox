package classifier.KNN;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used in conjunction with the KNN Algorithm.
 * It holds the features in N dimensions
 */
public class KNN_Instance {
	
	/**
	 * The features we hold
	 */
	private List<Double> features;
	private Object referenceObject; 
	private Double distance;

	/**
	 * Basic CTor
	 */
	public KNN_Instance() {
		this.features = new ArrayList<Double>();
		this.referenceObject = null;
		this.distance = 0.0;
	}
	
	/**
	 * CTor with features to copy
	 */
	public KNN_Instance( List<Double> features ) {
		this.features = new ArrayList<Double>(features);
		this.referenceObject = null;
		this.distance = 0.0;
	}
	
	/**
	 * Set and get a reference object
	 */
	public void setRef( Object ref ) {
		referenceObject = ref;
	}
	
	/**
	 * Set and get a reference object
	 */
	public Object getRef() {
		return (referenceObject);
	}
	
	/**
	 * Number of feature we hold
	 */
	public int size() {
		return (features.size());
	}
	
	/**
	 * Get the feature at position 'pos'
	 */
	public double get( int pos ) {
		return (features.get(pos));
	}
	
	/**
	 * Add a feature at the end of the array
	 */
	public void add( Double feature ) {
		features.add(feature);
	}
	
	/**
	 * Distance to the element to get KNN from
	 */
	public Double getDistance() {
		return (distance);
	}

	/**
	 * Distance to the element to get KNN from
	 */
	public void setDistance(Double distance) {
		this.distance = distance;
	}
}
