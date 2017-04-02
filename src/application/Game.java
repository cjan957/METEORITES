package application;
	
//codeSplit
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

import com.ttcj.components.Ball;
import com.ttcj.components.Base;
import com.ttcj.components.Bat;
import com.ttcj.testing.IGame;

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
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Game extends Application{
	
	private View view;
	
	private Ball ball;
	private Bat bat;
	private Bat bat2;
	
	private ArrayList<String> input = new ArrayList<String>();
	
	private static final int WINDOW_W = 1024;
	private static final int WINDOW_H = 768;
	private static double angle1  = 0.0;
	private static double angle2  = 0.0;
	

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage gameStage) {

		View view = new View();
		view.viewSetup(gameStage);
		
		gameInit();
			
		Timeline gameLoop = new Timeline();
		gameLoop.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameFrame = new KeyFrame(Duration.seconds(0.016), event ->{ //60fps
			input = view.accessUserInput();
			tick();			
			view.canvasGC().clearRect(0, 0, 1024, 768);		
			bat.render(view.canvasGC());	
			bat2.render(view.canvasGC());
			ball.render(view.canvasGC());
		});
		
		gameLoop.getKeyFrames().add(gameFrame);
		gameLoop.play();
		
	}
	
	public void tick() {
		ball.moveThatBall();
		if(input.contains("LEFT")){
			//check LHS boundary condition so that the paddle won't be able to
			//go beyond the limit
			if(bat.GetxPosition() >= 0){
				angle1 += 0.05;
				//System.out.println("ANGLE: " +angle1);

				bat.setXPos((int)(Math.cos(angle1)*(WINDOW_H/3)));
				bat.setYPos((int)(Math.sin(angle1)*(WINDOW_H/3)));
				//System.out.println("LEFT Bat co-or x: "+bat.GetxPosition());
				//System.out.println("LEFT Bat co-or y: "+bat.GetyPosition());
			}
		}
		else if(input.contains("RIGHT")){
			//check RHS boundary condition
			if(bat.GetyPosition() > 0){
				angle1 -= 0.05; 
				//System.out.println("ANGLE: " +angle1);
				
				bat.setXPos((int)(Math.cos(angle1)*(WINDOW_H/3)));
				bat.setYPos((int)(Math.sin(angle1)*(WINDOW_H/3)));
				//System.out.println("RIGHT Bat co-or x: "+bat.GetxPosition());
				//System.out.println("RIGHT Bat co-or y: "+bat.GetyPosition());
			}
		}		
		
		if(input.contains("A")){
			//check LHS boundary condition so that the paddle won't be able to
			//go beyond the limit
			if(bat2.GetxPosition() >= 0){
				angle2 += 0.05; 
				bat2.setXPos((int)(Math.cos(angle2)*(WINDOW_H/3)));
				bat2.setYPos((int)(704 - Math.sin(angle2)*(WINDOW_H/3)));
			}
		}
		else if(input.contains("D")){
			//check bottom boundary condition
			if(bat2.GetyPosition() < WINDOW_H-64){
				angle2 -= 0.05; 
				bat2.setXPos((int)(Math.cos(angle2)*(WINDOW_H/3)));
				bat2.setYPos((int)(704 - Math.sin(angle2)*(WINDOW_H/3)));
			}
		}		

		
		if((ball.getXPos() <= bat.GetxPosition() + 32)   //bat1
				&& (ball.getXPos() >= bat.GetxPosition() - 32)
				&& (ball.getYPos() <= bat.GetyPosition() + 32)
				&& (ball.getYPos() >= bat.GetyPosition() -32)){
				ball.setXVelocity(-ball.getXVelocity());
				ball.setYVelocity(ball.getYVelocity());
		}
		
		if((ball.getXPos() <= bat2.GetxPosition() + 32)   //bat2
				&& (ball.getXPos() >= bat2.GetxPosition() - 32)
				&& (ball.getYPos() <= bat2.GetyPosition() + 32)
				&& (ball.getYPos() >= bat2.GetyPosition() -32)){
				ball.setXVelocity(-ball.getXVelocity());
				ball.setYVelocity(ball.getYVelocity());
		}
		

//		 Rotate(double angle, double pivotX, double pivotY)
//		 Creates a two-dimensional Rotate transform with pivot.
		
		}//end tick		
	

	public void gameInit(){
		//Create bat objects // CHANGE TO CONSTRUCTOR!
		bat = new Bat();
		bat.setImage("rsz_1basketball.png");
		bat.SetxPosition(WINDOW_H/3);
		bat.SetyPosition(0);
						
			
		bat2 = new Bat();
		bat2.setImage("rsz_1basketball.png");
		bat2.SetxPosition(WINDOW_H/3);
		bat2.SetyPosition(WINDOW_H-64);
				
					
		ball = new Ball();
		ball.setImage("rsz_1basketball.png");
		ball.SetxPosition(WINDOW_W / 2 - 32);
		ball.SetyPosition(WINDOW_H / 2);
		ball.setXVelocity(5);
		ball.setYVelocity(5);
	}
	
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setTimeRemaining(int seconds) {
		// TODO Auto-generated method stub	
	}
}