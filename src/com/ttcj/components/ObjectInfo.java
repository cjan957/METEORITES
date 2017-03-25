package com.ttcj.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/*
 * ObjectInfo is a superclass of game components (Ball,Wall etc). It 
 * has all common elements a game component needs such as X and Y 
 * positions, object's speed and size.
 */

public class ObjectInfo {
	
	protected Image image;
	
	protected double xPosition;
	protected double yPosition;
	
	protected double size;
	protected boolean visible;
	protected double width;
	protected double height;
	
	protected double velocityX;
	protected double velocityY;	
	
	ObjectInfo(){
		
	}
	
	
	public void setImage(String filename){
		Image image = new Image(filename);
		this.image = image;
		width = image.getWidth();
		height = image.getHeight();
	}
	
	public double GetxPosition(){
		return this.xPosition;
	}
	
	public void SetxPosition(double xPos){
		this.xPosition = xPos;
	}
	
	public double GetyPosition(){
		return this.yPosition;
	}
	
	public void SetyPosition(double yPos){
		this.yPosition = yPos;
	}
	
	public double GetSize(){
		return this.size;
	}
	
	public void SetSize(double size){
		this.size = size;
	}
	
	public double GetVelocityX(){
		return this.velocityX;
	}
	
	public void SetVelocityX(double velocityX){
		this.velocityX = velocityX;
	}
	
	public double GetVelocityY(){
		return this.velocityY;
	}
	
	public void SetVelocityY(double velocityY){
		this.velocityY = velocityY;
	}
	
	public boolean GetVisibilityStatus(){
		return this.visible;
	}
	
	public void SetWidth(double width){
		this.width = width;
	}
	
	public double GetWidth(){
		return this.width;
	}
	
	public void SetHeight(double height){
		this.height = height;
	}
	
	public double GetHeight(){
		return this.height;
	}
	
	public void SetVisibilityStatus(boolean visibility){
		this.visible = visibility;
	}
	
	public void render(GraphicsContext gc){
		gc.drawImage(image, xPosition, yPosition);
	}
	
}
