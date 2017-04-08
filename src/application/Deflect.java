package application;

import com.ttcj.components.Ball;

public class Deflect {
	
	private int tempBallDirection_RIGHT;
	private int tempBallDirection_UP;
	
	
	public void setTempDir(int right, int up){
		this.tempBallDirection_RIGHT = right;
		this.tempBallDirection_UP = up;
	}

	public void deflectTheBall(Ball ball, int brickArrangement) {

		if (!ball.getIfTravelRight() && ball.getIfTravelUp()) { // 0,1
			if (tempBallDirection_RIGHT == 0 && tempBallDirection_UP == 1) {
				// Check if the ball still travels in the same direction
				// This is checked to prevent no velocity change when the ball
				// hits two bricks at once
				// ie) When ball hits two walls, its velocity is reverse twice
				// -(-V) resulting in the
				// original velocity (V)
			} else {
				// Check the brick arrangement (deflect the ball differently
				// depending on how wall is arranged.
				if (brickArrangement == 0) {
					ball.setYVelocity(-ball.getYVelocity());
				} else {
					ball.setXVelocity(-ball.getXVelocity());
				}
				tempBallDirection_RIGHT = 0;
				tempBallDirection_UP = 1;
			}
		} else if (ball.getIfTravelRight() && ball.getIfTravelUp()) { // 1,1
			if (tempBallDirection_RIGHT == 1 && tempBallDirection_UP == 1) {

			} else {
				if (brickArrangement == 0) {
					ball.setYVelocity(-ball.getYVelocity());
				} else {
					ball.setXVelocity(-ball.getXVelocity());
				}
				tempBallDirection_RIGHT = 1;
				tempBallDirection_RIGHT = 1;
			}
		} else if (!ball.getIfTravelRight() && !ball.getIfTravelUp()) { // 0,0
			if (tempBallDirection_RIGHT == 0 && tempBallDirection_UP == 0) {

			} else {
				if (brickArrangement == 0) {
					ball.setYVelocity(-ball.getYVelocity());
				} else {
					ball.setXVelocity(-ball.getXVelocity());
				}
				tempBallDirection_RIGHT = 0;
				tempBallDirection_UP = 0;
			}
		} else if (ball.getIfTravelRight() && !ball.getIfTravelUp()) { // 1,0
			if (tempBallDirection_RIGHT == 1 && tempBallDirection_UP == 0) {

			} else {
				if (brickArrangement == 0) {
					ball.setYVelocity(-ball.getYVelocity());

				} else {
					ball.setXVelocity(-ball.getXVelocity());
				}
				tempBallDirection_RIGHT = 1;
				tempBallDirection_UP = 0;
			}
		} else {
			ball.setXVelocity(-ball.getXVelocity());
			ball.setYVelocity(-ball.getYVelocity());
		}
	}

}
