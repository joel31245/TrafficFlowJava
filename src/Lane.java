import java.util.ArrayList;
// June 12, 2018 TODO: Reorder and Rename Variables that display unit information. DONE
// June 16, 2018 TODO: Vehicles local x position. 

public class Lane {
	
	// VARIABLES
	    // Input Variables (Input/Output Rates)
	    double 	inputRate;		/* [car/s]	*/
	    // Measurement Stats (Rates, Flow)
	    double 	outputRate;	 	/* [car/s]	*/
	    double 	flowRate; 		/* [car/s]	*/
	    double 	density;		/* [car/s]	*/
	    double 	avgSpeed;			/* [ft/s]	*/
	    double 	timeThrough;	/* [s]		*/
	    // General Settings
	    double 	length;			/* [ft]		*/
	    int 	heading;		/* [degrees]*/
	    double 	speedLimit;		/* [ft/s]	*/
	    // Lane Attachment Points and Routing
	    boolean inputAttached;
	    boolean exitAttached;
	    boolean leftLaneChange;
	    boolean rightLaneChange;
	    // Cars assigned to this Lane
	    ArrayList<Vehicle> myCars = new ArrayList<Vehicle>();
    
    
    // CONSTRUCTORS
	    public Lane(){
	    	// Input Variables
	    	inputRate = 1.0;
	    	// Measurement Stats
	    	// General Settings
	    	length = 5280.0;
	    	heading = 90;
	    	speedLimit = 35*5280.0/3600.0;
	    	// Lane Attachment Points and Routing
	    	inputAttached = false;	exitAttached = false;
	    	leftLaneChange = false;	rightLaneChange = false;
	    }
	    public Lane(double inptRate, 
	                double laneLength, 	int laneHeading, 	double laneSpeedLimit,
	                boolean iAttach, 	boolean eAttach,	boolean leftCross, 	boolean rightCross){
	        // Input Variables
	    	inputRate = inptRate;
	    	// Measurement Stats
	    	// General Settings
	        length = laneLength;    
	        heading = laneHeading;  
	        speedLimit = laneSpeedLimit;
	        // Lane Attachment Points and Routing
	        inputAttached = iAttach;	exitAttached = eAttach;
	        leftLaneChange = leftCross;	rightLaneChange = rightCross;
	    }
    
  
	// METHODS
	    public void display() {
			
		}
	    //simply adds cars that get passed into the method to its individual arraylist
	    public void addVehicleToLane(Vehicle car) { myCars.add(car); }
	    public void removeVehicleFromLane(Vehicle car) { myCars.remove(car); }
	    public void autoRemoveVehicleFromLane() { // change to local xPos
	    	if(myCars.size()>1)
	    		if( myCars.get( 0 ).getxPos()>length )
	    				removeVehicleFromLane(myCars.get( 0 ));
	    }
	    public boolean detectCrash() {
	    	boolean crashDetected = false;
	    	
	    	// check if ArrayList is empty
	    	if(myCars.size() >= 2) {
	    		// check if overlapping positions. Start with presorted arrayList. Cars further down are first.
	    		for( int i=0; i<myCars.size()-1; i++ ) {
	    			if( myCars.get(i).getxPos() - myCars.get(i).getLength() < myCars.get(i+1).getxPos() )
	    				crashDetected = true;
	    		}
	    	}
	    	
	    	return crashDetected;
	    }
	    
	    
	// GETTERS AND SETTERS
	    public double getinputRate() 	{ return inputRate; }				public void setInputRate(double input)	{ inputRate = input; }
	    
	    public double getOutputRate() 	{ return outputRate; }				public void setOutputRate(double output) {outputRate = output; }
	    public double getflowRate() 	{ return flowRate; }				public void setFlowRate(double flow)	{ flowRate = flow; }
	    public double getDensity()		{ return density; }					public void setDensity(double den)		{ density = den; }
	    public double getAvgSpeed()		{ return avgSpeed; }				public void setAvgSpeed(double avgSpd)	{ avgSpeed = avgSpd; }
	    public double getTimeThrough()	{ return timeThrough; }				public void setTimeThrough(double time)	{ timeThrough = time; }
	    
	    public double getLength()		{ return length; }					public void setLength(double len)		{ length = len; }
	    public int 	  getHeading()		{ return heading; }					public void setHeading(int head)		{ heading = head; }
	    public double getSpeedLimit()	{ return speedLimit; }				public void setSpeedLimit(double spdLim){ speedLimit = spdLim; }
	    
	    public boolean getInputAttached()	{ return inputAttached; }		public void setInputAttach(boolean iA)	{ inputAttached = iA; }
	    public boolean getExitAttached()	{ return exitAttached; }		public void setExitAttach(boolean eA)	{ exitAttached = eA; }
	    public boolean getLeftLaneChange()	{ return leftLaneChange; }		public void setLeftLaneChange(boolean lX)	{ leftLaneChange = lX; }
	    public boolean getRightLaneChange()	{ return rightLaneChange; }		public void setRightLaneChange(boolean rX) 	{ rightLaneChange = rX; }
}
