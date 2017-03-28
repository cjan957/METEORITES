package application;
	
import java.util.ArrayList;
import java.lang.Math;

import com.ttcj.components.Ball;
import com.ttcj.components.Bat;

import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class Main extends Application {
	
	private static final String GAMENAME = "Warlords";
	private static final int WINDOW_W = 1024;
	private static final int WINDOW_H = 768;
	private static double angle1  = 0.0;
	private static double angle2  = 0.0;
	
	
	@Override
	public void start(Stage gameStage) {
		gameStage.setTitle(GAMENAME);
		
		Group root = new Group(); //JavaFX scene root node 
		Scene theScene = new Scene(root); //create scene object with root parent
		gameStage.setScene(theScene); //set Stage class's scene
		
		//Create canvas object with a specific size
		Canvas canvas = new Canvas(WINDOW_W,WINDOW_H);
		root.getChildren().add(canvas); //add canvas to children of root (theScene)
		//ArrayList to store keystrokes
		ArrayList<String> input = new ArrayList<String>();
		
		//When a key press event occurs, store the key the player pressed
		//into input array
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
		
		//TODO: Verify this comment
		//Create gc object so that we can draw on the created canvas using
		//commands by GraphicContext?????????
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		//Create ball object
		Ball ball = new Ball();
		ball.setImage("rsz_1basketball.png");
		ball.SetxPosition(WINDOW_W/4);
		ball.SetyPosition(WINDOW_H-64);
				
		Ball ball2 = new Ball();
		ball2.setImage("angery.jpg");
		ball2.SetxPosition((WINDOW_W)/3);
		ball2.SetyPosition((WINDOW_H)/3);
		
	
		//TODO: write a comment
		Timeline gameLoop = new Timeline();
		gameLoop.setCycleCount(Timeline.INDEFINITE);
		
		//PathTransition
		
		
		//final long timeStart = System.currentTimeMillis();
		
		KeyFrame gameFrame = new KeyFrame(Duration.seconds(0.016), event ->{ //60fps
			if(input.contains("LEFT")){
				//check LHS boundary condition so that the paddle won't be able to
				//go beyond the limit
				if(ball.GetxPosition() >= 0){
					angle1 += 0.05; 
					ball.SetxPosition(Math.cos(angle1)*(WINDOW_W/4));
					ball.SetyPosition(Math.sin(angle1)*(WINDOW_H/4));
				}
			}
			else if(input.contains("RIGHT")){
				//check RHS boundary condition
				if(ball.GetyPosition() >= 0){
					angle1 -= 0.05; 
					ball.SetxPosition(Math.cos(angle1)*(WINDOW_W/4));
					ball.SetyPosition(Math.sin(angle1)*(WINDOW_W/4));
				}
			}		
			
			
			if(input.contains("A")){
				if(ball2.GetxPosition() >= 0){
					ball2.SetxPosition(ball2.GetxPosition() - 5);
				}
			}
			else if(input.contains("D")){
				if(ball2.GetxPosition() + 32 <= WINDOW_W){
					ball2.SetxPosition(ball2.GetxPosition() + 5);
				}
			}
			
			
			//TODO: not sure how to describe this 
			gc.clearRect(0, 0, 1024, 768);
			
			//draw the ball on canvas!
			ball.render(gc);	
			ball2.render(gc);
		});
		
		gameLoop.getKeyFrames().add(gameFrame);
		gameLoop.play();
		
		gameStage.setResizable(false);
		gameStage.sizeToScene();
		gameStage.show();		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
