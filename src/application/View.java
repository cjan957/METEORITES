/*
 * View Class 
*	Handles all user interactions and graphics
*/

package application;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

public class View {

	private static final int WINDOW_W = 1024;
	private static final int WINDOW_H = 768;
	private static final String GAMENAME = "Meteorites";

	private GraphicsContext gc;
	private ArrayList<String> input = new ArrayList<String>();

	public void viewSetup(Stage gameStage) {

		//Add Title to JavaFX top level container (Stage)
		gameStage.setTitle(GAMENAME);
		
		//Create a new JavaFX scene root node (group)
		Group root = new Group(); 
		
		//Create scene object with root parent		
		Scene theScene = new Scene(root); 
		
		//Set Stage scene
		gameStage.setScene(theScene); 

		//Apply Background image to the scene
		Image background = new Image("stars_space.jpg");
		theScene.setFill(new ImagePattern(background));

		// Circle(double centerX, double centerY, double radius, Paint fill)
		// Rectangle(double x, double y, double width, double height)
		// Arc(double centerX, double centerY, double radiusX, double radiusY,
		// double startAngle, double length)

		
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

		// Rectangle testBrick = new Rectangle(500, 500, 30, 10);
		// testBrick.setFill(Color.WHITE);

		// Circle circle = new Circle(50);
		// Image asteroid = new Image("b30000.png");
		// circle.setFill(new ImagePattern(asteroid));
		// circle.setFill(Color.WHITE);
		// circle.setCenterX(400);
		// circle.setCenterY(500);

		//Add planets to the scene
		root.getChildren().addAll(planetTL, planetTR, planetBL, planetBR); 
		
		//Create a new canvas and add it as a child of root  
		Canvas canvas = new Canvas(WINDOW_W, WINDOW_H);
		root.getChildren().add(canvas); 

		//Handle user input, do something when an event occurs (a key is pressed) 
		theScene.setOnKeyPressed(event -> {
			
			//Convert the keycode to string so we can handle it easily
			String code = event.getCode().toString();
			
			//Check if the key is already in the array, if not add that key to the array
			if (!input.contains(code)) {
				input.add(code);
			}
		});

		// Remove keystroke from array when key is released
		theScene.setOnKeyReleased(event -> {
			String code = event.getCode().toString();
			input.remove(code);
		});

		//Issue a draw call on the canvas
		gc = canvas.getGraphicsContext2D();

		//Apply conditions to the stage
		gameStage.setResizable(false);
		gameStage.sizeToScene();
		
		//Show the stage on the screen
		gameStage.show();
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
