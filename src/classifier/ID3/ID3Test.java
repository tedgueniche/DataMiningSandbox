package classifier.ID3;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Unit tests for this algorithm
 */
public class ID3Test {

	/**
	 * jUnit test yo
	 */
	@Test
	public void unitTest_1() {
		
		// first attribute is 1
		List<ID3Instance> goodInstances = new ArrayList<ID3Instance>();
		ID3Instance good1 = new ID3Instance();
		goodInstances.add(good1);
		good1.add(1.0);
		good1.add(1.0);
		ID3Instance good2 = new ID3Instance();
		goodInstances.add(good2);
		good2.add(1.0);
		good2.add(2.0);
		ID3Instance good3 = new ID3Instance();
		goodInstances.add(good3);
		good3.add(1.0);
		good3.add(3.0);
		
		// first attribute is 2 
		List<ID3Instance> badInstances = new ArrayList<ID3Instance>();
		ID3Instance bad1 = new ID3Instance();
		badInstances.add(bad1);
		bad1.add(2.0);
		bad1.add(1.0);
		ID3Instance bad2 = new ID3Instance();
		badInstances.add(bad2);
		bad2.add(2.0);
		bad2.add(2.0);
		ID3Instance bad3 = new ID3Instance();
		badInstances.add(bad3);
		bad3.add(2.0);
		bad3.add(2.0);
		
		// should be class 1
		ID3Instance test = new ID3Instance();
		test.add(1.1);
		test.add(4.0);
		
		// create and evaluate
		ID3 id3 = new ID3();
		id3.train(goodInstances, badInstances);
		double goodPer = id3.evaluate(test);
		
		assertEquals(true, (goodPer == 1.0));
	}
}
