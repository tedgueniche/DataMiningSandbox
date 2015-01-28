package sampling;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the stuff
 */
public class kFoldSample<Stuff> {
	public List<Stuff> training;
	public List<Stuff> test;
	public kFoldSample() {
		training = new ArrayList<Stuff>();
		test = new ArrayList<Stuff>();
	}
}
