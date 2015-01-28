package sampling;

import java.util.ArrayList;
import java.util.List;

/**
 * Get units for kfold testing yo.
 */
public class kFold<Stuff> {

	/**
	 * Stuff
	 */
	private int K = 0;
	private List<Stuff> stuff;
	private int pos;
	
	/**
	 * Constructor
	 */
	public kFold( int K ) {
		assert( K >= 2 );
		this.K = K;
		this.pos = 0;
		this.stuff = new ArrayList<Stuff>();
	}
	
	/**
	 * Get K
	 */
	public int K() {
		return K;
	}
	
	/**
	 * Add to stuff
	 */
	public void add( Stuff thing ) {
		pos = 0;
		stuff.add(thing);
	}
	
	/**
	 * Add to stuff
	 */
	public void add( List<Stuff> things ) {
		pos = 0;
		stuff.addAll(things);
	}
	
	/**
	 * Get next grouping of stuff.
	 * Returns null if it can't give you the next stuff.
	 * For example if stuff.size() == 5 and K == 10
	 */
	public kFoldSample<Stuff> get() {
		
		// get the stuff
		int amountInTest = (int) Math.ceil(((double)stuff.size()) / ((double)K));
		
		// create the sampling
		kFoldSample<Stuff> sample = new kFoldSample<Stuff>();
		for ( int i = 0; i < stuff.size(); ++i ) {
			if ( i >= pos*amountInTest && i < (pos+1)*amountInTest ) {
				sample.test.add( stuff.get(i) );
			} else {
				sample.training.add( stuff.get(i) );
			}
		}
		
		// goto next time yo
		++pos;
		if ( pos >= K ) {
			pos = 0;
		}
		
		// return the Kth fold
		return ( sample );
	}
	
}
