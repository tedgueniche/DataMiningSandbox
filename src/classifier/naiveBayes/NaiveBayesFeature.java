package classifier.naiveBayes;

import java.util.HashMap;
import java.util.Set;

import utilities.SV;

public class NaiveBayesFeature {

	/**
	 * A list of data per class
	 */
	private HashMap<String, SV> data;

	
	public NaiveBayesFeature() {
		data = new HashMap<String, SV>();
	}
	
	public NaiveBayesFeature(String identifier, SV values) {
		this();
		
		trainClass(identifier, values);
	}
	

	public HashMap<String, Double> classify(Double Xi) {
		
		Set<String> classes = data.keySet();
		
		//Store the results per class
		HashMap<String, Double> results = new HashMap<String, Double>();
		
		//Calculate P(Class i | X i) for each class i
		for(String classe : classes) {
		
			SV v = data.get(classe);
			
			double tier1 = 1 / Math.sqrt( 2 * Math.PI * v.getVariance());
			double tier2 = Math.pow(Xi - v.getMean(), 2) * -1;
			double tier3 = 2 * v.getVariance();
			double tier4 = Math.exp( (tier2 / tier3) );
			
			double score = (tier1 * tier4);
			
			if(Double.isNaN(score) || score == 0.0)
				score = 1.0;
			
			results.put(classe, score);
		}
		
		return results;
	}

	
	public void trainClass(String identifier, SV values) {
		data.put(identifier, values);
	}
	

	public void reset() {
		data = new HashMap<String, SV>();
	}
	
}
