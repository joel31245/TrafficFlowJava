// June 12, 2018 TODO: Reorder and rename variables that display unit information. DONE
// June 13, 2018 TODO: Make the driving style and set comfortable acceleration. 
// June 16, 2018 TODO: Make localxPos Workable for the lanes.


public class Vehicle {
	
	// VARIABLES
	    // Vehicle Real Motion
		double 	velocity;	/* [ft/s]	*/ 
		double 	xPos;		/* [ft]		*/
		double 	localxPos;	/* [ft]		*/
		int 	yLane;		/* [ID#] 	*/
		boolean crashflag;	/* stops veh*/
		// Vehicle Motion Limits
		double 	maxAccel;	/* [ft/s2]	*/
		double 	maxVel;		/* [ft/s]	*/
		double 	comfortAccel;/*[ft/s2]	*/
		// Vehicle Parameters
		double lambda;	/* [constant]	*/
		double length;	/* [ft] 	*/
		// Vehicle Styles and Rules
		int followRungeType;	/* [see accelFunct]	*/
		int accelDriveType;		/* [scale] may be replaced by ruleFollow*/
		int laneChangeType;		/* [scale] may be replaced by accelType	*/
		float ruleFollowType;		/* [scale]								*/
		
		
    // CONSTRUCTORS
		// Default Constructor
	    public Vehicle(){ 
	        // Vehicle Real Motion
	    	velocity = 0.0; 	xPos = 0.0;		localxPos = 0.0; 	yLane = 0; 		crashflag = false;
	        // Vehicle Motion Limits
	    	maxAccel = 2.0*32.2;	maxVel = 120*5280.0/3600.0;		comfortAccel = 1.5*32.2;
	    	// Vehicle Parameters
	    	lambda = 0.8;		length = 15;
	    	// Vehhicle Styles and Rules
	    	followRungeType = 1; accelDriveType = 1; laneChangeType = 1; ruleFollowType = 100;
	    }
	    // Specific Constructor
	    public Vehicle(double vel, double x, int yID, boolean crash,
	    			   double maxA, double 	maxV, double comfortA,
	    			   double lambdaY, double carLength,
	    			   int rungeType, int aDriveType, int lnChangeType, int ruleType){
	    	// Vehicle Real Motion
	    	velocity = vel; 	xPos = x; 	yLane = yID;	crashflag = crash;
	        // Vehicle Motion Limits
	    	maxAccel = maxA;	maxVel = maxV;	comfortAccel = comfortA;
	    	// Vehicle Parameters
	    	lambda = lambdaY;	length = carLength;
	    	// Vehhicle Styles and Rules
	    	followRungeType = rungeType; 	accelDriveType = aDriveType; 
	    	laneChangeType = lnChangeType; 	ruleFollowType = ruleType;
	    }
     
    
    // GENERAL MOTION METHODS
    	public double accelFunct( double velofCurr, double posofCurr, double y, double velInFront, double posInFront, double dt){
	        double vNew;
	        
	        switch (followRungeType) {
	        // Acceleration equation a = lambda*(velVehInFront - velCurrent)
	            case 0:{
	                vNew = y * (velInFront - velofCurr);
	                if(     (vNew-velofCurr)/dt < 0 && (vNew-velofCurr)/dt < -maxAccel  )
	                    vNew = velofCurr - maxAccel*dt;
	                else if( (vNew-velofCurr)/dt > 0 && (vNew-velofCurr)/dt > maxAccel  )
	                    vNew = velofCurr + maxAccel*dt;
	                break;
	            }
	            case 1:
	                vNew = y * ( posInFront - posofCurr);
	                if(     (vNew-velofCurr)/dt < 0 && (vNew-velofCurr)/dt < -maxAccel  )
	                    vNew = velofCurr - maxAccel*dt;
	                else if( (vNew-velofCurr)/dt > 0 && (vNew-velofCurr)/dt > maxAccel  )
	                    vNew = velofCurr + maxAccel*dt;
	                break;
	            case 2:
	                vNew = y/(posInFront - posofCurr) * (velInFront - velofCurr);
	                if(     (vNew-velofCurr)/dt < 0 && (vNew-velofCurr)/dt < -maxAccel  )
	                    vNew = velofCurr - maxAccel*dt;
	                else if( (vNew-velofCurr)/dt > 0 && (vNew-velofCurr)/dt > maxAccel  )
	                    vNew = velofCurr + maxAccel*dt;
	                break;
	            default:
	                vNew = 0.0;
	                if(     (vNew-velofCurr)/dt < 0 && (vNew-velofCurr)/dt < -maxAccel  )
	                    vNew = velofCurr - maxAccel*dt;
	                else if( (vNew-velofCurr)/dt > 0 && (vNew-velofCurr)/dt > maxAccel  )
	                    vNew = velofCurr + maxAccel*dt;
	                break;
	        }
	        
	        return vNew;
	    }
		    // runge kutta method outputs both but methods only output a single thing. Unless, it can change it immediately. 
		    // NOTE: Designed to update from front to back (x wise). NOT randomly nor any other order.
		    // Because method is specific to current vehicle, only need other veh params
    	public boolean rungKutta(double dt, double xPosInFront, 
	                                        double velInFront, 
	                                        double lengthVehinFront){
	        double vStar, vStar2, vStar3, vNew,
	               xStar, xStar2, xStar3, xNew;
	        
	        // Method runs Runge Kutta 4th Order for the current vehicle.
	        vStar = velocity + dt/2 * accelFunct(velocity, xPos, lambda, velInFront, xPosInFront, dt);
	        xStar = xPos + dt/2 * velocity;
	
	        vStar2 = velocity + dt/2 * accelFunct(vStar, xStar, lambda, velInFront, xPosInFront, dt);
	        xStar2 = xPos + dt/2 * vStar;
	
	        vStar3 = velocity + dt * accelFunct(vStar2, xStar, lambda, velInFront, xPosInFront, dt);
	        xStar3 = xPos + dt * vStar2;
	
	        vNew = velocity + dt/6 * (    accelFunct(velocity, xPos, lambda, velInFront, xPosInFront, dt)
	               + 2 * accelFunct(vStar, xStar, lambda, velInFront, xPosInFront, dt) 
	               + 2 * accelFunct(vStar2, xStar2, lambda, velInFront, xPosInFront, dt) 
	               + accelFunct(vStar3, xStar3, lambda, velInFront, xPosInFront, dt)    );
	        
	        xNew = xPos   +   dt/6*(velocity + 2*vStar + 2*vStar2 + vStar3);
	        
	        if(xNew >= xPosInFront + lengthVehinFront){
	            crashflag = true;
	            velocity = 0.0;
	            //localxPos = xNew - xPos;
	            xPos = xNew;
	        }
	        else {
	        	velocity = vNew;
	        	xPos = xNew;
	        }
	        
	        return crashflag;
	    }
    		// Runge Kutta Default (no people up front)
    	public boolean rungKutta(double dt){
	        /*double vStar, vStar2, vStar3, vNew,
	               xStar, xStar2, xStar3, xNew;
	        
	        // Method runs Runge Kutta 4th Order for the current vehicle.
	        vStar = velocity + dt/2 * ;
	        xStar = xPos + dt/2 * velocity;
	
	        vStar2 = velocity + dt/2 * ;
	        xStar2 = xPos + dt/2 * vStar;
	
	        vStar3 = velocity + dt * ;
	        xStar3 = xPos + dt * vStar2;
	
	        vNew = velocity + dt/6 * 
	               + 2 *  
	               + 2 *  
	               +     );
	        
	        xNew = xPos   +   dt/6*(velocity + 2*vStar + 2*vStar2 + vStar3);
	        
	        if(xNew >= xPosInFront + lengthVehinFront){
	            crashflag = true;
	        }
	        */
	        
	        double vNew = velocity + dt*comfortAccel;
	        if(vNew > maxVel) {
	            vNew = velocity;
	        }
	        double xNew = xPos + dt*vNew;
	        velocity = vNew; xPos = xNew;
	        
	        return crashflag;
    	}

    
    // STYLE SPECIFIC MOTION 
    	// Need driving style, rule following, lane changing type. 
        // Based on the rule following type of person changes max allowable acceleration
	    public void drivingStyle(){
	    	// make density dependent
	        maxAccel = 0.0;
	    }// move this into a global
	    	// sets the maximum velocity based on the rule following type.
	    public void determineMaxSpeed(Lane lane){
	    	/* make peer pressure dependent */
	    	// scale dependency first
	    	
	    	// check if in correct range
	    	if(ruleFollowType>1.95 || ruleFollowType<.95) {
	    		System.out.println("Incorrect entry for ruleFollowType. Must be in range of .95 to 1.95. Switching to default of 1.0");
	    		ruleFollowType = (float) 1.0;
	    	}
	    	else if(ruleFollowType>.95 && ruleFollowType<=1.0)
	    		maxVel = ruleFollowType*lane.getSpeedLimit();
	    	else if(ruleFollowType>1.0 && ruleFollowType<=1.7)
	    		maxVel = lane.getSpeedLimit() + (5*5280.0/3600.0);
	    		// add in a frequency factor for this. Most people don't always stay at +5 mph on open road
	    	else if(ruleFollowType>1.7 && ruleFollowType<=1.9)
	    		maxVel = lane.getSpeedLimit() + ((ruleFollowType%1.7)*10+10)*5280.0/3600.0;
	    	else 
	    		maxVel = lane.getSpeedLimit()*ruleFollowType;
	    }
	    public void laneChange(Lane currentLane, Lane newLane){
	        // make density dependent
	    	
	    }
    
	    
    // GETTERS AND SETTERS
    public double getVelocity(){return velocity;} 	public void setVelocity(double vel){velocity=vel;}
    public double getxPos(){return xPos;} 			public void setxPos(double x){xPos=x;}
    public double getlocalxPos() { return localxPos; }	public void setlocalxPos(double localx){localxPos=localx;}
    public int 	  getyLane(){return yLane;}			public void setyLane(int y){yLane=y;}
    public boolean getCrashFlag(){return crashflag;}public void setCrashFlag(boolean crash){crashflag=crash;}
    
    public double getMaxAccel() {return maxAccel;}
    public double getMaxVel() {return maxVel;}
    public double getComfortAccel() {return comfortAccel;}
    
    public double getLambda(){return lambda;}
    public double getLength(){return length;}
    
    public int getVehFollowType(){return followRungeType;}		public void setVehFollowType(int fltp){followRungeType=fltp;}
    public int getAccelDriveType(){return accelDriveType;}		public void setAccelDriveType(int aDT) {accelDriveType=aDT;}
    public int getVehLaneChangeType(){return laneChangeType;}	public void setVehLaneChangeType(int lnchgtp){laneChangeType=lnchgtp;}
    public float getVehRuleType(){return ruleFollowType;} 		public void setVehRuleType(float rlfltp){ruleFollowType=rlfltp;}

}
