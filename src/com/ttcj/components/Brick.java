/*Brick Class (Model)
*Includes: Getter and Setter of Brick properties
*	
*	This class extends ObjectInfo class for the purpose
*	of code reuse.
*/

package com.ttcj.components;

import com.ttcj.testing.IWall;

public class Brick extends ObjectInfo implements IWall {

	//enum with 3 brick status
	private enum brickStatus { 
		STANDING, DAMAGED, DESTROYED 
	}

	private brickStatus brickHealth;

	public void setBrickStatus(brickStatus brickHealth) {
		this.brickHealth = brickHealth;
	}

	public brickStatus getBrickStatus() {
		return this.brickHealth;
	}

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

	public boolean isDestroyed() {
		return false;
	}
}
