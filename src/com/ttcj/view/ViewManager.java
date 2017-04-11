package com.ttcj.view;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import application.GamePlay;
import application.GamePlaySingle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

public class ViewManager {

	//private Scene mainMenu;
	private Scene mainMenuScene;
	
	public void MainMenu(Stage gameStage){
		
		Pane root = new Pane();
		root.setPrefSize(1024,768);
		
		InputStream stream = null;
		try {
			stream = Files.newInputStream(Paths.get("Images/meteorites_bg.jpg"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("xxx");
		}
		Image image = new Image(stream);
		try {
			stream.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("Error closing i/o stream");
			e1.printStackTrace();
		}
		
		VBox menuList = new VBox(25);
		
		menuList.setTranslateX(110);
		menuList.setTranslateY(300);
			
		ButtonMaker singlePlayer = new ButtonMaker("Single Player");
		singlePlayer.setOnMouseClicked(event->{
			new GamePlaySingle(0, gameStage);
		});
		
		ButtonMaker twoPlayer = new ButtonMaker("Two Players");
		twoPlayer.setOnMouseClicked(event->{
			new GamePlay(0, gameStage);
		});
		
		ButtonMaker exit = new ButtonMaker("Exit");
		exit.setOnMouseClicked(event->{
			System.exit(0);
		});
		
		menuList.getChildren().addAll(singlePlayer,twoPlayer,exit);
		
		
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(1024);
		imageView.setFitHeight(768);
		
		root.getChildren().addAll(imageView, menuList);
		
		Scene scene = new Scene(root);
		
		gameStage.setScene(scene);
		gameStage.setResizable(false);
		gameStage.sizeToScene();

		// Show the stage on the screen
		gameStage.show();

		

		
	
		
//		Button button = new Button("Launch Game");
//		Button button2 = new Button("Exit");
//			
//		//launch
//		button.setOnAction(e -> new GamePlay(0, gameStage));
//		button2.setOnAction(e -> System.exit(0));
//	
//		VBox listOfOptions = new VBox(10);
//		listOfOptions.getChildren().addAll(button, button2);
//	
//		mainMenuScene = new Scene(listOfOptions, 1024, 768);
//		
//		mainMenuScene.setOnKeyPressed(event -> {	
//			String code = event.getCode().toString();
//			if(code == "ENTER"){
//				//launch
//				new GamePlay(0, gameStage);
//			}
//		});
//	
//		gameStage.setScene(mainMenuScene);
//		
//		// Apply conditions to the stage
//		gameStage.setResizable(false);
//		gameStage.sizeToScene();
//
//		// Show the stage on the screen
//		gameStage.show();
	}

}
