/*
 * Description: This class sets the voltage and time outputs to the graph
 * Author: Alekhya T.M.S.K.    Email id: tmsk.alekhya@gmail.com
 * Author: Anupam Sanghi	   Email id: sanghi.anupam@gmail.com
 * 			
 * 
 */

package com.example.emulator8051;

public class VoltageOutput {
	
	/* declaring variables for setting output voltage and time to plot
	 * the graph goes here
	 */
	static double vout[] = new double[100];
	static int[] timeline = new int[100];
	static int counter = 0;
	
	//setting the values

	public static void setValues(double v, int t) {
		if (counter == 0 || t != timeline[counter]) {
			counter++;
			timeline[counter] = t;
			vout[counter] = v;
		}

	}
}
