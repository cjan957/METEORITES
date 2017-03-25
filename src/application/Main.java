package application;
	
import java.util.ArrayList;

import com.ttcj.components.Ball;

import javafx.animation.KeyFrame;
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


public class Main extends Application {
	
	private static final String GAMENAME = "Warlords";
	private static final int WINDOW_W = 1024;
	private static final int WINDOW_H = 768;

	@Override
	public void start(Stage gameStage) {
		gameStage.setTitle(GAMENAME);
		
		Group root = new Group(); //JavaFX scene root node 
		Scene theScene = new Scene(root); //create scene object with root parent
		gameStage.setScene(theScene); //set Stage class's scene
		
		//Create canvas object with a specific size
		Canvas canvas = new Canvas(WINDOW_W,WINDOW_H);
		root.getChildren().add(canvas); //add canvas to children of root (theScene)
		
		//ArrayList to store keystroke
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
		ball.SetxPosition((WINDOW_W)/2);
		ball.SetyPosition((WINDOW_H)/2);
	
		//TODO: write a comment
		Timeline gameLoop = new Timeline();
		gameLoop.setCycleCount(Timeline.INDEFINITE);
		
		//final long timeStart = System.currentTimeMillis();
		
		KeyFrame gameFrame = new KeyFrame(Duration.seconds(0.016), event ->{
			if(input.contains("LEFT")){
				//check LHS boundary condition so that the paddle won't be able to
				//go beyond the limit
				if(ball.GetxPosition() >= 0){
					ball.SetxPosition(ball.GetxPosition() - 5);
				}
			}
			else if(input.contains("RIGHT")){
				//check RHS boundary condition
				if(ball.GetxPosition() + 64 <= WINDOW_W){
					ball.SetxPosition(ball.GetxPosition() + 5);
				}
			}
					
			//TODO: not sure how to describe this 
			gc.clearRect(0, 0, 1024, 768);
			
			//draw the ball on canvas!
			ball.render(gc);					
		});
		
		gameLoop.getKeyFrames().add(gameFrame);
		gameLoop.play();
		
		gameStage.setResizable(false);
		gameStage.show();		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
