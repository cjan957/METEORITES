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
	private batPosition batLocation;
	
	private int AISpeed = 5;
	
	public enum batPosition {
		TopLEFT, TopRIGHT, BottomLEFT, BottomRIGHT
	}

	public Bat(String img, int xPos, int yPos, boolean isAI, batPosition bat) {
		this.setImage(img);
		this.SetxPosition(xPos);
		this.SetyPosition(yPos);
		this.setAIBat(isAI);
		this.setBatPosition(bat);
	}

	public void setBatPosition(batPosition bat) {
		this.batLocation = bat;
	}

	public batPosition getBatPosition() {
		return batLocation;
	}

	public void makeAIMoveY(double targetY) {
		if (batLocation == batPosition.TopRIGHT) {
			if (this.GetxPosition() > 741) {
				this.SetxPosition(this.GetxPosition() - AISpeed);
			} else if (this.GetxPosition() >= 737) {
				this.SetxPosition(736);
			} else {
				//if the bat is within +-5 pixels tolerance, don't do anything
				if ((this.GetyPosition() >= targetY - 5) && (this.GetyPosition() <= targetY + 5)) {

				} else if (this.GetyPosition() < targetY) {
					if (this.GetyPosition() < 250) {
						this.SetyPosition(this.GetyPosition() + AISpeed);
					}
				} else if (this.GetyPosition() > targetY) {
					if (this.GetyPosition() > 5) {
						this.SetyPosition(this.GetyPosition() - AISpeed);
					}
				}
			}
		}
		else if(batLocation == batPosition.BottomLEFT){
			if (this.GetxPosition() < 251) {
				this.SetxPosition(this.GetxPosition() + AISpeed);
			} else if (this.GetxPosition() <= 255) {
				this.SetxPosition(256);
			} else {
				//if the bat is within +-5 pixels tolerance, don't do anything
				if ((this.GetyPosition() >= targetY - 5) && (this.GetyPosition() <= targetY + 5)) {

				} else if (this.GetyPosition() < targetY) {
					if (this.GetyPosition() < 768-32-5) {
						this.SetyPosition(this.GetyPosition() + AISpeed);
					}
				} else if (this.GetyPosition() > targetY) {
					if (this.GetyPosition() > 480+5) {
						this.SetyPosition(this.GetyPosition() - AISpeed);
					}
				}
			}
		}
		else{ //TOPLEFT
			//If the bat is not at the corner where it will curve back to vertical, move the bat to the corner. 
			if (this.GetxPosition() < 251){
				this.SetxPosition(this.GetxPosition() + AISpeed); 
			}
			//if xPos is between 252 and 255, set position to 256 straight away
			else if(this.GetxPosition() <= 255){ 
				this.SetxPosition(256);
			}
			else{
				if ((this.GetyPosition() >= targetY - 5) && (this.GetyPosition() <= targetY + 5)) {

				} 
				//if the bat is above the ball's path, move the bat down!
				else if (this.GetyPosition() < targetY) {
					if (this.GetyPosition() < 250) {
						this.SetyPosition(this.GetyPosition() + AISpeed);
					}
				} else if (this.GetyPosition() > targetY) {
					if (this.GetyPosition() > 5) {
						this.SetyPosition(this.GetyPosition() - AISpeed);
					}
				}
				
			}
		}

	}

	public void makeAIMoveX(double targetX) {
		if(batLocation == batPosition.TopRIGHT){
			if (this.GetyPosition() <= 251) {
				this.SetyPosition(this.GetyPosition() + AISpeed);
			} 
			else if (this.GetyPosition() <= 255) {
				this.SetyPosition(256);
			}
			// Already at 256 and is ready to move
			else {
				if ((this.GetxPosition() >= targetX - 5) && (this.GetxPosition() <= targetX + 5)) {
	
				} else if (this.GetxPosition() < targetX) {
					if (this.GetxPosition() < 992) {
						this.SetxPosition(this.GetxPosition() + AISpeed);
					}
				} else if (this.GetxPosition() > targetX) {
					if (this.GetxPosition() > 741) {
						this.SetxPosition(this.GetxPosition() - AISpeed);
					}
				}
	
			}
		}
		else if(batLocation == batPosition.BottomLEFT){
			//If bat is not at the corner, move it to the corner
			if (this.GetyPosition() >= 485) {
				this.SetyPosition(this.GetyPosition() - AISpeed);
			} 
			else if (this.GetyPosition() >= 481) {
				this.SetyPosition(480);
			}
			else {
				if ((this.GetxPosition() >= targetX - 5) && (this.GetxPosition() <= targetX + 5)) {
	
				} else if (this.GetxPosition() < targetX) {
					if (this.GetxPosition() < (256-5)) {
						this.SetxPosition(this.GetxPosition() + AISpeed);
					}
				} else if (this.GetxPosition() > targetX) {
					if (this.GetxPosition() > 5) {
						this.SetxPosition(this.GetxPosition() - AISpeed);
					}
				}
	
			}
			
		}
		else{ //TOP LEFT
			if (this.GetyPosition() <= 251) {
				this.SetyPosition(this.GetyPosition() + AISpeed);
			} 
			else if (this.GetyPosition() <= 255) {
				this.SetyPosition(256);
			}
			// Already at 256 and is ready to move
			else {
				if ((this.GetxPosition() >= targetX - 5) && (this.GetxPosition() <= targetX + 5)) {
	
				} else if (this.GetxPosition() < targetX) {
					if (this.GetxPosition() < (256-5)) {
						this.SetxPosition(this.GetxPosition() + AISpeed);
					}
				} else if (this.GetxPosition() > targetX) {
					if (this.GetxPosition() > 5) {
						this.SetxPosition(this.GetxPosition() - AISpeed);
					}
				}
	
			}
		}
	}

	// Specify whether a bat is controlled by AI
	public void setAIBat(boolean AIBat) {
		this.isAIBat = AIBat;
	}

	public boolean isAIBat() {
		return this.isAIBat;
	}

	// Size of the bat can change in-game
	// when the player collects a powerup
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

	public Rectangle2D getBoundary() {
		return new Rectangle2D(xPosition, yPosition, width + 8, height);
	}

}
