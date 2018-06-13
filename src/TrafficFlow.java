/*
 * Simple speed diff creates difference in speed
 */
package TrafficFlow;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Joel Chavali
 * Start Date: April 18, 2018
 * 
 * // Results dictated by volume output for a specific road stretch plus time it takes for
   //     a single vehicle to cross boundaries.
   // Tool used to compare real traffic approximations. 
   //     All measurements in feet. [American System.
 */

/* TODO:    - Make initial library of functions. Vehicle specific like real life. 
            - Git functionality
            - Make Road making function
            - Start Assembly
            - Model I-10 section. 
   TODO (Donut Prize):  
        - Add obstructions (lights, accidents, slow drivers, etc)
        - Ability to switch imperial and metric.
*/

public class PrimitiveTrafficFlow {
	
	public void singleLaneOps(ArrayList<Vehicle> convoy, Lane l1) {
		double dt = .1;
		double time = 0.0;
		
		for(time=0.0; time<60; time+=dt) {
			// Adds the vehicle to the lanes and sets them on their way.
		}
		
	}

    public static void main(String[] args) throws IOException {
        // Just the assembly stage. Dictates the flowrates of each lane/entrance
        //  and outputs to a file / displays results. 
        Vehicle car = new Vehicle();
        ArrayList<Vehicle> laneInput = new ArrayList<Vehicle>;
        
        
        
        System.out.println("Hello. Car Made");
    }
    
}