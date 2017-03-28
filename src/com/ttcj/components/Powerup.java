package com.ttcj.components;

public class Powerup extends ObjectInfo{

	private enum powerupType {   //for identifying type of powerup, maybe for colorcoordinating
		GOOD, NEUTRAL, BAD  
	}
	
	private enum powerupModifier {   // the modifier to apply to the game  
		BAT_SMALLER, BAT_LARGER, BAT_SLOWER, BAT_FASTER, BAT_FROZEN,
		BALL_SMALLER, BALL_BIGGER, BALL_SLOWER, BALL_FASTER, 	
		BASE_SMALLER, BASE_LARGER
	}
	
	private powerupType puType;
	private powerupModifier puModifier;
	
	public void setPowerupType(powerupType Type){ 
		puType = Type;
	}
	
	public powerupType getPowerupType(){
		return this.puType;
	}
	
	public void setpowerupModifier(powerupModifier Type){
		puModifier = Type;
	}
	
	public powerupModifier getPowerupModifier(){
		return this.puModifier;
	}
	
}
