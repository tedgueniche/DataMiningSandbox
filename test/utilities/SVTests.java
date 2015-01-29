package utilities;

import static org.junit.Assert.*;

import org.junit.Test;

public class SVTests {
	
	@Test
	public void onlyPositives() {
		SV vectors = new SV();
		vectors.append(4);
		vectors.append(5);
		vectors.append(6);
	}
	
	@Test
	public void smallZeroMean() {
		SV testVector = new SV();
		int goToVal = 10_000;
		for (int absVal = 1; absVal <= goToVal; absVal++) {
			testVector.append(absVal);
			testVector.append(-absVal);
		}
		TestUtility.checkSVCorrect(testVector, goToVal * 2, 0, -goToVal, goToVal, 5773.936 * 5773.936, 0.01);
	}
	
	
}
