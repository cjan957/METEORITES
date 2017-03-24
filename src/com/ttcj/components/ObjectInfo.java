package com.ttcj.components;

/*
 * ObjectInfo is a superclass of game components (Ball,Wall etc). It 
 * has all common elements a game component needs such as X and Y 
 * positions, object's speed and size.
 */

public class ObjectInfo {
	
	protected double xPosition;
	protected double yPosition;
	
	protected double size;
	protected double velocity;	
	protected boolean visible;
	
	//const
	ObjectInfo(){
		
	}
	
	public double GetxPosition(){
		return this.xPosition;
	}
	
	public void SetxPosition(double xPos){
		this.xPosition = xPos;
	}
	
	public double GetyPosition(){
		return this.yPosition;
	}
	
	public void SetyPosition(double yPos){
		this.yPosition = yPos;
	}
	
	public double GetSize(){
		return this.size;
	}
	
	public void SetSize(double size){
		this.size = size;
	}
	
	public double GetVelocity(){
		return this.velocity;
	}
	
	public void SetVelocity(double velocity){
		this.velocity = velocity;
	}
	
	public boolean GetVisibilityStatus(){
		return this.visible;
	}
	
	public void SetVisibilityStatus(boolean visibility){
		this.visible = visibility;
	}
	
}
