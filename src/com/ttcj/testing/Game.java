package com.ttcj.testing;

import com.ttcj.testing.Ball;
import com.ttcj.testing.Base;
import com.ttcj.testing.Bat;

public class Game implements IGame{
	
	private Ball ball;
	private Bat bat;
	private Brick player1Brick;
	private Base player1Base;
	private Base player2Base;
	private int timeRemaining;
	
	public void gameInit(Ball ball, Bat bat, Brick p1brick, Base p1base, Base p2base){
		this.ball = ball;
		this.bat = bat;
		this.player1Brick = p1brick;
		this.player1Base = p1base;
		this.player2Base = p2base;
		this.timeRemaining = 1;
	}
	
	@Override
	public void tick() {		
		WallCollisionCheck();
		PaddleCollisionCheck();
		ball.moveThatBall();
		warlordCollisionCheck();
		BoundaryCollisionCheck();
		checkWinCondition();
	}
	
	
	public void PaddleCollisionCheck(){
		int tempX = -1;
		int tempY = -1;
		
		if(bat.getXPos() != 0 && bat.getYPos() != 0){ 
			if(ball.getXPos() + ball.getXVelocity() > (bat.getXPos())){
				if(ball.getYPos() + ball.getYVelocity() > (bat.getYPos())){
					tempX = ball.getXPos() + ball.getXVelocity();
					tempY = ball.getYPos() + ball.getYVelocity();
					if(tempX > tempY){ //tempX bigger means collision is caused by the Y direction
						ball.setYVelocity(-ball.getYVelocity());
					}
					else{ //collision caused by the X direction
						ball.setXVelocity(-ball.getXVelocity());
					}
				}
			}	
		}		
	}
	
	public void warlordCollisionCheck(){
		if(ball.IsTravelRight() == true && ball.IsTravelDown() == true){ 
			if(ball.getXPos() + ball.getXVelocity() > (player1Base.getXPos())){
				if(ball.getYPos() + ball.getYVelocity() > (player1Base.getYPos())){
					player1Base.setIsDead(true);		
				}
			}
		}
	}
	
	public void WallCollisionCheck(){
		int tempX = -1;
		int tempY = -1;
		
		if(player1Brick.getXPos() != 0 && player1Brick.getYPos() != 0){ //no wall should be placed at position 0,0 (This is added to prevent conflict when other tests are run
			if(ball.getXPos() + ball.getXVelocity() > (player1Brick.getXPos())){
				if(ball.getYPos() + ball.getYVelocity() > (player1Brick.getYPos())){
					tempX = ball.getXPos() + ball.getXVelocity();
					tempY = ball.getYPos() + ball.getYVelocity();
					if((tempX != -1) && (tempY!= -1)){
						if(tempX > tempY){ //tempX bigger means collision is caused by the Y direction
							ball.setYVelocity(-ball.getYVelocity());
							player1Brick.setDestoryed();
						}
						else{ //collision caused by the X direction
							ball.setXVelocity(-ball.getXVelocity());
							player1Brick.setDestoryed();
						}
					}
				}
			}	
		}		
	}
	
	public void BoundaryCollisionCheck(){
		//check for boundary collision
		if(ball.getXPos() < (0)){		
			ball.setXVelocity(-ball.getXVelocity());
			ball.setXPos(0);
		}
		else if(ball.getXPos() > (1024-64)){		
			ball.setXVelocity(-ball.getXVelocity());
			ball.setXPos(1024-64);			
		}
		if(ball.getYPos() < (0)){
			ball.setYVelocity(-ball.getYVelocity());
			ball.setYPos(0);
		}
		else if(ball.getYPos() > (768-64)){
			ball.setYVelocity(-ball.getYVelocity());
			ball.setYPos(768-64);
		}
	}

	
	  /***
     * Determine if the game has finished. Results need only be valid before the start and after the end of a game tick.
     *
     * @return true if no more than one warlord remains alive, or if the time remaining is less than or equal to zero. Otherwise, return false.
     */
	@Override
	public boolean isFinished() {
				
		if(player1Base.isDead() || player2Base.isDead()){
			return true;
		}
		else if(this.timeRemaining <= 0){
			if(player1Brick.isDestroyed() == false){
				player1Base.setIsWinner(true);
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
		
	}
	
	public void checkWinCondition(){
		if(player1Base.isDead()){
			player2Base.setIsWinner(true);
		}
		else if(player2Base.isDead()){
			player1Base.setIsWinner(true);
		}
	}

	@Override
	public void setTimeRemaining(int seconds) {
		this.timeRemaining = seconds;
	}

}
