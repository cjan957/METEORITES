package com.ttcj.components;

import javafx.scene.canvas.GraphicsContext;

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
	
	public void moveThatBall(){
		xPosition += velocityX;
		yPosition += velocityY;
		if(xPosition < (0)){
			velocityX = -velocityX; 
			xPosition = 0 ;
		}
		else if(xPosition > (1024-64)){
			velocityX = -velocityX;
			xPosition = (1024-64);
		}
		if(yPosition < (0)){
			velocityY = -velocityY;
			yPosition = 0;
		}
		else if(yPosition > (768-64)){
			velocityY = -velocityY;
			yPosition = 768-64;
		}
	}
	
	
	
	
	
}
