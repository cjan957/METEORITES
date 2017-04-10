package com.ttcj.view;


import application.GamePlay;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

public class ViewManager {

	//private Scene mainMenu;
	private Scene mainMenuScene;
	
	public void MainMenu(Stage gameStage){
		
		Button button = new Button("Launch Game");
		Button button2 = new Button("Exit");
			
		//launch
		button.setOnAction(e -> new GamePlay(0, gameStage));
		
		button2.setOnAction(e -> System.exit(0));
	
		VBox listOfOptions = new VBox(10);
		listOfOptions.getChildren().addAll(button, button2);
	
		mainMenuScene = new Scene(listOfOptions, 1024, 768);
		
		mainMenuScene.setOnKeyPressed(event -> {	
			String code = event.getCode().toString();
			if(code == "ENTER"){
				//launch
				new GamePlay(0, gameStage);
			}
		});
	
		gameStage.setScene(mainMenuScene);
		
		// Apply conditions to the stage
		gameStage.setResizable(false);
		gameStage.sizeToScene();

		// Show the stage on the screen
		gameStage.show();
	}

}
