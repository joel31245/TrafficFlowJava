// June 12, 2018 Reorder and rename variables.


public class Vehicle {
	
	// VARIABLES
    double lambda, vehV, maxAccel, comfortAccel, vehVMax,// movement data
           vehX,  // position data
           vehLgth;
    int followRungeType, accelDriveType, lnChangeType, ruleFollowType; // decision data 1(nice) - 60 (Aggressive) (range/mulitplying factors. 
    boolean crashflag = false;        
    
    // CONSTRUCTORS
    public Vehicle(){
        // Default constructor. 
        // Vehicle specific parameters. 
        lambda = 0.8; vehV = 0.0; maxAccel = 2.0*32.2; comfortAccel = 1.5*32.2; vehVMax = 120*5280/3600; vehX = 0.0; vehLgth = 10;
        followRungeType = 1; accelDriveType = 1; lnChangeType = 1; ruleFollowType = 1;
    }
    public Vehicle(double y, double velocity, double maxAcceleration, double comfortAcceleration, double maxVelocity,
                   double xPos, double vehLength, 
                   int followData, int aDType, int LaneData, int ruleData){
        lambda = y; vehV = velocity; maxAccel = maxAcceleration; comfortAccel = comfortAcceleration; vehVMax = maxVelocity;
        vehX = xPos; vehLgth = vehLength;
        followRungeType = followData; accelDriveType = aDType;
        lnChangeType = LaneData;      ruleFollowType = ruleData;
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
	        vStar = vehV + dt/2 * accelFunct(vehV, vehX, lambda, velInFront, xPosInFront, dt);
	        xStar = vehX + dt/2 * vehV;
	
	        vStar2 = vehV + dt/2 * accelFunct(vStar, xStar, lambda, velInFront, xPosInFront, dt);
	        xStar2 = vehX + dt/2 * vStar;
	
	        vStar3 = vehV + dt * accelFunct(vStar2, xStar, lambda, velInFront, xPosInFront, dt);
	        xStar3 = vehX + dt * vStar2;
	
	        vNew = vehV + dt/6 * (    accelFunct(vehV, vehX, lambda, velInFront, xPosInFront, dt)
	               + 2 * accelFunct(vStar, xStar, lambda, velInFront, xPosInFront, dt) 
	               + 2 * accelFunct(vStar2, xStar2, lambda, velInFront, xPosInFront, dt) 
	               + accelFunct(vStar3, xStar3, lambda, velInFront, xPosInFront, dt)    );
	        
	        xNew = vehX   +   dt/6*(vehV + 2*vStar + 2*vStar2 + vStar3);
	        
	        if(xNew >= xPosInFront + lengthVehinFront){
	            crashflag = true;
	            vehV = 0.0;
	            vehX = xNew;
	        }
	        else {
	        	vehV = vNew;
	        	vehX = xNew;
	        }
	        
	        return crashflag;
	    }
    		// Runge Kutta Default (no people up front)
    	public boolean rungKutta(double dt){
	        /*double vStar, vStar2, vStar3, vNew,
	               xStar, xStar2, xStar3, xNew;
	        
	        // Method runs Runge Kutta 4th Order for the current vehicle.
	        vStar = vehV + dt/2 * ;
	        xStar = vehX + dt/2 * vehV;
	
	        vStar2 = vehV + dt/2 * ;
	        xStar2 = vehX + dt/2 * vStar;
	
	        vStar3 = vehV + dt * ;
	        xStar3 = vehX + dt * vStar2;
	
	        vNew = vehV + dt/6 * 
	               + 2 *  
	               + 2 *  
	               +     );
	        
	        xNew = vehX   +   dt/6*(vehV + 2*vStar + 2*vStar2 + vStar3);
	        
	        if(xNew >= xPosInFront + lengthVehinFront){
	            crashflag = true;
	        }
	        */
	        
	        double vNew = vehV + dt*comfortAccel;
	        if(vNew > vehVMax) {
	            vNew = vehV;
	        }
	        double xNew = vehX + dt*vNew;
	        vehV = vNew; vehX = xNew;
	        
	        return crashflag;
    	}

    
    // STYLE SPECIFIC MOTION 
    	// Need driving style, rule following, lane changing type. 
        // Based on the rule following type of person changes max allowable acceleration
	    public void drivingStyle(){
	        maxAccel = 0.0;
	    }// move this into a global
	    public void ruleFollowing(){
	        
	    }
	        // Changes lanes based on how far the rear car in the other lane is.
	    public void laneChange(){
	        
	    }
    
    // GETTERS AND SETTERS
    public double getLambda(){return lambda;} public void setLambda(double y){lambda=y;}
    public double getvehV(){return vehV;} public void setvehV(double vel){vehV=vel;}
    public double getVehX(){return vehX;} public void setVehX(double xPos){vehX=xPos;}
    public double getVehLgth(){return vehLgth;} public void setVehLgth(double length){vehLgth=length;}
    public int getVehFollowTp(){return followRungeType;} public void setVehFollowTp(int fltp){followRungeType=fltp;}
    public int getVehLnChgTp(){return lnChangeType;} public void setVehLnChgTp(int lnchgtp){lnChangeType=lnchgtp;}
    public int getVehRuleTp(){return ruleFollowType;} public void setVehRuleTp(int rlfltp){ruleFollowType=rlfltp;}

}
