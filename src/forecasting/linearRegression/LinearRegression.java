package forecasting.linearRegression;

import java.util.ArrayList;
import java.util.List;

/**
 * Regress Linearly YO. So u canz predicts the future!!!
 */
public class LinearRegression {

	/**
	 * The data to regress
	 */
	private List<Double> X;
	private List<Double> Y;
	
	/**
	 * Constructor
	 */
	public LinearRegression() {
		X = new ArrayList<Double>();
		Y = new ArrayList<Double>();
	}
	
	/**
	 * Add sum Data's
	 */
	public void addData( List<Double> xVals, List<Double> yVals ) {
		X.addAll(X);
		Y.addAll(Y);
	}
}
