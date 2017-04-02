/*
* Base Class (Model)
*	Includes: Getter and Setter of Base properties
*
*	This class extends ObjectInfo class for the purpose
*	of code reuse.
*/

package com.ttcj.components;

import com.ttcj.testing.IWarlord;

public class Base extends ObjectInfo implements IWarlord {

	private boolean isDead; // true for dead, false for alive
	private boolean isWinner; // true for winner, false for loser/ undetermined

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

	@Override
	public void setXPos(int x) {
		this.SetxPosition(x);
	}

	@Override
	public void setYPos(int y) {
		this.SetyPosition(y);
	}

	@Override
	public boolean hasWon() {
		//Not yet implemented
		return false;
	}
}
