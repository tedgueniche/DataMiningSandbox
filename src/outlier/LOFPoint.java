package outlier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import utilities.SV;

public class LOFPoint {

	private static int lastId = 0;
	
	public int id;
	public SV values;
	
	public LOFPoint() {
		
		//TODO: make it thread safe (use semaphore)
		id = lastId;
		lastId++;
		
		values = new SV();
	}
	
	public LOFPoint(double value) {
		this();
		values.append(value);
	}
	
	

	
	
	public String toString() {
		return values.toString();
	}
}
