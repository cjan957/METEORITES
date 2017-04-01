package com.ttcj.testing;
import com.ttcj.testing.IWarlord;

public class Base implements IWarlord{
	
	private int xPosition;
	private int yPosition;
	
	private boolean isDead = false;		//true for dead, false for alive
	private boolean isWinner = false;	//true for winner, false for loser/ undetermined
	
	public void setIsDead(boolean Dead){
		this.isDead = Dead;
	}
	
	public boolean isDead(){
		return this.isDead;
	}
	
	public void setIsWinner(boolean Winner){
		this.isWinner = Winner;
	}
	
	public void setXPos(int x) {
		this.xPosition = x;	
	}

	public void setYPos(int y) {
		this.yPosition = y;	
	}
	
	public int getXPos(){
		return this.xPosition;
	}
	
	public int getYPos(){
		return this.yPosition;
	}

	public boolean hasWon() {
		return this.isWinner;
	}
}
