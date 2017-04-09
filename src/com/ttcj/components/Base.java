/*
* Base Class (Model)
*	Includes: Getter and Setter of Base properties
*
*	This class extends ObjectInfo class for the purpose
*	of code reuse.
*/

package com.ttcj.components;

import com.ttcj.testing.IWarlord;

import javafx.geometry.Rectangle2D;

public class Base extends ObjectInfo {

	private boolean isDead; // true for dead, false for alive
	private boolean isWinner; // true for winner, false for loser/ undetermined
	private String baseName;
	
	
	
	public Base(String baseName, String img,int xPos, int yPos, int width, int height){
		this.setBaseName(baseName);
		this.setImage(img);
		this.SetxPosition(xPos);
		this.SetyPosition(yPos);
		this.SetWidth(width);
		this.SetHeight(height);
	}
	
	public void setBaseName(String name){
		this.baseName = name;
	}
	
	public String getBaseName(){
		return this.baseName;
	}
	

	public void setIsDead(boolean Dead) {
		this.isDead = Dead;
	}

	public boolean isDead() {
		return this.isDead;
	}

	public void setIsWinner(boolean Winner) {
		this.isWinner = Winner;
	}

	public boolean isWinner() {
		return this.isWinner;

	}

	public boolean hasWon() {
		//Not yet implemented
		return false;
	}
	
	public Rectangle2D getBoundary(){
		return new Rectangle2D(xPosition,yPosition,width,height);
	}
}
