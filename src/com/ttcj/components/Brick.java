/*Brick Class (Model)
*Includes: Getter and Setter of Brick properties
*	
*	This class extends ObjectInfo class for the purpose
*	of code reuse.
*/

package com.ttcj.components;

public class Brick extends ObjectInfo{

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

	public boolean isDestroyed() {
		return false;
	}
}
