/*
 * ObjectInfo is a superclass of game model/components (Ball,Wall etc). It 
 * has all common elements a game component needs such as X and Y 
 * positions, object's speed and size.
 * 	Includes: Getter and Setter of objects
 * 			: Rendering method
 */

package com.ttcj.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ObjectInfo {

	protected Image image;

	protected int xPosition;
	protected int yPosition;

	protected int size;
	protected double width;
	protected double height;

	ObjectInfo() {

	}

	public void setImage(String filename) {
		Image image = new Image(filename);
		this.image = image;
		width = image.getWidth();
		height = image.getHeight();
	}

	public int GetxPosition() {
		return this.xPosition;
	}

	public void SetxPosition(int xPos) {
		this.xPosition = xPos;
	}

	public int GetyPosition() {
		return this.yPosition;
	}

	public void SetyPosition(int yPos) {
		this.yPosition = yPos;
	}

	public int GetSize() {
		return this.size;
	}

	public void SetSize(int size) {
		this.size = size;
	}

	public void SetWidth(double width) {
		this.width = width;
	}

	public double GetWidth() {
		return this.width;
	}

	public void SetHeight(double height) {
		this.height = height;
	}

	public double GetHeight() {
		return this.height;
	}

	//Render the object on a canvas as specify by graphicContent 
	//argument
	public void render(GraphicsContext gc) {
		gc.drawImage(image, xPosition, yPosition);
	}

}
