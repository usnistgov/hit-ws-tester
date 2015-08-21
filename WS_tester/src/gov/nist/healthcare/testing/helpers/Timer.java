package gov.nist.healthcare.testing.helpers;

public class Timer {
	private static long time;
	
	public static void start(){
		time = System.currentTimeMillis();
	}
	
	public static long finish(){
		return System.currentTimeMillis() - time;
	}
}
