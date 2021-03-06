// June 12, 2018 TODO: [DONE] Reorder and rename variables that display unit information. DONE
// June 13, 2018 TODO: [DONE] Make the driving style and set comfortable acceleration. 
// June 16, 2018 TODO: [DONE] Make localxPos Workable for the lanes.

// [1] make density dependent (ruleFollowing), [2] make +5 mph frequency variable (ruleFollowing)

public class Vehicle {
	
	// VARIABLES
	    // Vehicle Real Motion
		double 	velocity;	/* [ft/s]	*/ 
		double 	xPos;		/* [ft]		*/
		double 	localxPos;	/* [ft]		*/
		double 	yPos;		/* [ft] 	*/
		int 	heading;	/* [degrees]*/ 				// only if needed.
		boolean crashflag;	/* stops veh*/
		// Vehicle Motion Limits
		final double MAXACCEL = 4.0*32.3;	/* [ft/s2]	*/ 	/* MAXIMUM G: 4 g's */
		double 	maxVel;		/* [ft/s]	*/
		double 	comfortAccel;/*[ft/s2]	*/
		// Vehicle Parameters
		double lambda;		/* [constant]*/
		double length;		/* [ft] 	*/
		// Vehicle Styles and Rules
		int followRungeType;	/* [see accelFunct]	*/
		float accelDriveType;	/* [scale] may be replaced by ruleFollow*/
		int laneChangeType;		/* [scale] may be replaced by accelType	*/
		float ruleFollowType;	/* [scale]								*/
		
		
    // CONSTRUCTORS
		// Default Constructor
	    public Vehicle(){ 
	        // Vehicle Real Motion
	    	velocity = 0.0; 	xPos = 0.0;		localxPos = 0.0; 	yPos = 0.0; 		crashflag = false;
	        // Vehicle Motion Limits
	    	maxVel = 120*5280.0/3600.0;		comfortAccel = 1.5*32.2;
	    	// Vehicle Parameters
	    	lambda = 0.8;		length = 15;
	    	// Vehhicle Styles and Rules
	    	followRungeType = 1; accelDriveType = 1; laneChangeType = 1; ruleFollowType = 100;
	    }
	    // Specific Constructor
	    public Vehicle(double vel, double x, double y, boolean crash,
	    			   double 	maxV, double comfortA,
	    			   double lambdaY, double carLength,
	    			   int rungeType, int aDriveType, int lnChangeType, int ruleType){
	    	// Vehicle Real Motion
	    	velocity = vel; 	xPos = x; 	yPos = y;	crashflag = crash;
	        // Vehicle Motion Limits
	    	maxVel = maxV;	comfortAccel = comfortA;
	    	// Vehicle Parameters
	    	lambda = lambdaY;	length = carLength;
	    	// Vehhicle Styles and Rules
	    	followRungeType = rungeType; 	accelDriveType = aDriveType; 
	    	laneChangeType = lnChangeType; 	ruleFollowType = ruleType;
	    }
     
	    
	 // STYLE SPECIFIC MOTION 
        // Based on the rule following type of person changes max allowable acceleration
	    public void drivingStyle(){
	    	// make density dependent
	    	
	    	// check if in correct range
	    	if(accelDriveType>1.95 || accelDriveType<.95) {
	    		System.out.println("Incorrect entry for comfort acceleration. Must be in range of .95 to 1.95. Switching to default of 1.0");
	    		accelDriveType = (float) 1.0;
	    	}
	    	else if(accelDriveType>.95 && accelDriveType<=1.0)
	    		comfortAccel = accelDriveType*32.2;
	    	else if(accelDriveType>1.0 && accelDriveType<=1.7)
	    		comfortAccel = accelDriveType*accelDriveType*32.2;
	    	else if(accelDriveType>1.7 && accelDriveType<=1.9)
	    		comfortAccel = (accelDriveType-1)*MAXACCEL;
	    	else 
	    		comfortAccel = accelDriveType*32.2;
	    }
	    	// sets the maximum velocity based on the rule following type.
	    public void determineMaxSpeed(double speedLimit){
	    	/* make peer pressure dependent */
	    	// scale dependency first
	    	
	    	// check if in correct range
	    	if(ruleFollowType>1.95 || ruleFollowType<.95) {
	    		System.out.println("Incorrect entry for ruleFollowType. Must be in range of .95 to 1.95. Switching to default of 1.0");
	    		ruleFollowType = (float) 1.0;
	    	}
	    	else if(ruleFollowType>.95 && ruleFollowType<=1.0)
	    		maxVel = ruleFollowType*speedLimit;
	    	else if(ruleFollowType>1.0 && ruleFollowType<=1.7)
	    		maxVel = speedLimit + (5*5280.0/3600.0);
	    		// add in a frequency factor for this. Most people don't always stay at +5 mph on open road
	    	else if(ruleFollowType>1.7 && ruleFollowType<=1.9)
	    		maxVel = speedLimit + ((ruleFollowType%1.7)*10+10)*5280.0/3600.0;
	    	else 
	    		maxVel = speedLimit*ruleFollowType;
	    }
	    public void laneChange(Lane currentLane, Lane newLane){
	        // make density dependent
	    	
	    }
    
	    
    // GENERAL MOTION METHODS
    	public double accelFunct( double velofCurr, double posofCurr, double y, double velInFront, double posInFront, double dt){
	        double vNew;
	        double a;
	        
	        if( followRungeType == 0) 		vNew = y * (velInFront - velofCurr);
	        else if( followRungeType == 1)	vNew = y * ( posInFront - posofCurr);
	        else if( followRungeType == 2)	vNew = y/(posInFront - posofCurr) * (velInFront - velofCurr);
	        else 							vNew = 0.0;
	        
	        a = (vNew-velofCurr)/dt;
	        
	        if( a<0 ) {
	        	// uses basic rule of keep comfortable if separation distance is spd/10*carlengths.. 7.0ft/s is 10mph
	        	if( a<-comfortAccel 	&& (posInFront-posofCurr)<Math.abs(vNew/7.0*length) 	&& a<-MAXACCEL )
	        		vNew = velofCurr - MAXACCEL*dt;
	        	else if( a<-comfortAccel 	&& (posInFront-posofCurr)<Math.abs(vNew/7.0*length) )
	        		vNew = velofCurr - comfortAccel*dt;
	        }
	        if( a>0 ) {
	        	// uses basic rule of keep comfortable if separation distance is spd/10*carlengths.. 7.0ft/s is 10mph
	        	if( a>comfortAccel 		&& Math.abs(posInFront-posofCurr)<Math.abs(vNew/7.0*length) 	&& a>MAXACCEL )
	        		vNew = velofCurr + MAXACCEL*dt;
	        	else if( a>comfortAccel 	&& Math.abs(posInFront-posofCurr)<Math.abs(vNew/7.0*length) )
	        		vNew = velofCurr + comfortAccel*dt;
	        }
	        else	vNew = velofCurr;
	        
	        return vNew;
	    }
		    // NOTE: Designed to update from front to back (localxPos wise). NOT randomly nor any other order.
		    // Because method is specific to current vehicle, only need other veh params
    	public boolean rungKutta(int hdg, double dt, double xPosInFront, 
	                                        double velInFront, 
	                                        double lengthVehinFront){
	        double vStar, vStar2, vStar3, vNew,
	               xStar, xStar2, xStar3, xNew;
	        
	        // Method runs Runge Kutta 4th Order for the current vehicle.
	        vStar = velocity + dt/2 * accelFunct(velocity, localxPos, lambda, velInFront, xPosInFront, dt);
	        xStar = localxPos + dt/2 * velocity;
	
	        vStar2 = velocity + dt/2 * accelFunct(vStar, xStar, lambda, velInFront, xPosInFront, dt);
	        xStar2 = localxPos + dt/2 * vStar;
	
	        vStar3 = velocity + dt * accelFunct(vStar2, xStar, lambda, velInFront, xPosInFront, dt);
	        xStar3 = localxPos + dt * vStar2;
	
	        vNew = velocity + dt/6 * (    accelFunct(velocity, localxPos, lambda, velInFront, xPosInFront, dt)
	               + 2 * accelFunct(vStar, xStar, lambda, velInFront, xPosInFront, dt) 
	               + 2 * accelFunct(vStar2, xStar2, lambda, velInFront, xPosInFront, dt) 
	               + accelFunct(vStar3, xStar3, lambda, velInFront, xPosInFront, dt)    );
	        
	        xNew = localxPos   +   dt/6*(velocity + 2*vStar + 2*vStar2 + vStar3);
	        
	        if(vNew>maxVel)   vNew = maxVel;
	        if(xNew >= xPosInFront + lengthVehinFront){
	            crashflag = true;
	            velocity = 0.0;
		        // determining x,y coordinates using heading
	            	heading = hdg;
	            	xPos = Math.abs(xNew-localxPos) * Math.cos( Math.toRadians(heading));
	            	yPos = Math.abs(xNew-localxPos) * Math.sin( Math.toRadians(heading));
	            localxPos = xNew;
	        }
	        else {
	        	velocity = vNew;
	        	// determining x,y coordinates using heading
            		heading = hdg;
            		xPos = Math.abs(xNew-localxPos) * Math.cos( Math.toRadians(heading));
            		yPos = Math.abs(xNew-localxPos) * Math.sin( Math.toRadians(heading));
            	localxPos = xNew;
	        }
	        
	        return crashflag;
	    }
    		// Runge Kutta Default (no people within range up front)
    	public void rungKutta(int hdg, double dt){
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
	        velocity = vNew; 
		     // determining x,y coordinates using heading
	        	heading = hdg;
	        	xPos = Math.abs(xNew-localxPos) * Math.cos( Math.toRadians(heading));
	        	yPos = Math.abs(xNew-localxPos) * Math.sin( Math.toRadians(heading));
	        localxPos = xNew;
	        
	        //return crashflag;
    	}

	    
    // GETTERS AND SETTERS
    public double getVelocity(){return velocity;} 	public void setVelocity(double vel){velocity=vel;}
    public double getxPos(){return xPos;} 			public void setxPos(double x){xPos=x;}
    public double getlocalxPos() { return localxPos; }	public void setlocalxPos(double localx){localxPos=localx;}
    public double getyPos(){return yPos;}			public void setyPos(int y){yPos=y;}
    public boolean getCrashFlag(){return crashflag;}public void setCrashFlag(boolean crash){crashflag=crash;}
    
    public double getMaxAccel() {return MAXACCEL;}
    public double getMaxVel() {return maxVel;}
    public double getComfortAccel() {return comfortAccel;}
    
    public double getLambda(){return lambda;}
    public double getLength(){return length;}
    
    public int getVehRungeType(){return followRungeType;}		public void setVehRungeType(int runge){followRungeType=runge;}
    public float getAccelDriveType(){return accelDriveType;}	public void setAccelDriveType(float aDT) {accelDriveType=aDT;}
    public int getVehLaneChangeType(){return laneChangeType;}	public void setVehLaneChangeType(int lnchgtp){laneChangeType=lnchgtp;}
    public float getVehRuleType(){return ruleFollowType;} 		public void setVehRuleType(float rlfltp){ruleFollowType=rlfltp;}

}
