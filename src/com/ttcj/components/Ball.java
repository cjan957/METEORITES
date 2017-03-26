package com.ttcj.components;

public class Ball extends ObjectInfo{
	
	private double destroyPower;
	private int ballRadius;
	
	/*
	Ball(double xPos, double yPos){
		xPosition = xPos;
		yPosition = yPos;
	}
	*/
	
	public double GetDestroyPower(){
		return this.destroyPower;
	}
	
	public void SetDestroyPower(double destroyPower){
		this.destroyPower = destroyPower; 
	}
	
	public void SetBallRadius(int ballRadius){  
		this.ballRadius = ballRadius;
	}
		
	public int GetBallRadius(){
		return this.ballRadius;
	}	
	
}
