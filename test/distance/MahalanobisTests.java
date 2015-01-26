package distance;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.sun.j3d.internal.Distance;

import utilities.DataFrame;
import utilities.SV;
import utilities.SVTests;

/**
 * Does checks on the {@link distance.Mahalanobis} class.
 * 
 * Some of these are based on examples found at <a
 * href="http://itl.nist.gov/div898/handbook/pmc/section5/pmc541.htm">nist</a>
 *
 */
public class MahalanobisTests extends SVTests {
	
	@Test
	public void checkColumns() {
		double[][] data = { { 4.0d, 2.0d, 0.60d }, { 4.2d, 2.1d, 0.59d }, { 3.9d, 2.0d, 0.58 }, { 4.3d, 2.1d, 0.62 },
				{ 4.1d, 2.2d, 0.62 } };
		
		DataFrame parsedData = new DataFrame();
		
		List<SV> rows = new ArrayList<>();
		for (double[] row : data) {
			SV newRow = new SV();
			Arrays.stream(row).forEachOrdered(newRow::add);
			parsedData.addRow(newRow);
		}
		
		Mahalanobis toTestDist = new Mahalanobis(parsedData);
		
		double[][] covarianceMatrix = toTestDist.getCovarianceMatrix();
		
	}
	
}
