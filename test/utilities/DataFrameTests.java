package utilities;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Some of these are based on examples found at <a
 * href="http://itl.nist.gov/div898/handbook/pmc/section5/pmc541.htm">nist</a>
 *
 */
public class DataFrameTests {
	
	@Test
	public void checkColumns() {
		double[][] data = { { 4.0d, 2.0d, 0.60d }, { 4.2d, 2.1d, 0.59d }, { 3.9d, 2.0d, 0.58 }, { 4.3d, 2.1d, 0.62 },
				{ 4.1d, 2.2d, 0.62 } };
		
		DataFrame parsedData = new DataFrame();
		
		for (double[] row : data) {
			SV newRow = new SV();
			Arrays.stream(row).forEachOrdered(newRow::add);
			parsedData.addRow(newRow);
		}
		
		double[] expectedMeans = { 4.10d, 2.08d, 0.604d };
		double[] expectedVars = { 0.025d, 0.0070d, 0.00135d };
		double[] expectedMax = { 4.3d, 2.2d, 0.63d };
		double[] expectedMins = { 3.9d, 2.0d, 0.58d };
		
		for(int rowNum = 0; rowNum < 3; rowNum++){
			SV columnToTest = parsedData.getColumnData(rowNum);
			TestUtility.checkSVCorrect("Row Number: " + rowNum + " ", columnToTest, data.length, 
					expectedMeans[rowNum], expectedMins[rowNum], 
					expectedMax[rowNum], expectedVars[rowNum], 0.01);
		}
	}


	
}
