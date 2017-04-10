/*
 * Game Class
 * 	Game class is the controller of the game. It creates and uses objects
 *  from view and model classes.
 */

package application;

import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Math;
import java.nio.file.Files;

import com.ttcj.components.Ball;
import com.ttcj.components.Base;
import com.ttcj.components.Bat;
import com.ttcj.components.Brick;
import com.ttcj.components.BrickManager;
import com.ttcj.components.Sound;
import com.ttcj.view.ViewManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Game extends Application {

	private static final String GAMENAME = "Meteorites";
	private static final int WINDOW_W = 1024;
	private static final int WINDOW_H = 768;
	
	private Scene mainMenuScene;
	//private Stage gameWindow;



	public static void main(String[] args) {
		// Launch javafx application / run start method
		launch(args);
	}

	@Override
	public void start(Stage gameStage) {
		
		ViewManager viewMgr = new ViewManager();
		viewMgr.MainMenu(gameStage);
		
		
		//gameWindow = gameStage;

		// Create view object. gameStage will be set up with Canvas applied.
		// Please jump to View class for more info.
		//view = new View();

		// Add Title to JavaFX top level container (Stage)
//		gameStage.setTitle(GAMENAME);
//
//		Button button = new Button("Launch Game");
//		Button button2 = new Button("Exit");
//		button.setOnAction(e -> new GamePlay(0, gameStage));
//		button2.setOnAction(e -> System.exit(0));
//
//		VBox listOfOptions = new VBox(10);
//		listOfOptions.getChildren().addAll(button, button2);
//
//		mainMenuScene = new Scene(listOfOptions, 1024, 768);
//		
//		
//		mainMenuScene.setOnKeyPressed(event -> {	
//			String code = event.getCode().toString();
//			if(code == "ENTER"){
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