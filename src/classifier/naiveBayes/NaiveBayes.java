package classifier.naiveBayes;

import java.util.HashMap;
import java.util.Set;

public class NaiveBayes {

	
	private HashMap<String, NaiveBayesFeature> features;
	
	
	public NaiveBayes() {
		features = new HashMap<String, NaiveBayesFeature>();
	}
	
	public void addFeature(String identifier, NaiveBayesFeature feature) {
		features.put(identifier, feature);
	}
	
	
	public HashMap<String, Double> classify(HashMap<String, Double> values) {
		
		HashMap<String, Double> results = new HashMap<String, Double>();
		
		
		
		Set<String> featureNames = values.keySet();
		for(String featureName : featureNames) {
			
			//Hashmap of the results for this feature for each classe
			HashMap<String, Double> vals = features.get(featureName).classify(values.get(featureName));
			
			//Updating each classe with the results from this feature
			Set<String> classes = vals.keySet();
			for(String classe : classes) {
				
				//updating the classe value for this feature
				Double classeResult = results.get(classe);
				if(classeResult == null) {
					classeResult = 1.0d;
				}
				classeResult *= vals.get(classe);
				results.put(classe, classeResult);
			}
			
		}
		
		return results;
	}
	
}
