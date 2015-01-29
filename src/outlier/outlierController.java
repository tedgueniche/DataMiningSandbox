package outlier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import distance.SVDistances;
import utilities.SV;

public class outlierController {

	/**Small set of dense data, outliers close*/
	private static List<SV> smallConcentrated = new ArrayList<SV>(){{
		add(new SV(2d));
		add(new SV(2.4d));
		add(new SV(2.6d));
		add(new SV(2.9d));
		add(new SV(3d));
		add(new SV(3.2d));
		add(new SV(3.3d));
		add(new SV(4d));
	}};
	
	/**Two dense clusters, very far away, point in the middle*/
	private static List<SV> biCluster = new ArrayList<SV>(){{
		add(new SV(0.97d));
		add(new SV(0.98d));
		add(new SV(0.99d));
		add(new SV(1.00d));
		add(new SV(1.01d));
		add(new SV(1.02d));
		add(new SV(1.03d));
	
		add(new SV(50.00d));
	
		add(new SV(99.97d));
		add(new SV(99.98d));
		add(new SV(99.99d));
		add(new SV(100.00d));
		add(new SV(100.01d));
		add(new SV(100.02d));
		add(new SV(100.03d));
	}};
	
	public static void main(String...args) {
		
		System.out.println("starting");
		LocalOutlierFactor lof = new LocalOutlierFactor(3, smallConcentrated, SVDistances::distPearson);
		HashMap<SV, Double> result = lof.LOF();
		System.out.println(result);
		
	
	}
	
}
