package com.ttcj.components;
import com.ttcj.testing.IBall;

public class Ball extends ObjectInfo implements IBall{
	
	private double destroyPower;
	private int ballRadius;
	
	private int xVelocity;
	private int yVelocity;
	
	
	/*
	Ball(double xPos, double yPos){
		xPosition = xPos;
		yPosition = yPos;
	}
	*/
	
	//following the testing interface
	public void setXPos(int x){
		this.SetxPosition(x);
	}
	
	public void setYPos(int y){
		this.SetyPosition(y);
	}
	
	public int getXPos(){
		return this.GetxPosition();
	}
	
	public int getYPos(){
		return this.GetyPosition();
	}
	
	public void setXVelocity(int dX){
		this.xVelocity = dX;
	}
	
	public void setYVelocity(int dY){
		this.yVelocity = dY;
	}
	
	public int getXVelocity(){
		return this.getXVelocity();
	}
	
	public int getYVelocity(){
		return this.getYVelocity();
	}
	
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
		xPosition += xVelocity;
		yPosition += yVelocity;
		if(xPosition < (0)){
			xVelocity = -xVelocity; 
			xPosition = 0 ;
		}
		else if(xPosition > (1024-64)){
			xVelocity = -xVelocity;
			xPosition = (1024-64);
		}
		if(yPosition < (0)){
			yVelocity = -yVelocity;
			yPosition = 0;
		}
		else if(yPosition > (768-64)){
			yVelocity = -yVelocity;
			yPosition = 768-64;
		}
	}
	
	
	
	
	
}
