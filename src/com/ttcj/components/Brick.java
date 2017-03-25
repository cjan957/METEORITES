package com.ttcj.components;

import javafx.scene.shape.*;

public class Brick extends ObjectInfo{
	
	private enum brickStatus{   //or maybe "brickHealth"
		STANDING, DAMAGED, DESTROYED  //more health levels or nah?
	}
	private brickStatus brickHealth;
	
	private void setBrickStatus(brickStatus brickHealth){
		this.brickHealth = brickHealth;
	}
	
	private brickStatus getBrickStatus(){
		 return this.brickHealth;
	}
}
