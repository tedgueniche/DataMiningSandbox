package utilities;

import java.util.HashMap;

public class Timer {

	private static HashMap<String, Long> timers = new HashMap<String, Long>();
	
	private static String output = "\n== Timer Statistics ==\n";
	
	public static void start(String name) {
		timers.put(name, System.currentTimeMillis());
	}
	
	public static void stop(String name) {
		stop(name, 1);
	}
	
	public static void stop(String name, int divider) {
		
		Long startTime = timers.get(name);
		
		if(startTime != null) {
		
			timers.remove(name);
			
			Long totalTime = System.currentTimeMillis() - startTime;
			System.out.println("Finished: "+ name);
			output += "Total "+ name + ": "+ totalTime + "ms, Per unit: "+ (totalTime / divider) +"ms\n";
		}
		
	}
	
	
	public static void printStats() {
		
		System.out.println(output);
	}
	
	
}
