/**
 * 
 */
package forecasting.movingaverage;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import utilities.SV;

/**
 *
 */
public class MovingAverage {

	SV svList = new SV();
	
	/**
	 * This method calculates the simple moving average
	 * 
	 * @param numPreviousDays
	 *            the number of previous days / values to use
	 * @return the simple moving average
	 */
	public double simpleMovingAverage(int numPreviousDays) {
		int n = svList.size();
		if (n <= 0) {
			return 0;
		}
		double sum = svList.sum();
		return (sum/n);
	}

	/**
	 * This method calculates the simple moving average update since yesterday or last known value
	 * 
	 * @param newValue
	 *            The new value item to use
	 * @param SMAyesterday
	 *            the previous known simple moving average
	 * @return the updated simple moving average
	 */
	public double simpleMovingAverageToday(int newValue, int SMAyesterday) {
		int n = svList.size();
		return SMAyesterday - ((svList.shift(newValue))/n) + (newValue/n);
	}
	
	/**
	 * This method calculates the cumulative moving average
	 * 
	 * @return the cumulative moving average
	 */
	public double getCumulativeMovingAverage() {
		return svList.sum()/svList.size();
	}
	
	/**
	 * This method calculates the updated cumulative moving average since the last known value
	 * 
	 * @param newValue
	 *            the number of previous days / values to use
	 * @param CMAn
	 *            the previous cumulative moving average
	 * @return the updated cumulative moving average
	 */
	public int cumulativeMovingAverage(int newValue, int CMAn) {
		int n = svList.size();
		return (newValue+(n*CMAn))/n+1;
	}
	
	/**
	 * This method calculates the weighted moving average
	 * 
	 * @return the weighted moving average
	 */
	public int weightedMovingAverage() {
		int sum = 0;
		int n = svList.size();
		if (n<=0) {
			return 0;
		}
		int denominator = (n*(n+1))/2;
		Iterator<Double> iterator = svList.getVector().iterator();
		while (iterator.hasNext()) {
			sum += (n*iterator.next());
			n--;
		}
		return sum/denominator;
	}
	
	/**
	 * This method calculates the exponential moving average
	 * 
	 * @param newValue
	 *            the new value item to use
	 * @param EMAyesterday
	 *            the previous exponential moving average
	 * @return the exponential moving average
	 */
	public int exponentialMovingAverage(int newValue, int EMAyesterday) {
		int k = 2/(svList.size()+1);
		int EMA = newValue*k+EMAyesterday*(1-k);
		return EMA;
	}
	
	/**
	 * This method calculates the modified moving average
	 * 
	 * @param newValue
	 *            the new value item to use
	 * @param MMAyesterday
	 *            the previous modified moving average
	 * @return the modified moving average
	 */
	public int modifiedMovingAverage(int newValue, int MMAyesterday) {
		int n = svList.size();
		int MMAtoday = (((n-1)*MMAyesterday)+newValue)/n;
		return MMAtoday;
	}
	
	/**
	 * This is the comparator to use when adding a moving average to a list
	 *
	 */
	public static Comparator<Double> c = new Comparator<Double>() {
		@Override
		public int compare(Double o1, Double o2) {
			return 0;
		}
		
	};

	/**
	 * This method calculates the moving median
	 * 
	 * @return the moving median value
	 */
	public double movingMedianValue() {
		Vector<Double> list = svList.getVector();
		Collections.sort(list);
		int listSize = list.size();
		if (listSize%2 == 0 && listSize > 1) {
			return ((svList.get((listSize/2)-1)+svList.get(listSize/2))/2);
		} else if (listSize > 1){
			return svList.get(listSize/2);
		} else if (listSize == 1) {
			return svList.get(0);
		} else {
			return 0;
		}
	}
	
	/**
	 * This method calculates the moving median with a specified number of points on each side
	 * 
	 * @return the moving median to a specified number of points on each side
	 */
	public double movingMedian(int numPoints) {
		int calcNumPoints = (numPoints*numPoints)+1;
		int listSize = svList.size();
		int startPoint = listSize-calcNumPoints-1;
		if (calcNumPoints < listSize) {
			startPoint = 0;
		}
		
		int average = 0;
		
		for (int x=startPoint; x< listSize; x++) {
			average += svList.get(x);
		}
		return average;
	}

	/**
	 * This method calculates the moving average regression using the regression formula:
	 * 				Xt = mu + (1 + theta_1 * B_1 + ... + theta_q *B_q)eps_t
	 * 
	 * @param eps
	 * 				the value of epsilon at a point in time
	 * @param mu
	 * 				the value of mu
	 * 
	 * @return the moving average regression
	 */
	public double getMovingAverageRegression(double eps, double mu) {
		double Xt = mu + eps;
		Iterator<Double> iterator = svList.getVector().iterator();
		while (iterator.hasNext()) {
			Xt += (iterator.next()*eps);
		}
		return Xt;
	}
}
