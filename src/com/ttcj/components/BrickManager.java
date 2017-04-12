package com.ttcj.components;
import java.util.ArrayList;

public class BrickManager {
	private ArrayList<Brick> brickList = new ArrayList<Brick>();
	private int numberOfBrick = 0;
	

	public void addBrick(Brick brick){
		this.brickList.add(brick);
		numberOfBrick++;
	}
	
	public void removeBrick(){
		numberOfBrick--;
	}
	
	public ArrayList<Brick> accessBrickArray(){
		return this.brickList;
	}
	
	public int getNumberOfBrick(){
		return numberOfBrick;
	}
}
