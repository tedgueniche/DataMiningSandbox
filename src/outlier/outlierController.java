package outlier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class outlierController {

	/**Small set of dense data, outliers close*/
	private static List<LOFPoint> smallConcentrated = new ArrayList<LOFPoint>(){{
		add(new LOFPoint(2d));
		add(new LOFPoint(2.4d));
		add(new LOFPoint(2.6d));
		add(new LOFPoint(2.9d));
		add(new LOFPoint(3d));
		add(new LOFPoint(3.2d));
		add(new LOFPoint(3.3d));
		add(new LOFPoint(4d));
	}};
	
	/**Two dense clusters, very far away, point in the middle*/
	private static List<LOFPoint> biCluster = new ArrayList<LOFPoint>(){{
		add(new LOFPoint(0.97d));
		add(new LOFPoint(0.98d));
		add(new LOFPoint(0.99d));
		add(new LOFPoint(1.00d));
		add(new LOFPoint(1.01d));
		add(new LOFPoint(1.02d));
		add(new LOFPoint(1.03d));
	
		add(new LOFPoint(50.00d));
	
		add(new LOFPoint(99.97d));
		add(new LOFPoint(99.98d));
		add(new LOFPoint(99.99d));
		add(new LOFPoint(100.00d));
		add(new LOFPoint(100.01d));
		add(new LOFPoint(100.02d));
		add(new LOFPoint(100.03d));
	}};
	
	public static void main(String...args) {
		
		System.out.println("starting");
		LocalOutlierFactor lof = new LocalOutlierFactor(3, smallConcentrated);
		HashMap<LOFPoint, Double> result = lof.LOF();
		System.out.println(result);
		
	
	}
	
}
