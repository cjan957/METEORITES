/*
 * Game Class
 * 	This class has all methods that checks for collisions and winning conditions
 *  It also has all objects from other model classes that are required to run the game.
 */

package com.ttcj.testing;

import com.ttcj.testing.Ball;
import com.ttcj.testing.Base;
import com.ttcj.testing.Bat;

public class Game implements IGame {

	private Ball ball;
	private Bat bat;
	private Brick player1Brick;
	private Base player1Base;
	private Base player2Base;
	private int timeRemaining;

	//Set up the game by taking all objects created and passed in by WarlordTest class's
	//setUp method
	public void gameInit(Ball ball, Bat bat, Brick p1brick, Base p1base, Base p2base) {
		this.ball = ball;
		this.bat = bat;
		this.player1Brick = p1brick;
		this.player1Base = p1base;
		this.player2Base = p2base;

		// let the game run by at least 1 tick
		this.timeRemaining = 1;
	}

	@Override
	public void tick() {
		// Check for collisions/win conditions each tick
		wallCollisionCheck();
		paddleCollisionCheck();
		ball.moveThatBall();
		warlordCollisionCheck();
		boundaryCollisionCheck();
		checkWinCondition();
	}

	public void paddleCollisionCheck() {
		int tempX = -1;
		int tempY = -1;

		if (bat.getXPos() != 0 && bat.getYPos() != 0) {
			// Check the ball's next position in advance (both X and Y)
			// to see whether the ball will go through the paddle
			if (ball.getXPos() + ball.getXVelocity() > (bat.getXPos())) {
				if (ball.getYPos() + ball.getYVelocity() > (bat.getYPos())) {
					tempX = ball.getXPos() + ball.getXVelocity();
					tempY = ball.getYPos() + ball.getYVelocity();
					// If tempX is bigger it means that the collision is caused
					// by Y velocity (Y collision occurs first in space)
					if (tempX > tempY) {
						ball.setYVelocity(-ball.getYVelocity());
					} else { // collision caused by the X velocity
						ball.setXVelocity(-ball.getXVelocity());
					}
				}
			}
		}
	}

	public void warlordCollisionCheck() {
		// Assuming the ball is traveling toward the bottom right of the screen.
		if (ball.IsTravelRight() == true && ball.IsTravelDown() == true) {
			if (ball.getXPos() + ball.getXVelocity() > (player1Base.getXPos())) {
				if (ball.getYPos() + ball.getYVelocity() > (player1Base.getYPos())) {
					player1Base.setIsDead(true);
				}
			}
		}
	}

	public void wallCollisionCheck() {
		int tempX = -1;
		int tempY = -1;

		// no wall should be placed at position 0,0 (this is added to prevent
		// conflict when other tests cases are run where player1brick position 
		//is initialized to (0,0))		
		if (player1Brick.getXPos() != 0 && player1Brick.getYPos() != 0) {
			if (ball.getXPos() + ball.getXVelocity() > (player1Brick.getXPos())) {
				if (ball.getYPos() + ball.getYVelocity() > (player1Brick.getYPos())) {
					tempX = ball.getXPos() + ball.getXVelocity();
					tempY = ball.getYPos() + ball.getYVelocity();
					if ((tempX != -1) && (tempY != -1)) {
						// tempX bigger means collision is caused by the Y direction
						if (tempX > tempY) {
							ball.setYVelocity(-ball.getYVelocity());
							player1Brick.setDestoryed();
						}
						// collision caused by the X direction
						else {
							ball.setXVelocity(-ball.getXVelocity());
							player1Brick.setDestoryed();
						}
					}
				}
			}
		}
	}

	public void boundaryCollisionCheck() {
		// check for boundary collision
		
		//LHS of the screen
		if (ball.getXPos() < (0)) {
			ball.setXVelocity(-ball.getXVelocity());
			ball.setXPos(0);
		} 
		//RHS
		else if (ball.getXPos() > (1024 - 64)) {
			ball.setXVelocity(-ball.getXVelocity());
			ball.setXPos(1024 - 64);
		}
		//TOP
		if (ball.getYPos() < (0)) {
			ball.setYVelocity(-ball.getYVelocity());
			ball.setYPos(0);
		} 
		//BOTTOM
		else if (ball.getYPos() > (768 - 64)) {
			ball.setYVelocity(-ball.getYVelocity());
			ball.setYPos(768 - 64);
		}
	}


	@Override
	public boolean isFinished() {
		//Check whether the game has ended. By returning true if no more than one
		//warlord is alive, or time is <= 0
		if (player1Base.isDead() || player2Base.isDead()) {
			return true;
		} else if (this.timeRemaining <= 0) {
			if (player1Brick.isDestroyed() == false) {
				player1Base.setIsWinner(true);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	//Determine which player wins the game by checking 
	//which player is dead.
	public void checkWinCondition() {
		if (player1Base.isDead()) {
			player2Base.setIsWinner(true);
		} else if (player2Base.isDead()) {
			player1Base.setIsWinner(true);
		}
	}

	
	@Override
	public void setTimeRemaining(int seconds) {
		this.timeRemaining = seconds;
	}

}
