package distance;

import static org.junit.Assert.*;

import java.util.Arrays;
import org.junit.Test;

import utilities.DataFrame;
import utilities.SV;

public class MahalanobisTests {
	/**
	 * Data from <a
	 * href="http://itl.nist.gov/div898/handbook/pmc/section5/pmc541.htm"
	 * >nist</a>
	 */
	public double[][] nist = { { 4.0d, 2.0d, 0.60d }, { 4.2d, 2.1d, 0.59d }, { 3.9d, 2.0d, 0.58 },
			{ 4.3d, 2.1d, 0.62 }, { 4.1d, 2.2d, 0.63 } };
	
	/**
	 * Data from <a href=
	 * "nptel.ac.in/courses/Webcourse-contents/IIT-KANPUR/mathematics-2/node27.html"
	 * >nptel</a>
	 */
	public double[][] nptel = { { 2, 1, 1 }, { 1, 2, 1 }, { 1, 1, 2 } };
	
	/**
	 * Data from <a href=
	 * "http://www.intmath.com/matrices-determinants/inverse-matrix-gauss-jordan-elimination.php"
	 * >intmath</a>
	 */
	public double[][] intmath = { {11,12,7}, {3,10,13}, {4,14,5} };
	
	public Mahalanobis setUpMaha(double[][] dataToUse) {
		
		DataFrame parsedData = new DataFrame();
		
		for (double[] row : dataToUse) {
			SV newRow = new SV();
			Arrays.stream(row).forEachOrdered(newRow::append);
			parsedData.addRow(newRow);
		}
		
		Mahalanobis maha = new Mahalanobis(parsedData);
		return maha;
	}
	
	/** Checks the covariance matrix for the nist data */
	@Test
	public void checkCovariance() {
		double[][] data = nist;
		
		double[][] covariance = setUpMaha(data).getCovarianceMatrix();
		
		double[][] expectedCovariance = { { 0.025d, 0.0075d, 0.00175d }, { 0.0075d, 0.0070d, 0.00135d },
				{ 0.00175d, 0.00135d, 0.00043d } };
		compare(expectedCovariance, covariance, 0.0000_1);
		
	}
	
	public void compare(double[][] expected, double[][] actual, double tolerance) {
		assertEquals("Same number of rows", expected.length, actual.length);
		
		for (int row = 0; row < expected.length; row++) {
			assertEquals("Same number of cols", expected[row].length, actual[row].length);
			for (int column = 0; column < expected[row].length; column++) {
				String message = String.format("Comparing Row %s, Column %d", row, column);
				assertEquals(message, expected[row][column], actual[row][column], tolerance);
			}
		}
	}
	
	/**
	 * Sees if I get the right inverted matrix from the NIST dataset
	 */
	@Test
	public void invertedTest1() {
		double[][] data = nist;
		
		double[][] invertedCovariance = setUpMaha(data).getInvertedCovarianceMatrix();
		
		// Numbers from excel, truncated to the hundredths.
		double[][] expectedInversion = { { 60.89d, -44.23d, -108.97d }, { -44.23d, 394.23d, -1057.69d },
				{ -108.97d, -1057.69d, 6089.74d } };
		compare(expectedInversion, invertedCovariance, 0.02d);
	}
	
	/**
	 * Sees if I get the right inverted matrix from the NPTEL dataset
	 */
	@Test
	public void invertedTest2() {
		double[][] data = nptel;
		
		double[][] invertedCovariance = setUpMaha(data).getInvertedMatrix(data);
		
		// Numbers from excel, truncated to the hundredths.
		double[][] expectedInversion = { { 0.75d, -.25d, -.25d }, { -.25d, 0.75d, -.25d }, { -.25d, -.25d, 0.75f } };
		compare(expectedInversion, invertedCovariance, 0.001d);
	}
	
	
	/**
	 * Sees if I get the right inverted matrix from the intmath dataset
	 */
	@Test
	public void invertedTest3() {
		double[][] data = intmath;
		
		double[][] invertedCovariance = setUpMaha(data).getInvertedMatrix(data);
		
		// Numbers from excel, truncated to the hundredths.
		double[][] expectedInversion = {
				{0.1328d, -.0382d, -.0865d},
				{-.0375d, -.0272d, 0.1227d},
				{-.0020d, 0.1066d, -.0744d}
				
		};
		compare(expectedInversion, invertedCovariance, 0.0003d);
	}
	
	/**
	 * Sees if I get the right inverted matrix with a two x two matrix (5,11) (6,7)
	 */
	@Test
	public void invertedTest4() {
		double[][] data = { {5,11}, {6,7}};
		
		double[][] invertedCovariance = setUpMaha(data).getInvertedMatrix(data);
		
		// Numbers from excel, truncated to the hundredths.
		double[][] expectedInversion = {
				{-.2258d, 0.3548d},
				{0.1935d, -.1613d}
		};
		compare(expectedInversion, invertedCovariance, 0.0001d);
	}
	
	/**
	 * Does simple matrix mults
	 */
	@Test
	public void matrixMultTest(){
		double[][] data = intmath;
		Mahalanobis calc = setUpMaha(data);
		
		double[][] A = { {1,2}, {3, 0} };
		double[][] B = { {5, 2} };
		double[][] C = { {2} , {3} };
		double[][] D = { {2, 9}, {1, 8}};
		
		double[][] AxD = { {4, 25}, {6, 27}};
		double[][] BxA = { {11, 10} };
		double[][] BxAxD = { {32, 179}};
		double[][] BxC = { {16} };
		double[][] DxA = { {29, 4}, {25, 2}};
		
		double[][] actAxD = calc.getProduct(A, D);
		double[][] actBxA = calc.getProduct(B, A);
		
		double[][] actBxAxD = calc.getProduct(calc.getProduct(B, A), D);
		double[][] actBxAxD2 = calc.getProduct(actBxA, D);

		double[][] actBxC = calc.getProduct(B, C);
		double[][] actDxA = calc.getProduct(D, A);
		
		compare(AxD, actAxD, 0d);
		compare(BxA, actBxA, 0d);
		
		compare(BxAxD, actBxAxD, 0d);
		compare(BxAxD, actBxAxD2, 0d);
		
		compare(BxC, actBxC, 0d);
		compare(DxA, actDxA, 0d);
	
		try{
			double[][] failing = { {1, 2, 3, 4, 5, 6}};
			double[][] res = calc.getProduct(failing, A);
			fail("Should have gotten an error");
		}catch(IllegalArgumentException e){
			
		}
		
	}
}
