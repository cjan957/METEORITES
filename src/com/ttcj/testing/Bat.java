/*
 * For more info on this class, please refer to the actual game bat model/class.
 */

package com.ttcj.testing;

import com.ttcj.testing.IPaddle;

public class Bat implements IPaddle {

	private int xPosition;
	private int yPosition;

	private boolean isAIBat;
	private int batWidth;
	private int batLength;

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

	public void setAIBat(boolean AIBat) {
		this.isAIBat = AIBat;
	}

	public boolean isAIBat() {
		return this.isAIBat;
	}

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

}
