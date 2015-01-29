package classifier.ID3;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of attributes for the classifier
 */
public class ID3Instance {

	/**
	 * The features we hold
	 */
	private List<Double> features;
	private Object referenceObject; 

	/**
	 * Basic CTor
	 */
	public ID3Instance() {
		this.features = new ArrayList<Double>();
		this.referenceObject = null;
	}
	
	/**
	 * CTor with features to copy
	 */
	public ID3Instance( List<Double> features ) {
		this.features = new ArrayList<Double>(features);
		this.referenceObject = null;
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
}
