/*
* Ball Class (Model)
*	Includes: Getter and Setter of Ball properties
*		    : Conditions checking when the ball hits
*			  the boundary.
*	This class extends ObjectInfo class for the purpose
*	of code reuse.
*/			

package com.ttcj.components;
import java.util.Random;

import javafx.geometry.Rectangle2D;

public class Ball extends ObjectInfo{

	private double destroyPower;
	private int ballRadius;

	private float xVelocity;
	private float yVelocity;
	
	private boolean travelingRight;
	private boolean travelingUp;
	
	public Ball(String img, int xPos, int yPos, int width, int height){
		
		//Set the ball up with necessary properties
		this.setImage(img);
		this.SetxPosition(xPos);
		this.SetyPosition(yPos);
		
		Random rand = new Random();
		int n = rand.nextInt(4) + 1;
		
		//Random the velocity 
		switch(n){
			case 1: this.setXVelocity(5); this.setYVelocity(5);
					break;
			case 2: this.setXVelocity(-5); this.setYVelocity(-5);
					break;
			case 3: this.setXVelocity(5); this.setYVelocity(-5);
					break;
			case 4: this.setXVelocity(-5); this.setYVelocity(5);
					break;	
		}
		this.SetWidth(width);
		this.SetHeight(height);
	}
	

	public void setXVelocity(float dX) {
		this.xVelocity = dX;
	}

	public void setYVelocity(float dY) {
		this.yVelocity = dY;
	}

	public float getXVelocity() {
		return this.xVelocity;
	}

	public float getYVelocity() {
		return this.yVelocity;
	}

	public double getDestroyPower() {
		return this.destroyPower;
	}

	public void setDestroyPower(double destroyPower) {
		this.destroyPower = destroyPower;
	}

	public void setBallRadius(int ballRadius) {
		this.ballRadius = ballRadius;
	}

	public int getBallRadius() {
		return this.ballRadius;
	}

	public void moveThatBall() {
		//Move the ball by adding the X and Y velocity to
		//its current position. The result will be the position
		//of the ball after 1 tick 	
		xPosition += xVelocity;
		yPosition += yVelocity;
		
		//Check screen boundary, deflect the ball (by changing
		//the velocity) when it hits the boundary
		
		//LHS of the screen
		if (xPosition < (0)) {
			xVelocity = -xVelocity;
			xPosition = 0;
			
		//RHS
		} else if (xPosition > (1024 - 64)) {
			xVelocity = -xVelocity;
			xPosition = (1024 - 64);
		}
		
		//TOP
		if (yPosition < (0)) {
			yVelocity = -yVelocity;
			yPosition = 0;
			
		//BOTTOM
		} else if (yPosition > (768 - 64)) {
			yVelocity = -yVelocity;
			yPosition = 768 - 64;
		}	
		
		setBallTravelingDirection();
	}
	

	public void setBallTravelingDirection(){
		if(xVelocity < 0 && yVelocity < 0){
			travelingRight = false;
			travelingUp = true;
		}
		else if(xVelocity > 0 && yVelocity < 0){
			travelingRight = true;
			travelingUp = true;
		}
		else if(xVelocity < 0 && yVelocity > 0){
			travelingRight = false;
			travelingUp = false;
		}
		else if(xVelocity > 0 && yVelocity >0){
			travelingRight = true;
			travelingUp = false;
		}
		else{
			System.out.println("Velocity Error detected!");
		}
	}
	
	public boolean getIfTravelRight(){
		return travelingRight;
	}
	
	public boolean getIfTravelUp(){
		return travelingUp;
	}
	
	
	public Rectangle2D getBoundary(){
		return new Rectangle2D(xPosition+16,yPosition+16,width,height);
	}
	
	public boolean objectsIntersect(Brick obj){
		return obj.getBoundary().intersects(this.getBoundary());
	}
	
	public boolean objectsIntersectBallAndPaddle(Bat obj){
		return obj.getBoundary().intersects(this.getBoundary());
	}
	
	
	

}
