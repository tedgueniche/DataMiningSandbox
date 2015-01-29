package utilities;
import static org.junit.Assert.assertEquals;
import utilities.SV;


public class TestUtility {
	public static void checkSVCorrect(SV toTest, int count, double mean, double min, double max, double variance,
			double tolerancePercent) {
		checkSVCorrect("", toTest, count, mean, min, max, variance, tolerancePercent);
	}
	
	public static void checkSVCorrect(String prependTestText, SV toTest, int count, double mean, double min, double max, double variance,
			double tolerancePercent) {
		System.out.println(toTest.getVector());
		double sd = Math.sqrt(variance);
		assertEquals(prependTestText + "Count: ", count, toTest.getCount());
		assertEquals(prependTestText + "Mean: ", mean, toTest.mean(), mean * tolerancePercent);
		assertEquals(prependTestText + "Variance: ", variance, toTest.var(), variance * tolerancePercent);
		assertEquals(prependTestText + "SD: ", sd, toTest.sd(), sd * tolerancePercent);
		assertEquals(prependTestText + "Min: ", min, toTest.min(), 0);
		assertEquals(prependTestText + "Max: ", max, toTest.max(), 0);	
	}
}
