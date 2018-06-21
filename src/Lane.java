import java.util.ArrayList;
import java.lang.Math;
// June 12, 2018 TODO: [DONE] Reorder and Rename Variables that display unit information. DONE
// June 16, 2018 TODO: [DONE] Vehicles local x position. 
// June 20, 2018 TODO: How to make Lanes communicate with each other for vehicles at end of one and beg. of other

public class Lane {
	
	// VARIABLES
	    // Input Variables (Input/Output Rates)
	    double 	inputRate;		/* [car/s]	*/
	    double 	inputSpeed;		/* [ft/s] 	*/
	    // Measurement Stats (Rates, Flow)
	    double 	outputRate;	 	/* [car/s]	*/
	    double 	flowRate; 		/* [car/s]	*/
	    double 	density;		/* [car/s]	*/
	    double 	avgSpeed;			/* [ft/s]	*/
	    double 	timeThrough;	/* [s]		*/
	    // General Settings
	    double 	length;			/* [ft]		*/
	    int 	heading;		/* [degrees]*/ 			// ( 0, 90, 180, 270) (north, east, south, west)
	    double 	speedLimit;		/* [ft/s]	*/
	    // Lane Attachment Points and Routing
	    boolean inputAttached;
	    boolean exitAttached;
	    boolean leftLaneChange;
	    boolean rightLaneChange;
	    // Cars assigned to this Lane
	    ArrayList<Vehicle> myCars = new ArrayList<Vehicle>();
	    	// NOTE: Requires a special order. First in ArrayList is furthest down (highest localxPos).
    
    
    // CONSTRUCTORS
	    public Lane(){
	    	// Input Variables
	    	inputRate = 1.0;
	    	inputSpeed = 35*5280.0/3600.0;
	    	// Measurement Stats
	    	// General Settings
	    	length = 5280.0;
	    	heading = 90;
	    	speedLimit = 35*5280.0/3600.0;
	    	// Lane Attachment Points and Routing
	    	inputAttached = false;	exitAttached = false;
	    	leftLaneChange = false;	rightLaneChange = false;
	    }
	    public Lane(double inptRate, double inptSpd,
	                double laneLength, 	int laneHeading, 	double laneSpeedLimit,
	                boolean iAttach, 	boolean eAttach,	boolean leftCross, 	boolean rightCross){
	        // Input Variables
	    	inputRate = inptRate;
	    	inputSpeed = inptSpd;
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
	    // adds cars that get passed into the method to its individual arraylist and SORTS
	    public void addVehicleToLane(Vehicle car, double localx) { 
	    	myCars.add(car);
	    	myCars.get( myCars.size()-1 ).setlocalxPos(localx);
	    	
	    	// sorting block puts car farthest down the lane (highest localxPos) first in the arrayList
	    	if(myCars.size()>1 && myCars.get( myCars.size()-1 ).getlocalxPos()>5 ) {
	    		Vehicle temp;
	    		for(int i=0; i<myCars.size(); i++)
	    			for(int j=0; j<i-1; j++)
	    				if( myCars.get(j).getlocalxPos() < myCars.get(j+1).getlocalxPos() ) {
	    					temp = myCars.get(j);
	    					myCars.set(j, myCars.get(j+1) );
	    					myCars.set(j+1, temp);
	    				}
	    	} // end of sorting block
	    }
	    public void autoAddInputRate(double time) {
	    	boolean addCar = false;
	    	
	    	if( time%inputRate == 0.0 ) {
		    	if( myCars.size() >= 1 ) {
		    		int i = myCars.size()-1;
		    		if( myCars.get(i).getlocalxPos() - myCars.get(i).getLength() > speedLimit/7*myCars.get(i).getLength() )
		    			addCar = true;
		    	}
		    	else
		    		addCar = true;
	    	}
	    	
	    	if(addCar == true) {
	    		Vehicle car = new Vehicle();
    			addVehicleToLane( car, 0.0 );
    			myCars.get( myCars.size()-1 ).setVelocity( inputSpeed );
    			myCars.get( myCars.size()-1 ).setVehRuleType(  (float)(Math.random()+.95)  );
    			myCars.get( myCars.size()-1 ).setAccelDriveType(  (float)(Math.random()+.95)  ); 
    			
    			myCars.get( myCars.size()-1 ).determineMaxSpeed(speedLimit);
    			myCars.get( myCars.size()-1 ).drivingStyle();
    			addCar = false;
	    	}
	    	
	    }
	    public void removeVehicleFromLane(Vehicle car) { myCars.remove(car); }
	    public void autoRemoveVehicleFromLane() { // change to local xPos
	    	if(myCars.size()>1)
	    		if( myCars.get( 0 ).getlocalxPos()>length )
	    				removeVehicleFromLane(myCars.get( 0 ));
	    }
	    public boolean detectCrash() { 				// Backup tester if it needs to be tested at any time. Needs presorted arrayList
	    	boolean crashDetected = false;
	    	
	    	// check if ArrayList is empty
	    	if(myCars.size() >= 2) {
	    		// check if overlapping positions.
	    		for( int i=0; i<myCars.size()-1; i++ ) {
	    			if( myCars.get(i).getxPos() - myCars.get(i).getLength() < myCars.get(i+1).getxPos() )
	    				crashDetected = true;
	    		}
	    	}
	    	
	    	return crashDetected;
	    }
	    public boolean updateLaneDefault(double dt) {
	    	boolean crash = false;
	    	if( myCars.size()>0 ) {
	    		
	    		myCars.get(0).rungKutta(heading, dt);
	    		if( myCars.size()>1) {
	    			// Step 1 is check distances between cars. > 10 seconds no forward checker
	    			for( int i=1; i<myCars.size(); i++) {
	    				Vehicle thisCar = myCars.get(i);
	    				Vehicle frontCar = myCars.get(i-1);
	    				if( thisCar.getlocalxPos() - ( frontCar.getlocalxPos()+frontCar.getLength() )  <= thisCar.getLength()*10 )
	    					crash = thisCar.rungKutta(heading, dt, frontCar.getlocalxPos(), frontCar.getVelocity(), frontCar.getLength() );
	    				else
	    					thisCar.rungKutta(heading, dt);
	    			}
	    		} // end of multiple cars in lane
	    	}
	    	return crash;
	    }
	    
	    
	// GETTERS AND SETTERS
	    public double getinputRate() 	{ return inputRate; }				public void setInputRate(double input)	{ inputRate = input; }
	    public double getinputSpeed()	{ return inputSpeed; }				public void setInputSpeed(double s) 	{ inputSpeed = s; }
	    
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
