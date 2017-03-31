package com.ttcj.components;

public class Base extends ObjectInfo{
	
	private boolean isDead;		//true for dead, false for alive
	private boolean isWinner;	//true for winner, false for loser/ undetermined
	
	public boolean isDead(){
		return this.isDead;
	}
	
	public boolean isWinner(){
		return this.isWinner;
		
	}
}
