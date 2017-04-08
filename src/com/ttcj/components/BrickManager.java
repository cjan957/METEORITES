package com.ttcj.components;
import java.util.ArrayList;
import java.util.Iterator;

public class BrickManager {
	private ArrayList<Brick> brickList = new ArrayList<Brick>();
	private int numberOfBrick = 0;
	

	
	public void addBrick(Brick brick){
		this.brickList.add(brick);
		numberOfBrick =+ 1;
	}
	
	public void removeBrick(int position){
		
	}
	
	public ArrayList<Brick> accessBrickArray(){
		return this.brickList;
	}
	
	//add getter for number of bricks
}
