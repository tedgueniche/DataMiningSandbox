package utilities;

import static org.junit.Assert.*;

import org.junit.Test;

public class PairTests {
	
	@Test
	public void equalsTest() {
		String litter = "litter";
		String maxum = "maxus";
		double small = 31.42d;
		Pair<String, Double> simplePair = new Pair<String, Double>(litter, small);
		
		assertEquals("Double value equality", small, simplePair.getR().doubleValue(), 0.0d);
		assertTrue("Strong string equality", simplePair.getL() == litter);
		assertFalse("Strong string inequality", simplePair.getL() == maxum);
		assertEquals(".equals String equality", simplePair.getL(), litter);
	}
	
}
