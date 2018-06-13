import java.io.*;
import java.util.ArrayList;

/**
 * @author Joel Chavali
 * Start Date: April 18, 2018
 * 
 * // Results dictated by volume output for a specific road stretch plus time it takes for
   //     a single vehicle to cross boundaries.
   // Tool used to compare real traffic approximations. 
   //     All measurements in feet. [American System].
 */

/* TODO:    - WORKING June 12, 2018 - Make initial library of functions. Vehicle specific like real life. 
            - DONE June 12, 2018 - Git functionality 
            - Make Road making function
            - Start Assembly
            - Model I-10 section. 
   TODO (Donut Prize):  
        - Add obstructions (lights, accidents, slow drivers, etc)
        - Ability to switch imperial and metric.
*/

public class TrafficFlow {
	
	public void singleLaneOps(ArrayList<Vehicle> convoy, Lane lane) {
		double dt = .1;
		double time = 0.0;
		
		for(time=0.0; time<60; time+=dt) {
			// Adds the vehicle to the lanes and sets them on their way.
			if(lane.getinputRate()%dt == 0) {
				convoy.add(new Vehicle());
			}
			
		}
		
	}

    public static void main(String[] args) throws IOException {
        
        Vehicle car = new Vehicle();
        ArrayList<Vehicle> laneInput = new ArrayList<Vehicle>();
        Lane testLane = new Lane();
        
        System.out.println("Hello. Car Made");
    }
    
}
