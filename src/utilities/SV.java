package utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.IntStream;



/**
 * An {@link #SV()} represents an ordered series of data.
 * 
 * Users of this class may uses it as an vector, where each entry is 'unrelated' to others, or a series of data. Say 1, 2, 4, 8, 16 to represent the first few terms of a geometric sequence.
 */
public class SV implements Serializable {

	private static final long serialVersionUID = 655635517094553855L;

	private Vector<Double> v;
	
	private int count;
	private double min;
	private double max;
	private double sum;
	private double mean;
	
	
	public SV() {
		clear();
	}
	
	public void clear() {
		v = new Vector<>();
		
		count = 0;
		min = Double.MAX_VALUE;
		max = Double.MIN_VALUE;
		sum = 0;
		mean = 0;
	}
	
	
	/**Returns the underlying vector as a list. Callers can mutate this return without affecting this vector*/
	public List<Double> getVector() {
		return new ArrayList<>(v);
	}
	
	/**Appends the given item to this vector. It is recommend not to provide large positive or negative values.
	 * 
	 * @param item Item to append
	 * @return True is the vector was changed by this addition
	 */
	public boolean append(Number item) {
		double rItem = item.doubleValue();
		boolean status = v.add(rItem);
		if(status == true) {
			
			if(rItem > max) {
				max = rItem;
			}
			
			if(rItem < min) {
				min = rItem;
			}
			
			sum += rItem;
			count ++;
			
			mean = sum / count;			
		}
		
		return status;
	}
	
	/**
	 * Returns the nth item in this vector. There is no check for whether this vector has an nth component.
	 * 
	 * @param index Item to get
	 * @return The nth value
	 */
	public Double get(int n) {
		return v.get(n);
	}

	/**
	 * The size of this vector (the number of dimensions)
	 * @return The number of dimensions or size of this vector
	 */
	public int size() {
		return v.size();
	}
	
	/**
	 * 
	 * Linearly scales the value between min and max used as normalization
	 * e.g.: for {-5,0,5} with scale(0, 1) -> {0, 0.5, 1)
	 * 
	 * @param scaleMin Min value
	 * @param scaleMax Max value
	 */
	public void scale(Double scaleMin, Double scaleMax) {
		
		//Scaling each value of this SV
		Vector<Double> scaledV = new Vector<Double>();

		for(Double vi : v) {
			
			Double scaled1 = (vi - min) / (max - min);
			Double scaled2 = (scaled1 * (scaleMax - scaleMin));
			
			scaledV.add(scaled2 + scaleMin);
		}
		
		//Recreating this current SV
		clear();
		for(Double vi : scaledV) {
			append(vi);
		}
	}
	
	/**Transforms this vector by transforming each value into its z-score
	 */
	public void scaleZScore() {
		
		//Scaling each value of this SV
		Vector<Double> scaledV = new Vector<Double>();
	
		Double sd = getSD();
		for(Double vi : v) {
			
			scaledV.add((vi - mean) / sd);
		}
		
		//Recreating this current SV
		clear();
		for(Double vi : scaledV) {
			append(vi);
		}
		
	}
	
	
	/**
	 * This transformation shifts the vector up or down than scales it. Specifically, it does:
	 * <ol>
	 * 	<li>
	 * 		Finds the biggest absolute value in vector, call it alpha
	 * 	</li>
	 * 	<li>
	 * 		Adds alpha to each item in the vector
	 *	</li>
	 *	<li>
	 *		Divides each item by twice alpha
	 *	</li>
	 * </ol>
	 * 
	 */
	public void scaleZMorph() {
		double maxAbs = Math.max(Math.abs(this.getMin()), Math.abs(this.getMax()));
		Vector<Double> transformed = new Vector<>();
		v.forEach( d -> transformed.add( (d + maxAbs)/(2* maxAbs)));
		clear();		
		transformed.forEach(d -> append(d));
	}
	
	
	/**
	 * Normalize the SV for values between 0 and 1
	 */
	public void normalize() {
		scale(0d,1d);
	}
	

	public Double getSum() {
		return sum;
	}
	
	public Double getMin() {
		return min;
	}
	
	public Double getMax() {
		return max;
	}
	
	public Double getMean() {
		return mean;
	}
	
	/**Returns the standard deviation based on the unbaiased sample variance of all data points added to this vector
	 * 
	 * @return The sample standard deviation
	 */
	public Double getSD() {
		return Math.sqrt(this.getVariance());
	}
	
	/**Returns the unbaiased sample variance of all data points added to this vector
	 * 
	 * @return The sample variance
	 */
	public Double getVariance() {
		
		Double sumsd = 0d;
		Double mean = getMean();
		
		for(Double d : v) {
			sumsd += Math.pow((d - mean), 2);
		}
		
		double var = sumsd / (size()-1);
		return var;
	}

	/**Returns a {@link #SV()} that is the nth, inclusive, to kth, exclusive components of this vector.
	 * For example, if this is {1, 2, 4, 6, 8} then sublist(1,4) returns {2, 4, 6}
	 * @param n Starting dimension, this is included
	 * @param k Ending dimension, this is not included
	 * @return 
	 */
	public SV sublist(int n, int k) {
		
		SV sv = new SV();
		
		for(int i = n; i < k; i++) {
			sv.append(v.get(i));
		}
		
		return sv;
	}
	
	@Override
	public int hashCode() {
		
		long hash =  Double.doubleToLongBits(mean) ^ 
				Double.doubleToLongBits(sum) ^ 
				Double.doubleToLongBits(min) ^ 
				Double.doubleToLongBits(max) ^ 
				count;
		
		return (int) hash;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof SV){
			SV otherT = (SV) o;
			if(otherT.size() == this.size()){
				//See if every item in other matches
				return IntStream.range(0, otherT.size()).allMatch(t -> otherT.get(t).equals(this.get(t)));
			}
		}
		return false;
	}
	
	
	public String toString() {
		return v.toString();
	}
	
	/**
	 * Prints the following states: size, min value, max value, mean, and the standard deviation
	 */
	public void printStats() {
		System.out.println(v);
		System.out.println("Size:\t"+ size());
		System.out.println("Min:\t"+ getMin());
		System.out.println("Max:\t"+ getMax());
		System.out.println("Mean:\t"+ getMean());
		System.out.println("SD:\t"+ getSD());
		System.out.println();
	}
	
	/**Prints some stats on this objects with the given title
	 * 
	 * @param title Title to prepend to the stats
	* @see #printStats()
	 */
	public void printStats(String title) {
		System.out.println("== "+ title + " ===");
		printStats();
	}

	/**Returns the number of values in the SV
	 * 
	 * @return The cardinality of this SV
	 */
	public int getCount() {
		return this.count;
	}
	
	/**Removes the first item from this vector and appends the given.  It is recommend not to provide large positive or negative values.
	 * 
	 * @param item The item to add
	 * @return The removed item
	 */
	public double shift(Number item) {
		Double removedItem = v.remove(0);
		this.append(item.doubleValue());
		return removedItem;
	}

}
