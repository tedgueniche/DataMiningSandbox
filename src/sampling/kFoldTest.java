package sampling;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit Tests for kFold
 */
public class kFoldTest {
	
	/**
	 * jUnit test so we know sutff works yo 
	 */
	@Test
	public void unitTest_1() {
		kFold<Integer> kf = new kFold<Integer>(5);
		kf.add(new Integer(0));
		kf.add(new Integer(1));
		kf.add(new Integer(2));
		kf.add(new Integer(3));
		kf.add(new Integer(4));
		
		// get the first one
		kFoldSample<Integer> sample1 = kf.get();
		assertEquals(1,sample1.test.size());
		assertEquals(4,sample1.training.size());
		assertEquals(new Integer(0),sample1.test.get(0));
		assertEquals(new Integer(1),sample1.training.get(0));
		assertEquals(new Integer(2),sample1.training.get(1));
		assertEquals(new Integer(3),sample1.training.get(2));
		assertEquals(new Integer(4),sample1.training.get(3));
		
		// get the first one
		kFoldSample<Integer> sample2 = kf.get();
		assertEquals(1,sample2.test.size());
		assertEquals(4,sample2.training.size());
		assertEquals(new Integer(1),sample2.test.get(0));
		assertEquals(new Integer(0),sample2.training.get(0));
		assertEquals(new Integer(2),sample2.training.get(1));
		assertEquals(new Integer(3),sample2.training.get(2));
		assertEquals(new Integer(4),sample2.training.get(3));
	}

	/**
	 * jUnit test so we know sutff works yo 
	 */
	@Test
	public void unitTest_2() {
		kFold<Integer> kf = new kFold<Integer>(2);
		kf.add(new Integer(0));
		kf.add(new Integer(1));
		kf.add(new Integer(2));
		kf.add(new Integer(3));
		
		// get the first one
		kFoldSample<Integer> sample1 = kf.get();
		assertEquals(2,sample1.test.size());
		assertEquals(2,sample1.training.size());
		assertEquals(new Integer(0),sample1.test.get(0));
		assertEquals(new Integer(1),sample1.test.get(1));
		assertEquals(new Integer(2),sample1.training.get(0));
		assertEquals(new Integer(3),sample1.training.get(1));
		
		// get the second one
		kFoldSample<Integer> sample2 = kf.get();
		assertEquals(2,sample2.test.size());
		assertEquals(2,sample2.training.size());
		assertEquals(new Integer(2),sample2.test.get(0));
		assertEquals(new Integer(3),sample2.test.get(1));
		assertEquals(new Integer(0),sample2.training.get(0));
		assertEquals(new Integer(1),sample2.training.get(1));
		
		// get the third one
		kFoldSample<Integer> sample3 = kf.get();
		assertEquals(2,sample3.test.size());
		assertEquals(2,sample3.training.size());
		assertEquals(new Integer(0),sample3.test.get(0));
		assertEquals(new Integer(1),sample3.test.get(1));
		assertEquals(new Integer(2),sample3.training.get(0));
		assertEquals(new Integer(3),sample3.training.get(1));
	}
}
