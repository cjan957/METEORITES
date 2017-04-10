package application;

import com.ttcj.components.Brick;
import com.ttcj.components.BrickManager;

public class BrickBuilder {
	private static final int WINDOW_W = 1024;
	private static final int WINDOW_H = 768;
	
	private BrickManager topLHSBrick;
	private BrickManager bottomLHSBrick;
	private BrickManager topRHSBrick;
	private BrickManager bottomRHSBrick;
	
	BrickBuilder(){
		topLHSBrickInit();
		bottomLHSBrickInit();
		topRHSBrickInit();
		bottomRHSBrickInit();
	}
	
	public BrickManager getTopLHSBrick(){
		return topLHSBrick;
	}
	
	public BrickManager getBottomLHSBrick(){
		return bottomLHSBrick;
	}
	
	public BrickManager getTopRHSBrick(){
		return topRHSBrick;
	}
	
	public BrickManager getBottomRHSBrick(){
		return bottomRHSBrick;
	}
		
	public void bottomRHSBrickInit() {
		bottomRHSBrick = new BrickManager();

		for (int i = 991; i > 826; i -= 33) { // Horizontal bricks Inner
			Brick brick = new Brick(i, WINDOW_H - 140 - 32, 32, 32, 0);
			bottomRHSBrick.addBrick(brick);
		}

		for (int j = 728; j > 568; j -= (33)) { // Vertical Bricks Inner
			Brick brick = new Brick(WINDOW_W - 180, j, 32, 32, 1);
			bottomRHSBrick.addBrick(brick);
		}

		for (int i = 991; i > 826; i -= 33) { // Horizontal bricks Outer
			Brick brick = new Brick(i, WINDOW_H - 140 - 64, 32, 32, 0);
			bottomRHSBrick.addBrick(brick);
		}

		for (int j = 728; j > 568; j -= 33) { // Vertical Bricks
			Brick brick = new Brick(WINDOW_W - 180 - 32, j, 32, 32, 1);
			bottomRHSBrick.addBrick(brick);
		}

	}

	public void topRHSBrickInit() {
		topRHSBrick = new BrickManager();

		// INNER
		for (int i = 991; i > 826; i -= 33) { // Horizontal bricks
			Brick brick = new Brick(i, 180, 32, 32, 0);
			topRHSBrick.addBrick(brick);
		}

		for (int j = 0; j < 140; j += 33) { // Vertical Bricks
			Brick brick = new Brick(WINDOW_W - 180, j, 32, 32, 1);
			topRHSBrick.addBrick(brick);
		}

		// OUTER WALL
		for (int i = 991; i > 826; i -= 33) { // Horizontal bricks
			Brick brick = new Brick(i, 148, 32, 32, 0);
			topRHSBrick.addBrick(brick);
		}

		for (int j = 0; j < 140; j += 33) { // Vertical Bricks
			Brick brick = new Brick(WINDOW_W - 180 - 32, j, 32, 32, 1);
			topRHSBrick.addBrick(brick);
		}
	}

	public void bottomLHSBrickInit() {
		bottomLHSBrick = new BrickManager();

		for (int i = 0; i < 140; i += 33) { // Horizontal bricks
			Brick brick = new Brick(i, WINDOW_H - 140 - 32, 32, 32, 0);
			bottomLHSBrick.addBrick(brick);
		}

		for (int j = 728; j > 568; j -= (33)) { // Vertical Bricks
			Brick brick = new Brick(180, j, 32, 32, 1);
			bottomLHSBrick.addBrick(brick);
		}

		// Outer Wall
		for (int i = 0; i < 140; i += 33) { // Horizontal bricks
			Brick brick = new Brick(i, WINDOW_H - 140 - 32 - 32, 32, 32, 0);
			bottomLHSBrick.addBrick(brick);
		}

		for (int j = 728; j > 568; j -= (33)) { // Vertical Bricks
			Brick brick = new Brick(148, j, 32, 32, 1);
			brick.setImage("iceblock_32.png");
			bottomLHSBrick.addBrick(brick);
		}
	}

	public void topLHSBrickInit() {
		topLHSBrick = new BrickManager();

		// TLHS
		for (int i = 0; i < 140; i += 33) { // Horizontal bricks 5 Bricks
											// 0,33,66,99,132
			Brick brick = new Brick(i, 180, 32, 32, 0);
			topLHSBrick.addBrick(brick);
		}

		for (int j = 0; j < 140; j += 33) { // Vertical Bricks
			Brick brick = new Brick(180, j, 32, 32, 1);
			topLHSBrick.addBrick(brick);
		}

		// INNER WALL
		for (int i = 0; i < 140; i += 33) { // Horizontal bricks
			Brick brick = new Brick(i, 148, 32, 32, 0);
			topLHSBrick.addBrick(brick);
		}

		for (int j = 0; j < 140; j += 33) { // Vertical Bricks
			Brick brick = new Brick(148, j, 32, 32, 1);
			topLHSBrick.addBrick(brick);
		}
	}



}
