/*
 * View Class 
*	Handles all user interactions and graphics
*/

package application;

import java.util.ArrayList;

import com.ttcj.view.ViewManager;

import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class View {

	//QUICK DEBUGGING OPTIONS

	private Scene gamePlayScene;
	private Group root;
	
	private boolean isPaused; //Game is paused or running
	private boolean confimationToExit;
	private boolean countDown3IsDone;
	private boolean gameHasFinished;
	

	private GraphicsContext gc;
	private ArrayList<String> input = new ArrayList<String>();
	
	private boolean enableBackground = true;
	
	private static final int WINDOW_W = 1024;
	private static final int WINDOW_H = 768;
	//private Stage window;

	public void setupGameView(Stage window){
		
		root = new Group();
		gamePlayScene = new Scene(root);
		
		//TODO: Remove this if statement when deliver
		if(enableBackground){
			Image background = new Image("galaxy_bg.jpg");
			gamePlayScene.setFill(new ImagePattern(background));
		}

		//Setup planet shapes to each corner - will represent the 'warlords'
		Arc planetTL = new Arc(0, 0, 100, 100, 270, 90); // planets
		planetTL.setFill(Color.BLUE);
		planetTL.setType(ArcType.ROUND);

		Arc planetTR = new Arc(1024, 0, 100, 100, 180, 90);
		planetTR.setFill(Color.GREEN);
		planetTR.setType(ArcType.ROUND);

		Arc planetBL = new Arc(0, 768, 100, 100, 0, 90);
		planetBL.setFill(Color.YELLOW);
		planetBL.setType(ArcType.ROUND);

		Arc planetBR = new Arc(1024, 768, 100, 100, 90, 90);
		planetBR.setFill(Color.RED);
		planetBR.setType(ArcType.ROUND);

		//Add planets to the scene
		root.getChildren().addAll(planetTL, planetTR, planetBL, planetBR); 
		
		//Create a new canvas and add it as a child of root  
		Canvas canvas = new Canvas(WINDOW_W, WINDOW_H);
		root.getChildren().add(canvas); 
		gc = canvas.getGraphicsContext2D();

		//Handle user input, do something when an event occurs (a key is pressed) 
		gamePlayScene.setOnKeyPressed(event -> {
			
			//Convert the keycode to string so we can handle it easily
			String code = event.getCode().toString();
			
			//Pause game if P is pressed
			if(code == "P"){
				//Don't allow any actions if the start countdown is not yet finished
				if(countDown3IsDone){ 
					//prevent conflict with ESC
					if(!confimationToExit){ 
						if(!isPaused){
							isPaused = true;
						} else {
							isPaused = false;
						}
					}
				}
			}
			
			//Exit game if ESC is pressed 			
			if(code == "ESCAPE"){
				if(gameHasFinished){
					ViewManager viewMgr = new ViewManager();
					viewMgr.MainMenu(window);
				}
				else if(countDown3IsDone){
					if(!confimationToExit){
						confimationToExit = true;
						isPaused = true;
					}
					else{
						isPaused = false;
						confimationToExit = false;
					}
				}
			}
			if(code == "Y"){
				if(confimationToExit){
					ViewManager viewMgr = new ViewManager();
					viewMgr.MainMenu(window);
				}
			}
			
			//Check if the key is already in the array, if not add that key to the array
			if (!input.contains(code)) {
				input.add(code);
			}
		});

		// Remove keystroke from array when key is released
		gamePlayScene.setOnKeyReleased(event -> {
			String code = event.getCode().toString();
			input.remove(code);
		});

		//Issue a draw call on the canvas
		
		window.setScene(gamePlayScene);
	}

	public boolean isPause(){
		return this.isPaused;
	}
	
	public void forcePause(){
		isPaused = true;
	}
	
	public boolean confirmationShown(){
		return confimationToExit;
	}
	
	public void setCountDown3IsDone(){
		this.countDown3IsDone = true;
	}
	
	public void setGameFinished(){
		this.gameHasFinished = true;
	}
	
	//Make GraphicsContext on the canvas created in this View class available
	//to Controller (Game) Class.
	public GraphicsContext canvasGC() {
		return gc;
	}

	//Make Array of user input available to controller class so user input
	//can be checked.
	public ArrayList<String> accessUserInput() {
		return input;
	}
	
	

}
