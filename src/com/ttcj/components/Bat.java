/*Bat Class (Model)
*Includes: Getter and Setter of Bat properties
*
*	This class extends ObjectInfo class for the purpose
*	of code reuse.
*/

package com.ttcj.components;
import javafx.geometry.Rectangle2D;

public class Bat extends ObjectInfo {

	private boolean isAIBat;
	private int batWidth;
	private int batLength;
	
	public Bat(String img, int xPos, int yPos){
		this.setImage(img);
		this.SetxPosition(xPos);
		this.SetyPosition(yPos);
	}

	//Specify whether a bat is controlled by AI
	public void setAIBat(boolean AIBat) {
		this.isAIBat = AIBat;
	}

	public boolean isAIBat() {
		return this.isAIBat;
	}

	//Size of the bat can change in-game
	//when the player collects a powerup
	public void setBatWidth(int batWidth) {
		this.batWidth = batWidth;
	}

	public int getBatWidth() {
		return this.batWidth;
	}

	public void setBatLength(int batLength) {
		this.batLength = batLength;
	}

	public int getBatLength() {
		return this.batLength;
	}
	
	public Rectangle2D getBoundary(){
		return new Rectangle2D(xPosition,yPosition,width+8,height);
	}

}
