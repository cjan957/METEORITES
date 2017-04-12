/*
* Powerup Class (Model)
*	Includes: Getter and Setter of Powerup properties
*
*	This class extends ObjectInfo class for the purpose
*	of code reuse.
*/		

package com.ttcj.components;

import javafx.geometry.Rectangle2D;

public class Powerup extends ObjectInfo {
	
	private boolean visible = false;

	public enum powerupType { // The modifier to apply to the game
		BAT_FROZEN, DECREASE_TIME;
	}

	private powerupType puModifier;
	
	
	public Powerup(String img, int xPos, int yPos, powerupType modifier){
		this.setImage(img);
		this.SetxPosition(xPos);
		this.SetyPosition(yPos);
		this.setpowerupModifier(modifier);
	}
	
	public void setpowerupModifier(powerupType Type) {
		puModifier = Type;
	}

	public powerupType getPowerupModifier() {
		return this.puModifier;
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public void setVisibility(boolean visible){
		this.visible = visible;
	}
	
	public Rectangle2D getBoundary(){
		return new Rectangle2D(xPosition,yPosition,width,height);
	}

}
