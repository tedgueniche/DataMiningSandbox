package forecasting.linearRegression;

import java.util.List;

/**
 * Regress Linearly YO. Givs de slippery slope n de bee yo. So u canz predicts the future!!!
 */
public class LinearRegression {
	
	/**
	 * Values used internally for cumpooting.
	 * I used horrible variables name because I hate you.
	 */
	private int N = 0;
	private double A = 0.0;
	private double B = 0.0;
	private double C = 0.0;
	private double D = 0.0;
	private double m = 0.0;
	private double b = 0.0;
	
	/**
	 * Constructor
	 */
	public LinearRegression() {
		N = 0;
		A = 0.0;
		B = 0.0;
		C = 0.0;
		D = 0.0;
		m = 0.0;
		b = 0.0;
	}
	
	/**
	 * Add sum Data's
	 */
	public void addData( List<Double> xVals, List<Double> yVals ) {
		
		// the size and stuff
	    int n = xVals.size();
	    N += n;

	    // for all points add up the errors
	    for ( int i = n-1; i >= 0; --i ) {
			A += xVals.get(i);
			B += yVals.get(i);
			C += xVals.get(i) * xVals.get(i);
			D += xVals.get(i) * yVals.get(i);
	    }
	    
	    // Trivial so I won't explain
	    m = ((N*D)-(A*B))/((N*C)-(A*A));
	    b = ((B-(m*A))/N);
	}
	
	/**
	 * Add only one Data's
	 */
	public void addData( Double xVal, Double yVal ) {
		
		// the size and stuff
	    N += 1;

	    // for all points add up the errors
		A += xVal;
		B += yVal;
		C += xVal * xVal;
		D += xVal * yVal;
	    
	    // Trivial so I won't explain
	    m = ((N*D)-(A*B))/((N*C)-(A*A));
	    b = ((B-(m*A))/N);
	}
	
	/**
	 * Get the slippery slope of the linear regression
	 */
	public double M() {
		return (m);
	}
	
	/**
	 * Get the spot where the line intersects the Y axis
	 */
	public double B() {
		return (b);
	}
}
