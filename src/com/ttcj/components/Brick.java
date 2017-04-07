/*Brick Class (Model)
*Includes: Getter and Setter of Brick properties
*	
*	This class extends ObjectInfo class for the purpose
*	of code reuse.
*/

package com.ttcj.components;

import javafx.geometry.Rectangle2D;

public class Brick extends ObjectInfo{

	//enum with 3 brick status
	private enum brickStatus { 
		STANDING, DAMAGED, DESTROYED 
	}
	
	//Specify whether a brick is laid out in horizontal or vertical direction 
	//0 for horizontal, 1 for vertical
	private int brickArrangement; 

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
	
	public void setArrangement(int arrangement){
		brickArrangement = arrangement;
	}
	
	public int getArrangement(){
		return brickArrangement;
	}
	
	public Rectangle2D getBoundary(){
		return new Rectangle2D(xPosition,yPosition,width+8,height);
	}

}
