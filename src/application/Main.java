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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class Main extends Application {
	
	private static final String GAMENAME = "Warlords";
	private static final int WINDOW_W = 1024;
	private static final int WINDOW_H = 768;

	
	@Override
	public void start(Stage gameStage) {
		
		gameStage.setTitle(GAMENAME);
		
		Group root = new Group();
		Scene theScene = new Scene(root);
		gameStage.setScene(theScene);
		
		Canvas canvas = new Canvas(WINDOW_W,WINDOW_H);
		root.getChildren().add(canvas);
		
		ArrayList<String> input = new ArrayList<String>();
		
		theScene.setOnKeyPressed(event->{
			String code = event.getCode().toString();
			if(!input.contains(code)){
				input.add(code);
			}	
			});
		
		theScene.setOnKeyReleased(event->{
			String code = event.getCode().toString();
			input.remove(code);
		});
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		Ball ball = new Ball();
		ball.setImage("rsz_1basketball.png");
		ball.SetxPosition((WINDOW_W)/2);
		ball.SetyPosition((WINDOW_H)/2);
	
		Timeline gameLoop = new Timeline();
		gameLoop.setCycleCount(Timeline.INDEFINITE);
		
		//final long timeStart = System.currentTimeMillis();
		
		KeyFrame gameFrame = new KeyFrame(Duration.seconds(0.016), event ->{
			if(input.contains("LEFT")){
				if(ball.GetxPosition() >= 0){
					ball.SetxPosition(ball.GetxPosition() - 5);
				}
			}
			else if(input.contains("RIGHT")){
				if(ball.GetxPosition() + 64 <= WINDOW_W){
					ball.SetxPosition(ball.GetxPosition() + 5);
				}
			}
			
			gc.clearRect(0, 0, 1024, 768);
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
