package forecasting.linearRegression;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for this bitch
 */
public class LinearRegressionTest {

	/**
	 * Our very own linear regression
	 */
	private LinearRegression lr;
	private ArrayList<Double> X;
	private ArrayList<Double> Y;
	
	/**
	 * Init the stuff
	 */
	@Before
	public void unitTest_Before() {
		lr = new LinearRegression();
		X = new ArrayList<Double>();
		Y = new ArrayList<Double>();
	}
	
	/**
	 * Junit test so we know it works yo
	 */
	@Test
	public void unitTest_1() {
		X.add(1.0);
		Y.add(1.0);
		X.add(2.0);
		Y.add(2.0);
		lr.addData(X, Y);
		assertEquals(1.0,lr.M(),0.0001);
		assertEquals(0.0,lr.B(),0.0001);
	}
	
	/**
	 * Junit test so we know it works yo
	 */
	@Test
	public void unitTest_2() {
		X.add(1.0);
		Y.add(1.0);
		X.add(2.0);
		Y.add(2.0);
		X.add(3.0);
		Y.add(3.0);
		X.add(4.0);
		Y.add(4.0);
		X.add(5.0);
		Y.add(5.0);
		lr.addData(X, Y);
		assertEquals(1.0,lr.M(),0.0001);
		assertEquals(0.0,lr.B(),0.0001);
	}
	
	/**
	 * Junit test so we know it works yo
	 */
	@Test
	public void unitTest_3() {
		X.add(1.0);
		Y.add(1.0);
		X.add(2.0);
		Y.add(2.0);
		X.add(3.0);
		Y.add(3.0);
		lr.addData(X, Y);
		lr.addData(4.0,4.0);
		lr.addData(5.0,5.0);
		assertEquals(1.0,lr.M(),0.0001);
		assertEquals(0.0,lr.B(),0.0001);
	}
	
	/**
	 * Junit test so we know it works yo
	 */
	@Test
	public void unitTest_4() {
		X.add(1.0);
		Y.add(1.0);
		X.add(2.0);
		Y.add(2.0);
		X.add(3.0);
		Y.add(3.0);
		lr.addData(X, Y);
		lr.addData(4.0,20.0);
		assertEquals(5.8,lr.M(),0.0001);
		assertEquals(-8.0,lr.B(),0.0001);
	}
	
	/**
	 * Junit test so we know it works yo
	 */
	@Test
	public void unitTest_5() {
		X.add(1.0);
		Y.add(1.0);
		X.add(2.0);
		Y.add(1.0);
		X.add(3.0);
		Y.add(1.0);
		lr.addData(X, Y);
		assertEquals(0.0,lr.M(),0.0001);
		assertEquals(1.0,lr.B(),0.0001);
	}
	
	/**
	 * Junit test so we know it works yo
	 */
	@Test
	public void unitTest_6() {
		lr.addData(1.0, 2.0);
		lr.addData(2.0, 4.0);
		lr.addData(3.0, 6.0);
		assertEquals(2.0,lr.M(),0.0001);
		assertEquals(0.0,lr.B(),0.0001);
	}
	
	/**
	 * Junit test so we know it works yo
	 */
	@Test
	public void unitTest_7() {
		X.add(1.0);
		Y.add(1.0);
		X.add(1.0);
		Y.add(2.0);
		X.add(1.0);
		Y.add(3.0);
		lr.addData(X, Y);
		assertEquals(Double.NaN,lr.M(),0.0001);
		assertEquals(Double.NaN,lr.B(),0.0001);
	}
}
