/*
* Ball Class (Model)
*	Includes: Getter and Setter of Ball properties
*		    : Conditions checking when the ball hits
*			  the boundary.
*	This class extends ObjectInfo class for the purpose
*	of code reuse.
*/			

package com.ttcj.components;

import com.ttcj.testing.IBall;

public class Ball extends ObjectInfo implements IBall {

	private double destroyPower;
	private int ballRadius;

	private int xVelocity;
	private int yVelocity;

	/*
	 * Ball(double xPos, double yPos){ xPosition = xPos; yPosition = yPos; }
	 */

	//Get and Set Ball position on the screen by using co-ordinates
	//Some methods are added here to make the class consistent with IBall interface
	public void setXPos(int x) {
		this.SetxPosition(x);
	}

	public void setYPos(int y) {
		this.SetyPosition(y);
	}

	public int getXPos() {
		return this.GetxPosition();
	}

	public int getYPos() {
		return this.GetyPosition();
	}

	public void setXVelocity(int dX) {
		this.xVelocity = dX;
	}

	public void setYVelocity(int dY) {
		this.yVelocity = dY;
	}

	public int getXVelocity() {
		return this.xVelocity;
	}

	public int getYVelocity() {
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
	}

}
