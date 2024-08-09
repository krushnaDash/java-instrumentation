package org.krushna.java.application;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Start of the main application");
		int i=0;
		while (true) {
			 
			calculatePi((int)(Math.random() * 3000 + 1),++i);
		}
	}
	
	public static void calculatePi(int milis, int counter) {
		System.out.println("Started calculating PI for " + counter);
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("PI caluclation Completed for "+ counter);
		
	}
}
