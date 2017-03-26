package com.ttcj.components;

import javafx.scene.shape.*;

public class Brick extends ObjectInfo{
	
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
}
