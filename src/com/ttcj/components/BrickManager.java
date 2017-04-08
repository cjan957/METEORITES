package com.ttcj.components;
import java.util.ArrayList;
import java.util.Iterator;

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
	
	//add getter for number of bricks
}
