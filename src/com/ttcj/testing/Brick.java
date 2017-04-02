/*
 * For more info on this class, please refer to the actual game brick model/class.
 */

package com.ttcj.testing;

import com.ttcj.testing.IWall;

public class Brick implements IWall {

	private int xPosition;
	private int yPosition;

	private boolean destroyed;

	private enum brickStatus { // or maybe "brickHealth"
		STANDING, DAMAGED, DESTROYED // more health levels or nah?
	}

	private brickStatus brickHealth;

	public void setBrickStatus(brickStatus brickHealth) {
		this.brickHealth = brickHealth;
	}

	public brickStatus getBrickStatus() {
		return this.brickHealth;
	}

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

	public boolean isDestroyed() {
		return this.destroyed;
	}

	public void setDestoryed() {
		this.destroyed = true;
	}
}
