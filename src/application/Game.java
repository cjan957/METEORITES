/*
 * Game Class
 * 	Game class is the controller of the game. It creates and uses objects
 *  from view and model classes.
 */

package application;

import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Math;

import com.ttcj.components.Ball;
import com.ttcj.components.Base;
import com.ttcj.components.Bat;
import com.ttcj.components.Brick;
import com.ttcj.components.BrickManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.scene.media.MediaPlayer; 
import javafx.scene.media.Media;
import java.io.File;

public class Game extends Application {
		
	//QUICK DEBUGGING OPTIONS
	private boolean inGameBallSpeedAdjust = true;

	// Instantiate game objects
	private Timer timer3sec;
	private Timer timer120sec;

	private Ball ball;
	private Bat bat;
	private Bat bat2;

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
	private static double angle1 = 0.0;
	private static double angle2 = 0.0;

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
	
	public void startCountDowntimeUp(){
		timer3sec.setTimeOut(true);
		timer3sec.setVisibility(false);
		timer120sec.setVisibility(true);
	}
	
	public void checkActualTimeRemaining(){
		if(timer120sec.getTime() == 0){
			concludeTheGame();
		}
	}
	
	public void concludeTheGame(){
		System.out.println("gameFinish");
		view.forcePause();
		
	}
	
	
	public void startMasterTimer(){
		
		//counting by one second, 120 times (2 mins)
		Timeline renderTimer = new Timeline(new KeyFrame(Duration.seconds(1), timeout ->{
			if(!view.isPause()){
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
		
		//Counting down from 3 to 0, decrement the timer for 1 sec every one second.
		Timeline renderKeyFrame = new Timeline(new KeyFrame(Duration.seconds(1), timeout-> {
			timer3sec.countDown();
		}
		));
		renderKeyFrame.setCycleCount(3); //repeat 1 sec for 3 times
		renderKeyFrame.play();
		
		//Call timeUp method, after 3 seconds time is up
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

			//Game proceeds if not paused
			if (!view.isPause() && timer3sec.isTimeOut()){
				countDown.stop();
				tick();		
			}	
			
			// Render each object on canvas using GraphicContext (gc) set up
			// from the View class. Clear canvas with transparent color after
			// each frame
			view.canvasGC().clearRect(0, 0, 1024, 768);		
			
			
			//Check if the 3-2-1 countdown has finished, if yes then don't bother trying to render the text
			if(timer3sec.getVisibility()){ 
				timer3sec.renderCountDownTimer(view.canvasGC());
			}
			
			if(timer120sec.getVisibility()){
				timer120sec.renderMasterTimer(view.canvasGC());
			}
			
			if(!topLHSBase.isDead()){
				bat.render(view.canvasGC());
			}
			if(!bottomLHSBase.isDead()){
				bat2.render(view.canvasGC());
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
			if (input.contains("LEFT")) {
				// check LHS boundary condition so that the paddle won't be able
				// to
				// go beyond the limit
				if (bat.GetxPosition() >= 6) {
					// Increase angle so the paddle/bat can move in circular
					// path as
					// defined by the maths function
					angle1 += 0.05;
					bat.SetxPosition((int) (Math.cos(angle1) * (WINDOW_H / 3)));
					bat.SetyPosition((int) (Math.sin(angle1) * (WINDOW_H / 3)));
				}
			} else if (input.contains("RIGHT")) {
				// check RHS boundary condition
				if (bat.GetyPosition() > 0) {
					angle1 -= 0.05;
					bat.SetxPosition((int) (Math.cos(angle1) * (WINDOW_H / 3)));
					bat.SetyPosition((int) (Math.sin(angle1) * (WINDOW_H / 3)));
				}
			}
		}

		if (!bottomLHSBase.isDead()) {
			if (input.contains("A")) {
				// check LHS boundary condition so that the paddle won't be able
				// to
				// go beyond the limit
				if (bat2.GetxPosition() >= 6) {
					angle2 += 0.05;
					bat2.SetxPosition((int) (Math.cos(angle2) * (WINDOW_H / 3)));
					bat2.SetyPosition((int) (704 - Math.sin(angle2) * (WINDOW_H / 3)));
				}
			} else if (input.contains("D")) {
				// check bottom boundary condition
				if (bat2.GetyPosition() < WINDOW_H - 39) {
					angle2 -= 0.05;
					bat2.SetxPosition((int) (Math.cos(angle2) * (WINDOW_H / 3)));
					bat2.SetyPosition((int) (704 - Math.sin(angle2) * (WINDOW_H / 3)));
				}
			}
		}

		// TODO: DEBUG ONLY, remove when deliver
		if(inGameBallSpeedAdjust){
			if (input.contains("DOWN")) { // slow down
				ball.setXVelocity(((float) (ball.getXVelocity() / 1.1)));
				ball.setYVelocity(((float) (ball.getYVelocity() / 1.1)));
			} else if (input.contains("UP")) { // go faster!
				ball.setXVelocity((float) (ball.getXVelocity() * 1.1));
				ball.setYVelocity((float) (ball.getYVelocity() * 1.1));
			}
		}
		
		if(input.contains("PAGE_DOWN")){
			this.setTimeRemainingToFive();
		}
	}

	public void paddleCollisionCheck() {
		
		if(!topLHSBase.isDead()){
			if(ball.objectsIntersectBallAndPaddle(bat)){
				ball.setXVelocity(-ball.getXVelocity());
				playPaddleDeflectSound();
			}
		}
		if(!bottomLHSBase.isDead()){
			if(ball.objectsIntersectBallAndPaddle(bat2)){
				ball.setXVelocity(-ball.getXVelocity());
				playPaddleDeflectSound();
			}
		}
	}

	public void baseCollisionCheck() {
		if(!topLHSBase.isDead()){
			if (ball.objectsIntersectBallAndBase(topLHSBase)) {
				topLHSBase.setIsDead(true);
				playBaseHitSound();
				System.out.println("Blue");
			}
		}
		if(!topRHSBase.isDead()){ 
			if (ball.objectsIntersectBallAndBase(topRHSBase)) {
				topRHSBase.setIsDead(true);
				playBaseHitSound();
				System.out.println("Green ");
			}
		} 
	
		if(!bottomRHSBase.isDead()){
			if (ball.objectsIntersectBallAndBase(bottomRHSBase)) {
				bottomRHSBase.setIsDead(true);
				playBaseHitSound();
				System.out.println("Red");
			}
		}
		
		if(!bottomLHSBase.isDead()){
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
		bat = new Bat("paddle_32.png", WINDOW_H / 3, 0);
		bat2 = new Bat("paddle_32.png", WINDOW_H / 3, WINDOW_H - 32);
		ball = new Ball("b10008.png", WINDOW_W / 2 - 32, WINDOW_H / 2, 32, 32);

		baseInit();
		topLHSBrickInit();
		bottomLHSBrickInit();
		topRHSBrickInit();
		bottomRHSBrickInit();
		deflect = new Deflect();
	}

	public void baseInit() {
		topLHSBase = new Base("planet1_64.png", 0, 0, 64, 64);
		topRHSBase = new Base("planet2_64.png", WINDOW_W - 64, 0, 64, 64);
		bottomRHSBase = new Base("planet3_64.png", WINDOW_W - 64, WINDOW_H - 64, 64, 64);
		bottomLHSBase = new Base("planet4_64.png", 0, WINDOW_H - 64, 64, 64);
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
		for (int i = 0; i < 140; i += 33) { // Horizontal bricks 5 Bricks 0,33,66,99,132
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
	
	private void playPaddleDeflectSound(){
		String paddleDeflectSound = "Sounds/paddleDeflect.wav";	//Locations of sounds
		Media sound = new Media(new File(paddleDeflectSound).toURI().toString());
		MediaPlayer deflectSound = new MediaPlayer(sound);
		deflectSound.play();
	}
	
	private void playWallHitSound(){
		String wallHitSound = "Sounds/wallHit.wav";
		Media sound = new Media(new File(wallHitSound).toURI().toString());
		MediaPlayer hitSound = new MediaPlayer(sound);
		hitSound.play();
		
	}
	
	private void playBaseHitSound(){
		String baseHitSound = "Sounds/baseHit.wav";
		Media sound = new Media(new File(baseHitSound).toURI().toString());
		MediaPlayer baseHit = new MediaPlayer(sound);
		baseHit.play();
	}
	
	
}