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

	// QUICK DEBUGGING OPTIONS
	private boolean inGameBallSpeedAdjust = true;

	// Instantiate game objects
	private Timer timer3sec;
	private Timer timer120sec;

	private Ball ball;
	private Bat topLHSbat;
	private Bat bottomLHSbat;
	private Bat topRHSbat;
	private Bat bottomRHSbat;

	private Base topLHSBase;
	private Base bottomLHSBase;
	private Base topRHSBase;
	private Base bottomRHSBase;

	private BrickManager topLHSBrick;
	private BrickManager bottomLHSBrick;
	private BrickManager topRHSBrick;
	private BrickManager bottomRHSBrick;

	private Deflect deflect;
	private View view;
	private Scene mainMenuScene;
	private Stage gameWindow;

	// This array list will store user input (key pressed).
	private ArrayList<String> input = new ArrayList<String>();

	// Defining constants
	private static final String GAMENAME = "Meteorites";
	private static final int WINDOW_W = 1024;
	private static final int WINDOW_H = 768;
//	private static double angle1 = 0.0;
//	private static double angle2 = 0.0;

	public static void main(String[] args) {
		// Launch javafx application / run start method
		launch(args);
	}

	@Override
	public void start(Stage gameStage) {
		gameWindow = gameStage;

		// Create view object. gameStage will be set up with Canvas applied.
		// Please jump to View class for more info.
		view = new View();

		// Add Title to JavaFX top level container (Stage)
		gameStage.setTitle(GAMENAME);

		Button button = new Button("Launch Game");
		Button button2 = new Button("Exit");
		button.setOnAction(e -> startGame1());
		button2.setOnAction(e -> System.exit(0));

		VBox listOfOptions = new VBox(10);
		listOfOptions.getChildren().addAll(button, button2);

		mainMenuScene = new Scene(listOfOptions, 1024, 768);

		gameStage.setScene(mainMenuScene);

		// Apply conditions to the stage
		gameStage.setResizable(false);
		gameStage.sizeToScene();

		// Show the stage on the screen
		gameStage.show();
	}

	public void startCountDowntimeUp() {
		timer3sec.setTimeOut(true);
		timer3sec.setVisibility(false);
		timer120sec.setVisibility(true);
	}

	public void checkActualTimeRemaining() {
		if (timer120sec.getTime() == 0) {
			timeOutFindWinner();
		} else {
			timeLeftFindWinner();
		}
	}

	public void timeLeftFindWinner() {

		if (!topLHSBase.isDead() && topRHSBase.isDead() && bottomLHSBase.isDead() && bottomRHSBase.isDead()) {
			topLHSBase.setIsWinner(true);
			System.out.println("Winner: " + topLHSBase.getBaseName());
			view.forcePause();

		} else if (topLHSBase.isDead() && !topRHSBase.isDead() && bottomLHSBase.isDead() && bottomRHSBase.isDead()) {
			topRHSBase.setIsWinner(true);
			System.out.println("Winner: " + topRHSBase.getBaseName());
			view.forcePause();

		} else if (topLHSBase.isDead() && topRHSBase.isDead() && !bottomLHSBase.isDead() && bottomRHSBase.isDead()) {
			bottomLHSBase.setIsWinner(true);
			System.out.println("Winner: " + bottomLHSBase.getBaseName());
			view.forcePause();

		} else if (topLHSBase.isDead() && topRHSBase.isDead() && bottomLHSBase.isDead() && !bottomRHSBase.isDead()) {
			bottomRHSBase.setIsWinner(true);
			System.out.println("Winner: " + bottomRHSBase.getBaseName());
			view.forcePause();

		}
	}

	public void timeOutFindWinner() {

		ArrayList<Integer> scoreList = new ArrayList<Integer>();
		ArrayList<Base> baseList = new ArrayList<Base>();

		scoreList.add(this.topLHSBrick.getNumberOfBrick());
		scoreList.add(this.topRHSBrick.getNumberOfBrick());
		scoreList.add(this.bottomLHSBrick.getNumberOfBrick());
		scoreList.add(this.bottomRHSBrick.getNumberOfBrick());

		baseList.add(this.topLHSBase);
		baseList.add(this.topRHSBase);
		baseList.add(this.bottomLHSBase);
		baseList.add(this.bottomRHSBase);

		int temp = 0;
		Base tempName;

		for (int i = 0; i < 4; i++) {
			for (int j = 1; j < (4 - i); j++) {
				if (scoreList.get(j - 1) < scoreList.get(j)) {
					temp = scoreList.get(j - 1);
					scoreList.set(j - 1, scoreList.get(j));
					scoreList.set(j, temp);

					tempName = baseList.get(j - 1);
					baseList.set(j - 1, baseList.get(j));
					baseList.set(j, tempName);
				}
			}
		}

		System.out.println(scoreList);
		System.out.println(baseList);

		baseList.get(0).setIsWinner(true);
		System.out.println("Winner: " + baseList.get(0).getBaseName());

		if (scoreList.get(1) == scoreList.get(0)) {
			baseList.get(1).setIsWinner(true);
			System.out.println("Winner: " + baseList.get(1).getBaseName());
		}
		if (scoreList.get(2) == scoreList.get(0)) {
			baseList.get(2).setIsWinner(true);
			System.out.println("Winner: " + baseList.get(2).getBaseName());

		}
		if (scoreList.get(3) == scoreList.get(0)) {
			baseList.get(3).setIsWinner(true);
			System.out.println("Winner: " + baseList.get(3).getBaseName());

		}

		view.forcePause();

	}

	public void startMasterTimer() {
		// counting by one second, 120 times (2 mins)
		Timeline renderTimer = new Timeline(new KeyFrame(Duration.seconds(1), timeout -> {
			if (!view.isPause()) {
				timer120sec.countDown();
				checkActualTimeRemaining();
			}
		}));
		renderTimer.setCycleCount(Timeline.INDEFINITE);
		renderTimer.play();
	}

	public void startGame1() {
		// Setup gameView
		view.setupGameView(gameWindow);
		// Invoking gameInit method
		gameInit();

		timer3sec = new Timer(3, true);
		timer120sec = new Timer(120, false);

		// Counting down from 3 to 0, decrement the timer for 1 sec every one
		// second.
		Timeline renderKeyFrame = new Timeline(new KeyFrame(Duration.seconds(1), timeout -> {
			timer3sec.countDown();
		}));
		renderKeyFrame.setCycleCount(3); // repeat 1 sec for 3 times
		renderKeyFrame.play();

		// Call timeUp method, after 3 seconds time is up
		Timeline countDown = new Timeline(new KeyFrame(Duration.millis(3000), timeout -> {
			startCountDowntimeUp();
			startMasterTimer();
		}));
		countDown.play();

		// Create timeline object called 'gameLoop' that will repeat
		// indefinitely
		Timeline gameLoop = new Timeline();
		gameLoop.setCycleCount(Timeline.INDEFINITE);

		// Setup so that the game Refresh/Repeat every 0.016 second (equals to
		// approximately 60fps)
		KeyFrame gameFrame = new KeyFrame(Duration.seconds(0.016), event -> {

			// Obtain user key pressed from the view class
			input = view.accessUserInput();

			// Game proceeds if not paused
			if (!view.isPause() && timer3sec.isTimeOut()) {
				countDown.stop();
				tick();
			}

			// Render each object on canvas using GraphicContext (gc) set up
			// from the View class. Clear canvas with transparent color after
			// each frame
			view.canvasGC().clearRect(0, 0, 1024, 768);

			// Check if the 3-2-1 countdown has finished, if yes then don't
			// bother trying to render the text
			if (timer3sec.getVisibility()) {
				timer3sec.renderCountDownTimer(view.canvasGC());
			}

			if (timer120sec.getVisibility()) {
				timer120sec.renderMasterTimer(view.canvasGC());
			}
			
			
			//
			if (!topLHSBase.isDead()) {
				topLHSbat.render(view.canvasGC());
			}
			if (!bottomLHSBase.isDead()) {
				bottomLHSbat.render(view.canvasGC());
			}
			if (!topRHSBase.isDead()) {
				topRHSbat.render(view.canvasGC());
			}
			if (!bottomRHSBase.isDead()) {
				bottomRHSbat.render(view.canvasGC());
			}

			ball.render(view.canvasGC());

			for (Brick brick : topLHSBrick.accessBrickArray()) {
				// view.canvasGC().fillRect(brick.GetxPosition(),
				// brick.GetyPosition(), brick.GetWidth(), brick.GetHeight());
				brick.render(view.canvasGC());
			}
			for (Brick brick : bottomLHSBrick.accessBrickArray()) {
				brick.render(view.canvasGC());
			}
			for (Brick brick : topRHSBrick.accessBrickArray()) {
				brick.render(view.canvasGC());
			}

			for (Brick brick : bottomRHSBrick.accessBrickArray()) {
				brick.render(view.canvasGC());
			}

			if (!topLHSBase.isDead()) {
				topLHSBase.render(view.canvasGC());
			}
			if (!topRHSBase.isDead()) {
				topRHSBase.render(view.canvasGC());
			}
			if (!bottomRHSBase.isDead()) {
				bottomRHSBase.render(view.canvasGC());
			}
			if (!bottomLHSBase.isDead()) {
				bottomLHSBase.render(view.canvasGC());
			}
		});

		// Play and Repeat the graphic rendering
		gameLoop.getKeyFrames().add(gameFrame);
		gameLoop.play();
	}

	// Tick, run the game by 1 frame
	public void tick() {

		// Move the ball once, checking necessary conditions.
		ball.moveThatBall();
		paddleCollisionCheck();
		wallCollisionCheck();
		baseCollisionCheck();

		// Check user input and move the paddle accordingly
		if (!topLHSBase.isDead()) {
			int increment = 7;
			if (input.contains("LEFT")) {
				if ( (topLHSbat.GetxPosition() == (WINDOW_H/3 )) && (topLHSbat.GetyPosition() >= 0) && (topLHSbat.GetyPosition() <= WINDOW_H/3) ) {		//vertical down
					topLHSbat.SetyPosition((int) (topLHSbat.GetyPosition() + increment));
//					System.out.println("x position: " + topLHSbat.GetxPosition());
//					System.out.println("y position: " + topLHSbat.GetyPosition());					
				}
				if ( (topLHSbat.GetxPosition() == (WINDOW_H/3 )) && (topLHSbat.GetyPosition() > WINDOW_H/3) ) {	//if it overshoots max point on vertical down
					topLHSbat.SetxPosition((int) (WINDOW_H/3));
					topLHSbat.SetyPosition((int) (WINDOW_H/3));		
				}
				if ( (topLHSbat.GetxPosition() >= 0) && (topLHSbat.GetxPosition() <= WINDOW_H/3) && (topLHSbat.GetyPosition() == WINDOW_H/3) ) {	//horizontal left
					topLHSbat.SetxPosition((int) (topLHSbat.GetxPosition() - increment));
				}
				if ( (topLHSbat.GetxPosition() < 0) && (topLHSbat.GetyPosition() == WINDOW_H/3) ){ //overshoot max pt on horizontal left
					topLHSbat.SetxPosition((int) (0));
					topLHSbat.SetyPosition((int) (WINDOW_H/3));
				} 
			}
			if (input.contains("RIGHT")) {
				if ( (topLHSbat.GetyPosition() == (WINDOW_H/3 )) && (topLHSbat.GetxPosition() >= 0) && (topLHSbat.GetxPosition() <= WINDOW_H/3) ) {	//horizontal right
					topLHSbat.SetxPosition((int) (topLHSbat.GetxPosition() + increment));			
				}
				if ( (topLHSbat.GetyPosition() == (WINDOW_H/3 )) && (topLHSbat.GetxPosition() > WINDOW_H/3) ) {	//if it overshoots max point on horizontal movement going right
					topLHSbat.SetxPosition((int) (WINDOW_H/3));
					topLHSbat.SetyPosition((int) (WINDOW_H/3));				
				}
				if ( (topLHSbat.GetyPosition() >= 0) && (topLHSbat.GetyPosition() <= WINDOW_H/3) && (topLHSbat.GetxPosition() == WINDOW_H/3) ) {	//vertical up
					topLHSbat.SetyPosition((int) (topLHSbat.GetyPosition() - increment));
				}
				if ( (topLHSbat.GetyPosition() < 0) && (topLHSbat.GetxPosition() == WINDOW_H/3) ){ //overshoot max pt on vertical up
					topLHSbat.SetxPosition((int) (WINDOW_H/3));
					topLHSbat.SetyPosition((int) (0));
				} 
			}
		}
		
		if (!bottomLHSBase.isDead()) {
			int increment = 7;
			if (input.contains("A")) {
				if ( (bottomLHSbat.GetxPosition() == (WINDOW_H/3)) && (bottomLHSbat.GetyPosition() <= 768) && (bottomLHSbat.GetyPosition() >= 768 - WINDOW_H/3 - 32) ) {	//vertical movement up
					bottomLHSbat.SetyPosition((int) (bottomLHSbat.GetyPosition() - increment));	
					System.out.println("x position: " + bottomLHSbat.GetxPosition());
					System.out.println("y position: " + bottomLHSbat.GetyPosition());	
				}
				if ( (bottomLHSbat.GetxPosition() == (WINDOW_H/3 )) && (bottomLHSbat.GetyPosition() < (768 - WINDOW_H/3 - 32)) ) {	//if it overshoots max point on vertical movement going up
					bottomLHSbat.SetxPosition((int) (WINDOW_H/3));
					bottomLHSbat.SetyPosition((int) (768 - WINDOW_H/3 - 32));	
					System.out.println("x position: " + bottomLHSbat.GetxPosition());
					System.out.println("y position: " + bottomLHSbat.GetyPosition());	
				}
				if ( (bottomLHSbat.GetxPosition() >= 0) && (bottomLHSbat.GetxPosition() <= WINDOW_H/3) && (bottomLHSbat.GetyPosition() == 768 - WINDOW_H/3 - 32) ) {//horizontal movement left
					bottomLHSbat.SetxPosition((int) (bottomLHSbat.GetxPosition() - increment));
					System.out.println("x position: " + bottomLHSbat.GetxPosition());
					System.out.println("y position: " + bottomLHSbat.GetyPosition());	
				}
				if ( (bottomLHSbat.GetxPosition() < 0) && (bottomLHSbat.GetyPosition() == 768 - WINDOW_H/3 - 32) ){ //overshoot max pt on horizontal going left
					bottomLHSbat.SetxPosition((int) (0));
					bottomLHSbat.SetyPosition((int) (768 - WINDOW_H/3 - 32));
					System.out.println("x position: " + bottomLHSbat.GetxPosition());
					System.out.println("y position: " + bottomLHSbat.GetyPosition());	
				} 
			}
			if (input.contains("D")) {
				if ( (bottomLHSbat.GetyPosition() == (768 - WINDOW_H/3 - 32 )) && (bottomLHSbat.GetxPosition() >= 0) && (bottomLHSbat.GetxPosition() <= WINDOW_H/3) ) {	//horizontal move right
					bottomLHSbat.SetxPosition((int) (bottomLHSbat.GetxPosition() + increment));			
				}
				if ( (bottomLHSbat.GetyPosition() == (768 - WINDOW_H/3 - 32)) && (bottomLHSbat.GetxPosition() > WINDOW_H/3) ) {	//if it overshoots max point on horizontal movement going right
					bottomLHSbat.SetxPosition((int) (WINDOW_H/3));
					bottomLHSbat.SetyPosition((int) (768-WINDOW_H/3 - 32));				
				}
				if ( (bottomLHSbat.GetyPosition() >= 768 - WINDOW_H/3 - 32) && (bottomLHSbat.GetyPosition() <= 768 - 32) && (bottomLHSbat.GetxPosition() == WINDOW_H/3) ) {	//vertical move down
					bottomLHSbat.SetyPosition((int) (bottomLHSbat.GetyPosition() + increment));
				}
				if ( (bottomLHSbat.GetyPosition() > 768) && (bottomLHSbat.GetxPosition() == WINDOW_H/3) ){ //overshoot max pt on vertical going down
					bottomLHSbat.SetxPosition((int) (WINDOW_H/3));
					bottomLHSbat.SetyPosition((int) (768 - 32));
				} 
			}
		}
		
		if (!topRHSBase.isDead()) {
			int increment = 7;
			if (input.contains("NUMPAD7")) {
				if ( (topRHSbat.GetxPosition() == (1024 - WINDOW_H/3 - 32)) && (topRHSbat.GetyPosition() >= 0) && (topRHSbat.GetyPosition() <= WINDOW_H/3) ) {		//vertical up
					topRHSbat.SetyPosition((int) (topRHSbat.GetyPosition() - increment));		
				}
				if ( (topRHSbat.GetxPosition() == (1024 - WINDOW_H/3 - 32)) && (topRHSbat.GetyPosition() < 0) ) {	//if it overshoots max point on vertical up
					topRHSbat.SetxPosition((int) (1024 - WINDOW_H/3 - 32));
					topRHSbat.SetyPosition((int) (0));		
				}
				if ( (topRHSbat.GetxPosition() >= 1024 - WINDOW_H/3 - 32) && (topRHSbat.GetxPosition() <= 1024 - 32) && (topRHSbat.GetyPosition() == WINDOW_H/3) ) {	//horizontal left
					topRHSbat.SetxPosition((int) (topRHSbat.GetxPosition() - increment));
				}
				if ( (topRHSbat.GetxPosition() < 1024 - WINDOW_H/3 - 32) && (topRHSbat.GetyPosition() == WINDOW_H/3) ){ //overshoot max pt on horizontal left 
					topRHSbat.SetxPosition((int) (1024 - WINDOW_H/3 - 32));
					topRHSbat.SetyPosition((int) (WINDOW_H/3));
				} 
			}
			if (input.contains("NUMPAD9")) {
				if ( (topRHSbat.GetyPosition() == (WINDOW_H/3)) && (topRHSbat.GetxPosition() >= 1024 - WINDOW_H/3 - 32) && (topRHSbat.GetxPosition() <= 1024 - 32) ) {	//horizontal right
					topRHSbat.SetxPosition((int) (topRHSbat.GetxPosition() + increment));			
				}
				if ( (topRHSbat.GetyPosition() == (WINDOW_H/3 )) && (topRHSbat.GetxPosition() > 1024 - 32) ) {	//if it overshoots max point on horizontal right
					topRHSbat.SetxPosition((int) (1024 - 32));
					topRHSbat.SetyPosition((int) (WINDOW_H/3));				
				}
				if ( (topRHSbat.GetyPosition() >= 0) && (topRHSbat.GetyPosition() <= WINDOW_H/3) && (topRHSbat.GetxPosition() == 1024 - WINDOW_H/3 - 32) ) {	//vertical down
					topRHSbat.SetyPosition((int) (topRHSbat.GetyPosition() + increment));
				}
				if ( (topRHSbat.GetyPosition() > WINDOW_H/3) && (topRHSbat.GetxPosition() == 1024 - WINDOW_H/3 - 32) ){ //overshoot max pt on vertical down
					topRHSbat.SetxPosition((int) (1024 - WINDOW_H/3 - 32));
					topRHSbat.SetyPosition((int) (WINDOW_H/3));
				} 
			}
		}
		
		if (!bottomRHSBase.isDead()) {
			int increment = 7;
			if (input.contains("NUMPAD1")) {
				if ( (bottomRHSbat.GetxPosition() == (1024 - WINDOW_H/3 - 32)) && (bottomRHSbat.GetyPosition() >= 768 - WINDOW_H/3 - 32 ) && (bottomRHSbat.GetyPosition() <= 768 - 32) ) {		//vertical down
					bottomRHSbat.SetyPosition((int) (bottomRHSbat.GetyPosition() + increment));		
				}
				if ( (bottomRHSbat.GetxPosition() == (1024 - WINDOW_H/3 - 32)) && (bottomRHSbat.GetyPosition() > 768 - 32) ) {	//if it overshoots max point on vertical down
					bottomRHSbat.SetxPosition((int) (1024 - WINDOW_H/3 - 32));
					bottomRHSbat.SetyPosition((int) (768 - 32));		
				}
				if ( (bottomRHSbat.GetxPosition() >= 1024 - WINDOW_H/3 - 32) && (bottomRHSbat.GetxPosition() <= 1024 - 32) && (bottomRHSbat.GetyPosition() == 768 - WINDOW_H/3 - 32) ) {	//horizontal left
					bottomRHSbat.SetxPosition((int) (bottomRHSbat.GetxPosition() - increment));
				}
				if ( (bottomRHSbat.GetxPosition() < 1024 - WINDOW_H/3 - 32) && (bottomRHSbat.GetyPosition() == 768 - WINDOW_H/3 - 32) ){ //overshoot max pt on horizontal left 
					bottomRHSbat.SetxPosition((int) (1024 - WINDOW_H/3 - 32));
					bottomRHSbat.SetyPosition((int) (768 - WINDOW_H/3 - 32));
				} 
			}
			if (input.contains("NUMPAD3")) {
				if ( (bottomRHSbat.GetyPosition() == (768 - WINDOW_H/3 - 32)) && (bottomRHSbat.GetxPosition() >= 1024 - WINDOW_H/3 - 32) && (bottomRHSbat.GetxPosition() <= 1024 - 32) ) {	//horizontal right
					bottomRHSbat.SetxPosition((int) (bottomRHSbat.GetxPosition() + increment));			
				}
				if ( (bottomRHSbat.GetyPosition() == (768 - WINDOW_H/3 - 32 )) && (bottomRHSbat.GetxPosition() > 1024 - 32) ) {	//if it overshoots max point on horizontal right
					bottomRHSbat.SetxPosition((int) (1024 - 32));
					bottomRHSbat.SetyPosition((int) (768 - WINDOW_H/3 - 32));				
				}
				if ( (bottomRHSbat.GetyPosition() >= 768 - WINDOW_H/3 - 32) && (bottomRHSbat.GetyPosition() <= 768 - 32) && (bottomRHSbat.GetxPosition() == 1024 - WINDOW_H/3 - 32) ) {	//vertical up
					bottomRHSbat.SetyPosition((int) (bottomRHSbat.GetyPosition() - increment));
				}
				if ( (bottomRHSbat.GetyPosition() < 768 - WINDOW_H/3 - 32) && (bottomRHSbat.GetxPosition() == 1024 - WINDOW_H/3 - 32) ){ //overshoot max pt on vertical up
					bottomRHSbat.SetxPosition((int) (1024 - WINDOW_H/3 - 32));
					bottomRHSbat.SetyPosition((int) (768 - WINDOW_H/3 - 32));
				} 
			}
		}
		


		// TODO: DEBUG ONLY, remove when deliver
		if (inGameBallSpeedAdjust) {
			if (input.contains("DOWN")) { // slow down
				ball.setXVelocity(((float) (ball.getXVelocity() / 1.1)));
				ball.setYVelocity(((float) (ball.getYVelocity() / 1.1)));
			} else if (input.contains("UP")) { // go faster!
				ball.setXVelocity((float) (ball.getXVelocity() * 1.1));
				ball.setYVelocity((float) (ball.getYVelocity() * 1.1));
			}
		}

		if (input.contains("PAGE_DOWN")) {
			this.setTimeRemainingToFive();
		}
	}

	public void paddleCollisionCheck() {

		if (!topLHSBase.isDead()) {
			if (ball.objectsIntersectBallAndPaddle(topLHSbat)) {
				ball.setXVelocity(-ball.getXVelocity());
				playPaddleDeflectSound();
			}
		}
		if (!bottomLHSBase.isDead()) {
			if (ball.objectsIntersectBallAndPaddle(bottomLHSbat)) {
				ball.setXVelocity(-ball.getXVelocity());
				playPaddleDeflectSound();
			}	
		}
		if (!topRHSBase.isDead()) {
			if (ball.objectsIntersectBallAndPaddle(topRHSbat)) {
				ball.setXVelocity(-ball.getXVelocity());
				playPaddleDeflectSound();
			}
		}
		if (!bottomRHSBase.isDead()) {
			if (ball.objectsIntersectBallAndPaddle(bottomRHSbat)) {
				ball.setXVelocity(-ball.getXVelocity());
				playPaddleDeflectSound();
			}	
		}
	}

	public void baseCollisionCheck() {
		if (!topLHSBase.isDead()) {
			if (ball.objectsIntersectBallAndBase(topLHSBase)) {
				topLHSBase.setIsDead(true);
				playBaseHitSound();
				System.out.println("Blue");
			}
		}
		if (!topRHSBase.isDead()) {
			if (ball.objectsIntersectBallAndBase(topRHSBase)) {
				topRHSBase.setIsDead(true);
				playBaseHitSound();
				System.out.println("Green ");
			}
		}

		if (!bottomRHSBase.isDead()) {
			if (ball.objectsIntersectBallAndBase(bottomRHSBase)) {
				bottomRHSBase.setIsDead(true);
				playBaseHitSound();
				System.out.println("Red");
			}
		}

		if (!bottomLHSBase.isDead()) {
			if (ball.objectsIntersectBallAndBase(bottomLHSBase)) {
				bottomLHSBase.setIsDead(true);
				playBaseHitSound();
				System.out.println("Yellow");
			}
		}
	}

	public void wallCollisionCheck() {

		Iterator<Brick> topLHSbrickList = topLHSBrick.accessBrickArray().iterator();
		Iterator<Brick> bottomLHSbrickList = bottomLHSBrick.accessBrickArray().iterator();
		Iterator<Brick> topRHSBrickList = topRHSBrick.accessBrickArray().iterator();
		Iterator<Brick> bottomRHSBrickList = bottomRHSBrick.accessBrickArray().iterator();

		while (topRHSBrickList.hasNext()) {
			Brick brick = topRHSBrickList.next();
			if (ball.objectsIntersect(brick)) {
				deflect.deflectTheBall(ball, brick.getArrangement());
				topRHSBrickList.remove();
				topRHSBrick.removeBrick();
				playWallHitSound();
				System.out.println("1 Brick destroyed, " + topRHSBrick.getNumberOfBrick() + " left: (TopRHS)");
			}
		}
		while (topLHSbrickList.hasNext()) {
			Brick brick = topLHSbrickList.next();
			if (ball.objectsIntersect(brick)) {
				deflect.deflectTheBall(ball, brick.getArrangement());
				topLHSbrickList.remove();
				topLHSBrick.removeBrick();
				playWallHitSound();
				System.out.println("1 Brick destroyed, " + topLHSBrick.getNumberOfBrick() + " left: (TopLHS)");

			}
		}
		while (bottomLHSbrickList.hasNext()) {
			Brick brick = bottomLHSbrickList.next();
			if (ball.objectsIntersect(brick)) {
				deflect.deflectTheBall(ball, brick.getArrangement());
				bottomLHSbrickList.remove();
				bottomLHSBrick.removeBrick();
				playWallHitSound();
				System.out.println("1 Brick destroyed, " + bottomLHSBrick.getNumberOfBrick() + " left: (BottomLHS)");

			}
		}
		while (bottomRHSBrickList.hasNext()) {
			Brick brick = bottomRHSBrickList.next();
			if (ball.objectsIntersect(brick)) {
				deflect.deflectTheBall(ball, brick.getArrangement());
				bottomRHSBrickList.remove();
				bottomRHSBrick.removeBrick();
				playWallHitSound();
				System.out.println("1 Brick destroyed, " + bottomRHSBrick.getNumberOfBrick() + " left: (BottomRHS)");

			}
		}
		deflect.setTempDir(99, 99);
	}

	public void gameInit() {
		// Create objects needed for the game play
		// with necessary properties.
		topLHSbat = new Bat("paddle_32.png", WINDOW_H / 3, WINDOW_H / 3);
		bottomLHSbat = new Bat("paddle_32.png", WINDOW_H / 3, 768 - WINDOW_H/3 - 32);
		topRHSbat = new Bat("paddle_32.png", 1024 - WINDOW_H / 3 - 32, WINDOW_H / 3);
		bottomRHSbat = new Bat("paddle_32.png", 1024 - WINDOW_H / 3 - 32, 768 - WINDOW_H / 3 - 32);
		ball = new Ball("b10008.png", WINDOW_W / 2 - 32, WINDOW_H / 2, 32, 32);

		baseInit();
		topLHSBrickInit();
		bottomLHSBrickInit();
		topRHSBrickInit();
		bottomRHSBrickInit();
		deflect = new Deflect();
	}

	public void baseInit() {
		topLHSBase = new Base("TopLHS", "planet1_64.png", 0, 0, 64, 64);
		topRHSBase = new Base("TopRHS", "planet2_64.png", WINDOW_W - 64, 0, 64, 64);
		bottomRHSBase = new Base("BottomRHS", "planet3_64.png", WINDOW_W - 64, WINDOW_H - 64, 64, 64);
		bottomLHSBase = new Base("BottomLHS", "planet4_64.png", 0, WINDOW_H - 64, 64, 64);
	}

	public void bottomRHSBrickInit() {
		bottomRHSBrick = new BrickManager();

		for (int i = 991; i > 826; i -= 33) { // Horizontal bricks Inner
			Brick brick = new Brick(i, WINDOW_H - 140 - 32, 32, 32, 0);
			bottomRHSBrick.addBrick(brick);
		}

		for (int j = 728; j > 568; j -= (33)) { // Vertical Bricks Inner
			Brick brick = new Brick(WINDOW_W - 180, j, 32, 32, 1);
			bottomRHSBrick.addBrick(brick);
		}

		for (int i = 991; i > 826; i -= 33) { // Horizontal bricks Outer
			Brick brick = new Brick(i, WINDOW_H - 140 - 64, 32, 32, 0);
			bottomRHSBrick.addBrick(brick);
		}

		for (int j = 728; j > 568; j -= 33) { // Vertical Bricks
			Brick brick = new Brick(WINDOW_W - 180 - 32, j, 32, 32, 1);
			bottomRHSBrick.addBrick(brick);
		}

	}

	public void topRHSBrickInit() {
		topRHSBrick = new BrickManager();

		// INNER
		for (int i = 991; i > 826; i -= 33) { // Horizontal bricks
			Brick brick = new Brick(i, 180, 32, 32, 0);
			topRHSBrick.addBrick(brick);
		}

		for (int j = 0; j < 140; j += 33) { // Vertical Bricks
			Brick brick = new Brick(WINDOW_W - 180, j, 32, 32, 1);
			topRHSBrick.addBrick(brick);
		}

		// OUTER WALL
		for (int i = 991; i > 826; i -= 33) { // Horizontal bricks
			Brick brick = new Brick(i, 148, 32, 32, 0);
			topRHSBrick.addBrick(brick);
		}

		for (int j = 0; j < 140; j += 33) { // Vertical Bricks
			Brick brick = new Brick(WINDOW_W - 180 - 32, j, 32, 32, 1);
			topRHSBrick.addBrick(brick);
		}
	}

	public void bottomLHSBrickInit() {
		bottomLHSBrick = new BrickManager();

		for (int i = 0; i < 140; i += 33) { // Horizontal bricks
			Brick brick = new Brick(i, WINDOW_H - 140 - 32, 32, 32, 0);
			bottomLHSBrick.addBrick(brick);
		}

		for (int j = 728; j > 568; j -= (33)) { // Vertical Bricks
			Brick brick = new Brick(180, j, 32, 32, 1);
			bottomLHSBrick.addBrick(brick);
		}

		// Outer Wall
		for (int i = 0; i < 140; i += 33) { // Horizontal bricks
			Brick brick = new Brick(i, WINDOW_H - 140 - 32 - 32, 32, 32, 0);
			bottomLHSBrick.addBrick(brick);
		}

		for (int j = 728; j > 568; j -= (33)) { // Vertical Bricks
			Brick brick = new Brick(148, j, 32, 32, 1);
			brick.setImage("iceblock_32.png");
			bottomLHSBrick.addBrick(brick);
		}
	}

	public void topLHSBrickInit() {
		topLHSBrick = new BrickManager();

		// TLHS
		for (int i = 0; i < 140; i += 33) { // Horizontal bricks 5 Bricks
											// 0,33,66,99,132
			Brick brick = new Brick(i, 180, 32, 32, 0);
			topLHSBrick.addBrick(brick);
		}

		for (int j = 0; j < 140; j += 33) { // Vertical Bricks
			Brick brick = new Brick(180, j, 32, 32, 1);
			topLHSBrick.addBrick(brick);
		}

		// INNER WALL
		for (int i = 0; i < 140; i += 33) { // Horizontal bricks
			Brick brick = new Brick(i, 148, 32, 32, 0);
			topLHSBrick.addBrick(brick);
		}

		for (int j = 0; j < 140; j += 33) { // Vertical Bricks
			Brick brick = new Brick(148, j, 32, 32, 1);
			topLHSBrick.addBrick(brick);
		}
	}

	public boolean isFinished() {
		return false;
	}

	public void setTimeRemainingToFive() {
		timer120sec.setTime(5);
	}

	private void playPaddleDeflectSound() {
		new Sound("Sounds/paddleDeflect.wav");
	}

	private void playWallHitSound() {
		new Sound("Sounds/wallHit.wav");
	}

	private void playBaseHitSound() {
		new Sound("Sounds/wallHit.wav");
	}

}