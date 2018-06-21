import java.io.*;

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
            - Make a seperate CurveLane for nonlinear lanes.
            - Create text IO
            - Create GUI IO
            - Move rungeKuttas to main to decrease size and increase efficiency?
            - Start Assembly
            - Model I-10 section. 
   TODO (Donut Prize):  
        - Add obstructions (lights, accidents, slow drivers, etc)
        - Ability to switch imperial and metric.
*/

public class TrafficFlow {
	
	public static boolean testSingleLaneOps(Lane lane) {
		double dt = .1;
		double time = 0.0;
		boolean crashflag = false;
		
		for(time=0.0; time<60; time+=dt) {
			lane.autoAddInputRate(time);
			crashflag = lane.updateLaneDefault(dt);
		}
		
		return crashflag;
	}

    public static void main(String[] args) throws IOException {

        Lane testLane = new Lane();
        
        boolean crashTestSingleLane = testSingleLaneOps( testLane );
        if( crashTestSingleLane == true) 	System.out.println("Crash Reported for Single Ops Lane Test.");
        else								System.out.println("Single Ops successfully executed.");

        System.out.println("Succesful Execution. Terminating Program.");
    }
    
}
