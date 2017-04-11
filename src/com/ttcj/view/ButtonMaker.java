package com.ttcj.view;

import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ButtonMaker extends StackPane{
	private Text text;
	
	public ButtonMaker(String buttonName){
		text = new Text(buttonName);
		text.setFont(text.getFont().font(20));
		text.setFill(Color.WHITE);
		
		Rectangle bg = new Rectangle(250,50);
		DropShadow dropShadow = new DropShadow(50, Color.WHITE);
		dropShadow.setInput(new Glow());
		
		bg.setEffect(new GaussianBlur(4));
		bg.setFill(Color.GREY);
		bg.setOpacity(0.7);
		
		setAlignment(Pos.CENTER);
		getChildren().addAll(bg,text);
		
		setOnMouseEntered(event ->{
			//bg.setTranslateX(20);
			//text.setTranslateX(20);
			setEffect(dropShadow);
		});
		
		setOnMouseExited(event ->{
			//bg.setTranslateX(0);
			//text.setTranslateX(0);
			setEffect(null);
		});
		
	}
}
