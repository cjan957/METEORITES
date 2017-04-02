/*
 * Game Class
 * 	Game class is the controller of the game. It creates and uses objects
 *  from view and model classes.
 */

package application;

import java.util.ArrayList;
import java.lang.Math;

import com.ttcj.components.Ball;
import com.ttcj.components.Bat;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game extends Application {

	//Instantiate game objects
	private Ball ball;
	private Bat bat;
	private Bat bat2;

	//This array list will store user input (key pressed).
	private ArrayList<String> input = new ArrayList<String>();

	//Defining constants
	private static final int WINDOW_W = 1024;
	private static final int WINDOW_H = 768;
	private static double angle1 = 0.0;
	private static double angle2 = 0.0;

	public static void main(String[] args) {
		//Launch javafx application / run start method
		launch(args);
	}

	@Override
	public void start(Stage gameStage) {

		//Create view object. gameStage will be set up with Canvas applied.
		//Please jump to View class for more info.
		View view = new View();
		view.viewSetup(gameStage);

		//Invoking gameInit method
		gameInit();

		//Create timeline object called 'gameLoop' that will repeat indefinitely
		Timeline gameLoop = new Timeline();
		gameLoop.setCycleCount(Timeline.INDEFINITE);
		
		//Setup so that the game Refresh/Repeat every 0.016 second (equals to approximately 60fps)
		KeyFrame gameFrame = new KeyFrame(Duration.seconds(0.016), event -> {
			//Obtain user key pressed from the view class
			input = view.accessUserInput();
			
			//'Tick', run the game by 1 frame.
			tick();
			
			//Render each object on canvas using GraphicContext (gc) set up
			//from the View class. Clear canvas with transparent color after
			//each frame
			view.canvasGC().clearRect(0, 0, 1024, 768);
			bat.render(view.canvasGC());
			bat2.render(view.canvasGC());
			ball.render(view.canvasGC());
		});

		//Play and Repeat the graphic rendering
		gameLoop.getKeyFrames().add(gameFrame);
		gameLoop.play();

	}

	public void tick() {
		//Move the ball once, checking necessary conditions.
		ball.moveThatBall();
		
		//Check user input and move the paddle accordingly
		if (input.contains("LEFT")) {
			// check LHS boundary condition so that the paddle won't be able to
			// go beyond the limit
			if (bat.GetxPosition() >= 0) {
				//Increase angle so the paddle/bat can move in circular path as 
				//defined by the maths function
				angle1 += 0.05;
				bat.setXPos((int) (Math.cos(angle1) * (WINDOW_H / 3)));
				bat.setYPos((int) (Math.sin(angle1) * (WINDOW_H / 3)));
			}
		} else if (input.contains("RIGHT")) {
			// check RHS boundary condition
			if (bat.GetyPosition() > 0) {
				angle1 -= 0.05;
				bat.setXPos((int) (Math.cos(angle1) * (WINDOW_H / 3)));
				bat.setYPos((int) (Math.sin(angle1) * (WINDOW_H / 3)));
			}
		}

		if (input.contains("A")) {
			// check LHS boundary condition so that the paddle won't be able to
			// go beyond the limit
			if (bat2.GetxPosition() >= 0) {
				angle2 += 0.05;
				bat2.setXPos((int) (Math.cos(angle2) * (WINDOW_H / 3)));
				bat2.setYPos((int) (704 - Math.sin(angle2) * (WINDOW_H / 3)));
			}
		} else if (input.contains("D")) {
			// check bottom boundary condition
			if (bat2.GetyPosition() < WINDOW_H - 64) {
				angle2 -= 0.05;
				bat2.setXPos((int) (Math.cos(angle2) * (WINDOW_H / 3)));
				bat2.setYPos((int) (704 - Math.sin(angle2) * (WINDOW_H / 3)));
			}
		}

		//TODO: Please comment KEison
		if ((ball.getXPos() <= bat.GetxPosition() + 32) // bat1
				&& (ball.getXPos() >= bat.GetxPosition() - 32) && (ball.getYPos() <= bat.GetyPosition() + 32)
				&& (ball.getYPos() >= bat.GetyPosition() - 32)) {
			ball.setXVelocity(-ball.getXVelocity());
			ball.setYVelocity(ball.getYVelocity());
		}

		if ((ball.getXPos() <= bat2.GetxPosition() + 32) // bat2
				&& (ball.getXPos() >= bat2.GetxPosition() - 32) && (ball.getYPos() <= bat2.GetyPosition() + 32)
				&& (ball.getYPos() >= bat2.GetyPosition() - 32)) {
			ball.setXVelocity(-ball.getXVelocity());
			ball.setYVelocity(ball.getYVelocity());
		}
	}

	public void gameInit() {
		
		//Create objects needed for the game play
		//with necessary properties. 
		bat = new Bat();
		bat.setImage("rsz_1paddle2.png");
		bat.SetxPosition(WINDOW_H / 3);
		bat.SetyPosition(0);

		bat2 = new Bat();
		bat2.setImage("rsz_1paddle2.png");
		bat2.SetxPosition(WINDOW_H / 3);
		bat2.SetyPosition(WINDOW_H - 64);

		ball = new Ball();
		ball.setImage("b10008.png");
		ball.SetxPosition(WINDOW_W / 2 - 32);
		ball.SetyPosition(WINDOW_H / 2);
		ball.setXVelocity(5);
		ball.setYVelocity(5);
	}

	public boolean isFinished() {
		//Not yet implemented
		return false;
	}

	public void setTimeRemaining(int seconds) {
		//Not yet implemented
	}
}