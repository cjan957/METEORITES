package com.ttcj.components;

import com.ttcj.testing.IPaddle;

public class Bat extends ObjectInfo implements IPaddle{
	
	private boolean isAIBat;
	private int batWidth;
	private int batLength;
	
	public void setXPos(int x) {
		this.SetxPosition(x);
	}

	public void setYPos(int y) {
		this.SetyPosition(y);
	}
	
	public void setAIBat(boolean AIBat){	
		this.isAIBat = AIBat; 
	}
	
	public boolean isAIBat (){	
		return this.isAIBat;
	}
	
	public void setBatWidth(int batWidth){
		this.batWidth = batWidth;
	}
	
	public int getBatWidth(){
		return this.batWidth;
	}
	
	public void setBatLength(int batLength){
		this.batLength = batLength;
	}
	
	public int getBatLength(){
		return this.batLength;
	}


}
