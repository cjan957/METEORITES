package com.ttcj.components;

public class Bat extends ObjectInfo{
	
	private boolean isAIBat ;
	private int batWidth;
	private int batLength;
		
	private boolean isAIBat (){
		return this.isAIBat;
	}
	
	private void setAIBat(boolean AIBat ){
		this.isAIBat = AIBat; 
	}
	
	private void setBatWidth(int batWidth){
		this.batWidth = batWidth;
	}
	private void setBatLength(int batLength){
		this.batLength = batLength;
	}
	
}
