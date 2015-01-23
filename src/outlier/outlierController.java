package outlier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class outlierController {

	
	
	
	
	
	public static void main(String...args) {
		
		System.out.println("starting");
		
		
		List<LOFPoint> data = new ArrayList<LOFPoint>();
		data.add(new LOFPoint(2d));
		data.add(new LOFPoint(2.4d));
		data.add(new LOFPoint(2.6d));
		data.add(new LOFPoint(2.9d));
		data.add(new LOFPoint(3d));
		data.add(new LOFPoint(3.2d));
		data.add(new LOFPoint(3.3d));
		data.add(new LOFPoint(400d));
		
		LocalOutlierFactor lof = new LocalOutlierFactor(3, data);
		HashMap<LOFPoint, Double> result = lof.LOF();
		
		
		System.out.println(result);
		
	
	}
	
}
