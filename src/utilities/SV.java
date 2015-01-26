package utilities;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;



/**
 * An {@link #SV()} represents an ordered series of data.
 * 
 * 
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
	
	
	public Vector<Double> getVector() {
		return v;
	}
	
	public boolean add(Integer item) {
		return add(1.0d * item);
	}
	
	public boolean add(Float item) {
		return add(1.0d * item);
	}
	
	public boolean add(Double item) {
		
		boolean status = v.add(item);
		if(status == true) {
		
			if(item > max) {
				max = item;
			}
			
			if(item < min) {
				min = item;
			}
			
			sum += item;
			count ++;
			
			mean = sum / count;			
		}
		
		return status;
	}
	

	public Double get(int index) {
		return v.get(index);
	}


	public int size() {
		return v.size();
	}
	
	/**
	 * Scales the value between min and max used as normalization
	 * e.g.: for {-5,0,5} with scale(0, 1) -> {0, 0.5, 1)
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
			add(vi);
		}
	}
	
	
	public void scaleZScore() {
		
		//Scaling each value of this SV
		Vector<Double> scaledV = new Vector<Double>();
	
		Double sd = sd();
		for(Double vi : v) {
			
			scaledV.add((vi - mean) / sd);
		}
		
		//Recreating this current SV
		clear();
		for(Double vi : scaledV) {
			add(vi);
		}
		
	}
	
	

	public void scaleZMorph() {
		double maxAbs = Math.max(Math.abs(this.min()), Math.abs(this.max()));
		Vector<Double> transformed = new Vector<>();
		v.forEach( d -> transformed.add( (d + maxAbs)/(2* maxAbs)));
		
		clear();
		
		transformed.forEach(d -> add(d));
	}
	
	
	/**
	 * Normalize the SV for values between 0 and 1
	 */
	public void normalize() {
		scale(0d,1d);
	}
	

	public Double sum() {
		return sum;
	}
	
	public Double min() {
		return min;
	}
	
	public Double max() {
		return max;
	}
	
	public Double mean() {
		return mean;
	}
	
	public Double sd() {
		return Math.sqrt(this.var());
	}
	
	public Double var() {
		
		Double sumsd = 0d;
		Double mean = mean();
		
		for(Double d : v) {
			sumsd += Math.pow((d - mean), 2);
		}
		
		return (sumsd / size());
	}

	
	public SV sublist(int from, int to) {
		
		SV sv = new SV();
		
		for(int i = from; i < to; i++) {
			sv.add(v.get(i));
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
		return (o.hashCode() == hashCode()) 
				&& (o instanceof SV)
				&& ((SV)o).mean == mean
				&& ((SV)o).sum == sum;
	}
	
	
	public String toString() {
		return v.toString();
	}
	
	
	public void printStats() {
		System.out.println(v);
		System.out.println("Size:\t"+ size());
		System.out.println("Min:\t"+ min());
		System.out.println("Max:\t"+ max());
		System.out.println("Mean:\t"+ mean());
		System.out.println("SD:\t"+ sd());
		System.out.println();
	}
	
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
	
	

}
