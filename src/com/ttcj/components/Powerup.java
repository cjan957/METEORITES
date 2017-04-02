/*
* Powerup Class (Model)
*	Includes: Getter and Setter of Powerup properties
*
*	This class extends ObjectInfo class for the purpose
*	of code reuse.
*/		

package com.ttcj.components;

public class Powerup extends ObjectInfo {

	private enum powerupType { // for identifying type of Power up.
		GOOD, NEUTRAL, BAD
	}

	private enum powerupModifier { // The modifier to apply to the game
		BAT_SMALLER, BAT_LARGER, BAT_SLOWER, BAT_FASTER, BAT_FROZEN, BALL_SMALLER, BALL_BIGGER, BALL_SLOWER, BALL_FASTER, BASE_SMALLER, BASE_LARGER
	}

	private powerupType puType;
	private powerupModifier puModifier;

	public void setPowerupType(powerupType Type) {
		puType = Type;
	}

	public powerupType getPowerupType() {
		return this.puType;
	}

	public void setpowerupModifier(powerupModifier Type) {
		puModifier = Type;
	}

	public powerupModifier getPowerupModifier() {
		return this.puModifier;
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

}
