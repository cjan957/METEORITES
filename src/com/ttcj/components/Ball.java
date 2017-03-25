package com.ttcj.components;

public class Ball extends ObjectInfo{
	
	private int ballRadius;
	private double destroyPower;
	
	/*
	Ball(double xPos, double yPos){
		xPosition = xPos;
		yPosition = yPos;
	}
	*/
	
	private double GetDestroyPower(){
		return this.destroyPower;
	}
	
	private void SetDestroyPower(double destroyPower){
		this.destroyPower = destroyPower; 
	}
	
	private void SetBallRadius(int ballRadius){  
		this.ballRadius = ballRadius;
	}
		
	private int GetBallRadius(){
		return this.ballRadius;
	}
	
}
