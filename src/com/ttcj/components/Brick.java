package com.ttcj.components;

import com.ttcj.testing.IWall;

public class Brick extends ObjectInfo implements IWall{
	
	private enum brickStatus{   //or maybe "brickHealth"
		STANDING, DAMAGED, DESTROYED  //more health levels or nah?
	}
	private brickStatus brickHealth;
	
	public void setBrickStatus(brickStatus brickHealth){
		this.brickHealth = brickHealth;
	}
	
	public brickStatus getBrickStatus(){
		 return this.brickHealth;
	}

	public void setXPos(int x) {
		this.SetxPosition(x);
	}

	public void setYPos(int y) {
		this.SetyPosition(y);
	}

	public boolean isDestroyed() {
		return false;
	}
}
