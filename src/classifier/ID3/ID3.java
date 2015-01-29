package classifier.ID3;

import java.util.ArrayList;
import java.util.List;


/**
 * Builds an ID3 tree with numerical attributes
 */
public class ID3 {

	/*
	 * Attributes for this node
	 */
	private ID3 biggerChild;
	private ID3 smallerChild;
	private int indexOfBestAttribute;
	private double bestAttributePointOfDivision;
	private double goodPercentage;
	private double badPercentage;
	private double bestInformationGain;
	private int nodeDepth;

	/**
	 * Maximum node depth. Avoids over fitting
	 */
	private final static int MAXIMUM_NODE_DEPTH = 7;
	private final static double MINIMUM_NODE_PERCENTAGE = 0.05;
	
	/**
	 * Constructor
	 */
	public ID3() {
		nodeDepth = 0;
		biggerChild = null;
		smallerChild = null;
		indexOfBestAttribute = -1;
		bestAttributePointOfDivision = -1;
		bestInformationGain = -Double.MAX_VALUE;
		goodPercentage = 0;
		badPercentage = 0;
	}
	
	/**
	 * Train the algorithm with a training set of good and bad
	 */
	public void train(List<ID3Instance> goodTrainingSet, List<ID3Instance> badTrainingSet) {
		
		// get the instances
		ArrayList<ID3Instance> goodInstances = new ArrayList<ID3Instance>(goodTrainingSet);
		ArrayList<ID3Instance> badInstances = new ArrayList<ID3Instance>(badTrainingSet);
		double totalInstances = goodInstances.size() + badInstances.size();
		goodPercentage = ((double)goodInstances.size()) / totalInstances;
		badPercentage = ((double)badInstances.size()) / totalInstances;
		
		// if node is pure then stop
		if ( goodPercentage < MINIMUM_NODE_PERCENTAGE ) { return; }
		if ( badPercentage < MINIMUM_NODE_PERCENTAGE ) { return; }
				
		// get number of attributes
		int numberOfAttributes = goodInstances.get(0).size();
		
		// this node value
		bestInformationGain = -Double.MAX_VALUE;
		indexOfBestAttribute = -1;
		bestAttributePointOfDivision = -1;
		
		// Calculate best information gain of all the attributes
		for ( int attributeIndex = 0; attributeIndex < numberOfAttributes; ++attributeIndex ) {
			
			// get the division point for the numerical attribute
			double pointOfDivision = divisionPoint(attributeIndex,goodInstances,badInstances);
			
			// split the attributes into bigger and smaller fields of splitting point
			int biggerBuy = 0;
			int smallerBuy = 0;
			int biggerSell = 0;
			int smallerSell = 0;
			for ( ID3Instance instance : goodInstances ) {
				if ( instance.get(attributeIndex) > pointOfDivision ) {
					++biggerBuy;
				} else {
					++smallerBuy;
				}
			}
			for ( ID3Instance instance : badInstances ) {
				if ( instance.get(attributeIndex) > pointOfDivision ) {
					++biggerSell;
				} else {
					++smallerSell;
				}
			}
			
			// get information gain
			double infoGain = informationGain(biggerBuy, smallerBuy, biggerSell, smallerSell);
			if ( infoGain > bestInformationGain ) {
				bestInformationGain = infoGain;
				indexOfBestAttribute = attributeIndex;
				bestAttributePointOfDivision = pointOfDivision;
			}
		}
		
		// Create children node if we can, else this is a leaf node
		if ( bestInformationGain > 0 && nodeDepth < MAXIMUM_NODE_DEPTH ) {
			
			// split the values
			List<ID3Instance> biggerSetGood = new ArrayList<ID3Instance>();
			List<ID3Instance> biggerSetBad = new ArrayList<ID3Instance>();
			List<ID3Instance> smallerSetGood = new ArrayList<ID3Instance>();
			List<ID3Instance> smallerSetBad = new ArrayList<ID3Instance>();
			for ( ID3Instance instance : goodInstances ) {
				if ( instance.get(indexOfBestAttribute) > bestAttributePointOfDivision ) {
					biggerSetGood.add(instance);
				} else {
					smallerSetGood.add(instance);
				}
			}
			for ( ID3Instance instance : badInstances ) {
				if ( instance.get(indexOfBestAttribute) > bestAttributePointOfDivision ) {
					biggerSetBad.add(instance);
				} else {
					smallerSetBad.add(instance);
				}
			}
			
			// debug
			if ( nodeDepth == 0 ) {
				System.out.println("nodeDepth : " + nodeDepth);
				System.out.println("bestInformationGain : " + bestInformationGain);
				System.out.println("indexOfBestAttribute : " + indexOfBestAttribute);
				System.out.println("bestAttributePointOfDivision : " + bestAttributePointOfDivision);
				System.out.println("biggerSet GOOD count : " + biggerSetGood.size() );
				System.out.println("biggerSet BAD count : " + biggerSetBad.size() );
				System.out.println("smallerSet GOOD count : " + smallerSetGood.size() );
				System.out.println("smallerSet BAD count : " + smallerSetBad.size() );
				System.out.println();
			}
			
			// create children and train them
			biggerChild = new ID3();
			biggerChild.nodeDepth = nodeDepth+1;
			biggerChild.train(biggerSetGood, biggerSetBad);
			smallerChild = new ID3();
			smallerChild.nodeDepth = nodeDepth+1;
			smallerChild.train(smallerSetGood, smallerSetBad);
		}
		else {
			biggerChild = null;
			smallerChild = null;
		}
	}

	/**
	 * Evaluate an a new instance. Returns the percetage that it is good
	 */
	public double evaluate(ID3Instance instance) {
		
		// if we have children
		if ( biggerChild != null && smallerChild != null ) {
			double instanceValue = instance.get(indexOfBestAttribute);
			if ( instanceValue > bestAttributePointOfDivision ) {
				return ( biggerChild.evaluate(instance) );
			} else {
				return ( smallerChild.evaluate(instance) );
			}
		}
		else {
			return ( goodPercentage );
		}
	}
	
	/**
	 * Pick division point for attribute
	 * For example if attribute 1 is slope the division point might be 0
	 */
	private double divisionPoint( int attributeIndex, ArrayList<ID3Instance> good, ArrayList<ID3Instance> bad ) {
		
		// buy average
		double buyAverage = 0.0;
		for ( ID3Instance instance : good ) {
			buyAverage += instance.get(attributeIndex);
		}
		buyAverage /= ((double)good.size());
		
		// sell average
		double sellAverage = 0.0;
		for ( ID3Instance instance : bad ) {
			sellAverage += instance.get(attributeIndex);
		}
		sellAverage /= ((double)bad.size());
		
		// get standard deviation for buy
		double buyStdDev = 0.0;
		for ( ID3Instance instance : good ) {
			buyStdDev += Math.pow(instance.get(attributeIndex)-buyAverage,2.0);
		}
		buyStdDev /= ((double)good.size());
		buyStdDev = Math.sqrt(buyStdDev);
		
		// get standard deviation for sell
		double sellStdDev = 0.0;
		for ( ID3Instance instance : bad ) {
			sellStdDev += Math.pow(instance.get(attributeIndex)-sellAverage,2.0);
		}
		sellStdDev /= ((double)bad.size());
		sellStdDev = Math.sqrt(sellStdDev);
		
		// if buy is avg 0 and stdDev 1 and sell is avg 6 and stdDev 3
		// we want the division point to be at 2 and not 3
		double divisonPoint = 0.0;
		if ( (buyAverage+buyStdDev) < (sellAverage-sellStdDev) ) {
			divisonPoint = ((buyAverage+buyStdDev) + (sellAverage-sellStdDev)) / 2.0;
		}  else if ( (sellAverage+sellStdDev) < (buyAverage-buyStdDev) ) {
			divisonPoint = ((buyAverage-buyStdDev) + (sellAverage+sellStdDev)) / 2.0;
		} else {
			divisonPoint = (buyAverage + sellAverage) / 2.0;
		}
		return ( divisonPoint );
	}
	
	/**
	 * Get information gain of numeric attributes
	 */
	private double informationGain( int biggerBuy, int smallerBuy, int biggerSell, int smallerSell ) {
		
		// sums
		double total = biggerBuy + smallerBuy + biggerSell + smallerSell;
		double totalBigger = biggerBuy + biggerSell;
		double totalSmaller = smallerBuy + smallerSell;
		double totalBuy = biggerBuy + smallerBuy;
		double totalSell = biggerSell + smallerSell;
		
		// calculate bigger entropy
		double biggerEntropy = Double.MAX_VALUE;
		if ( totalBigger > 0.0 ) {
			biggerEntropy = (totalBigger/total) * entropy( ((double)biggerBuy)/totalBigger, ((double)biggerSell)/totalBigger );
		}
		
		// calculate the smaller entropy
		double smallerEntropy = Double.MAX_VALUE;
		if ( totalSmaller > 0.0 ) {
			smallerEntropy = (totalSmaller/total) * entropy( ((double)smallerBuy)/totalSmaller, ((double)smallerSell)/totalSmaller );
		}
		
		// information gain of the system
		double totalEntropy = entropy( totalBuy/total, totalSell/total );
		return ( totalEntropy - smallerEntropy - biggerEntropy );
	}
	
	/**
	 * Get entropy of classes
	 * Only to be used by informationGain
	 */
	private double entropy( double... classes ) {
		double totalEntropy = 0.0;
		for ( double probability : classes ) {
			if ( probability > 0.0 ) {
				totalEntropy += (-probability*Math.log(probability));
			}
		}
		return ( totalEntropy );
	}

}