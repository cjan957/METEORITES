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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class View {
	
	private static final int WINDOW_W = 1024;
	private static final int WINDOW_H = 768;
	private static final String GAMENAME = "Meteorites";

	private GraphicsContext gc;	
	private ArrayList<String> input = new ArrayList<String>();

	public void viewSetup(Stage gameStage){
		
		gameStage.setTitle(GAMENAME);		
		Group root = new Group(); //JavaFX scene root node 
		Scene theScene = new Scene(root); //create scene object with root parent
		gameStage.setScene(theScene); //set Stage class's scene
//		theScene.setFill(Color.BLACK);
		
		Image background = new Image("stars_space.jpg");
		theScene.setFill(new ImagePattern(background));
		
		//Circle(double centerX, double centerY, double radius, Paint fill)
		//Rectangle(double x, double y, double width, double height)
		//Arc(double centerX, double centerY, double radiusX, double radiusY, double startAngle, double length)
		
		Arc planetTL = new Arc(0,0,100,100,270,90);   //planets 
		planetTL.setFill(Color.BLUE);
		planetTL.setType(ArcType.ROUND);
		
		Arc planetTR = new Arc(1024,0,100,100,180,90);
		planetTR.setFill(Color.GREEN);
		planetTR.setType(ArcType.ROUND);
		
		
		Arc planetBL = new Arc(0,768,100,100,0,90);
		planetBL.setFill(Color.YELLOW);
		planetBL.setType(ArcType.ROUND);
		
		Arc planetBR = new Arc(1024,768,100,100,90,90);
		planetBR.setFill(Color.RED);
		planetBR.setType(ArcType.ROUND);
		
		Rectangle testBrick = new Rectangle(500,500,30,10);
		testBrick.setFill(Color.WHITE);
		
		root.getChildren().addAll(planetTL, planetTR , planetBL, planetBR);  //add planets to canvas
		
		Canvas canvas = new Canvas(WINDOW_W, WINDOW_H);
		root.getChildren().add(canvas); //add canvas to children of root (theScene)
		
		theScene.setOnKeyPressed(event->{
			String code = event.getCode().toString();
			if(!input.contains(code)){
				input.add(code);
			}	
		});
		
		//Remove keystroke from array when key is released
		theScene.setOnKeyReleased(event->{
			String code = event.getCode().toString();
			input.remove(code);
		});
		
		gc = canvas.getGraphicsContext2D();
		
		gameStage.setResizable(false);
		gameStage.sizeToScene();
		gameStage.show();
	}
	
	public GraphicsContext canvasGC(){
		return gc;
	}
	
	public ArrayList<String> accessUserInput(){
		return input;
	}
	
}
