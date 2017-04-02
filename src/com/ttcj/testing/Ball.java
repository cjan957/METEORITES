/*
 * For more info on this class, please refer to the actual game ball model/class.
 */

package com.ttcj.testing;

import com.ttcj.testing.IBall;

public class Ball implements IBall {

	private int xPosition;
	private int yPosition;

	private int xVelocity;
	private int yVelocity;

	//These booleans determine the direction
	//of the ball.
	boolean travelsRight;
	boolean travelsDown;

	public void setXPos(int x) {
		this.xPosition = x;
	}

	public void setYPos(int y) {
		this.yPosition = y;
	}

	public int getXPos() {
		return this.xPosition;
	}

	public int getYPos() {
		return this.yPosition;
	}

	public void setXVelocity(int dX) {
		this.xVelocity = dX;
		if (dX >= 0) {
			travelsRight = true;
		} else {
			travelsRight = false;
		}
	}

	public void setYVelocity(int dY) {
		this.yVelocity = dY;
		if (dY >= 0) {
			travelsDown = true;
		} else {
			travelsDown = false;
		}
	}

	public boolean IsTravelRight() {
		return travelsRight;
	}

	public boolean IsTravelDown() {
		return travelsDown;
	}

	public int getXVelocity() {
		return this.xVelocity;
	}

	public int getYVelocity() {
		return this.yVelocity;
	}

	public void moveThatBall() {
		xPosition += xVelocity;
		yPosition += yVelocity;
	}
}
