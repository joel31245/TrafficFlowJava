// June 12, 2018 TODO: Reorder and Rename Variables that display unit information.

public class Lane {
	// VARIABLES
	    // Metric Variables (Input/Output Rates)
	    double inputRate, outptRate;
	    // Measurement Stats (Rates, Flow)
	    double flowRate, density, avgSpd, timeThrough;
	    // General Settings
	    double length, heading, spdLimit;
    
    
    // CONSTRUCTORS
	    public Lane(){
	        inputRate = 1.0;   outptRate = -1.0;
	        flowRate = 0;        density = 0;       avgSpd = 0; timeThrough = 0;
	        length = 5280.0;    heading = 90;  spdLimit = 35*5280.0/3600;
	    }
	    public Lane(double inptRate, double outputRate,
	                double flow, double densty, double averageSpeed, double timeThru,
	                double laneLength, double laneHeading, double laneSpeedLimit){
	        inptRate = inputRate;   outptRate = outputRate;
	        flowRate = flow;        density = densty;       avgSpd = averageSpeed; timeThrough = timeThru;
	        length = laneLength;    heading = laneHeading;  spdLimit = laneSpeedLimit;
	    }
    
  
	// METHODS
	public void display() {
		
	}
	    
	// GETTERS AND SETTERS
    public double getinputRate() { return inputRate; }
}
