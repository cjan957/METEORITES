package com.ttcj.components;

public class Bat extends ObjectInfo{
	
	private boolean isAIBat;
	private int batWidth;
	private int batLength;
	
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
