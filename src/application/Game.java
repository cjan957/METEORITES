/*
 * Game Class
 * 	Game class is the controller of the game. It creates and uses objects
 *  from view and model classes.
 */

package application;

import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Math;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.paint.Color;
import com.ttcj.components.Ball;
import com.ttcj.components.Bat;
import com.ttcj.components.Brick;
import com.ttcj.components.BrickManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game extends Application {

	//Instantiate game objects
	private Ball ball;
	private Bat bat;
	private Bat bat2;
	private BrickManager topLHSBrick;
	private BrickManager bottomLHSBrick;
	

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
			
			//Rectangle2D rec = new Rectangle2D((double)ball.GetxPosition(),(double)ball.GetyPosition(),ball.GetWidth(),ball.GetHeight());
			//view.canvasGC().setFill(Color.BLUE);
			view.canvasGC().fillRect(ball.GetxPosition()+16, ball.GetyPosition()+16, ball.GetWidth(), ball.GetHeight());
			ball.render(view.canvasGC());
			
			for(Brick brick : topLHSBrick.accessBrickArray()){
				view.canvasGC().fillRect(brick.GetxPosition()+1, brick.GetyPosition()+1, brick.GetWidth(), brick.GetHeight());
				brick.render(view.canvasGC());
			}
			
		});

		//Play and Repeat the graphic rendering
		gameLoop.getKeyFrames().add(gameFrame);
		gameLoop.play();

	}

	//Tick, run the game by 1 frame		
	public void tick() {
		//System.out.println("Tic toc");
		
		//Move the ball once, checking necessary conditions.
		ball.moveThatBall();
		wallCollisionCheck();
	
		//Check user input and move the paddle accordingly
		if (input.contains("LEFT")) {
			// check LHS boundary condition so that the paddle won't be able to
			// go beyond the limit
			if (bat.GetxPosition() >= 0) {
				//Increase angle so the paddle/bat can move in circular path as 
				//defined by the maths function
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

		if (input.contains("A")) {
			// check LHS boundary condition so that the paddle won't be able to
			// go beyond the limit
			if (bat2.GetxPosition() >= 0) {
				angle2 += 0.05;
				bat2.SetxPosition((int) (Math.cos(angle2) * (WINDOW_H / 3)));
				bat2.SetyPosition((int) (704 - Math.sin(angle2) * (WINDOW_H / 3)));
			}
		} else if (input.contains("D")) {
			// check bottom boundary condition
			if (bat2.GetyPosition() < WINDOW_H - 64) {
				angle2 -= 0.05;
				bat2.SetxPosition((int) (Math.cos(angle2) * (WINDOW_H / 3)));
				bat2.SetyPosition((int) (704 - Math.sin(angle2) * (WINDOW_H / 3)));
			}
		}
		
		if(input.contains("DOWN")){ //slow down
			ball.setXVelocity(((float)(ball.getXVelocity() / 1.2)));
			ball.setYVelocity(((float)(ball.getYVelocity() / 1.2)));
			System.out.println("S hit");
			System.out.println("Xvelo: "+ ball.getXVelocity());
			System.out.println("Yvelo: "+ ball.getYVelocity());
		}
		else if(input.contains("UP")){ //go faster!
			ball.setXVelocity((float)(ball.getXVelocity() * 1.1));
			ball.setYVelocity((float)(ball.getYVelocity() * 1.1));
			System.out.println("F hit");
			System.out.println("Xvelo: "+ ball.getXVelocity());
			System.out.println("Yvelo: "+ ball.getYVelocity());
		}

		// Ball - Bat collision algorithm
		// bat1 - ball
		if ((ball.GetxPosition() <= bat.GetxPosition() + 32) 		//check if ball collides with bat
				&& (ball.GetxPosition() >= bat.GetxPosition() - 32) && (ball.GetyPosition() <= bat.GetyPosition() + 32)
				&& (ball.GetyPosition() >= bat.GetyPosition() - 32)) {
			ball.setXVelocity(-ball.getXVelocity());		//reflect ball by reversing velocity
			ball.setYVelocity(ball.getYVelocity());
		}

		// bat2 - ball
		if ((ball.GetxPosition() <= bat2.GetxPosition() + 32) 		//check if ball collides with bat
				&& (ball.GetxPosition() >= bat2.GetxPosition() - 32) && (ball.GetyPosition() <= bat2.GetyPosition() + 32)
				&& (ball.GetyPosition() >= bat2.GetyPosition() - 32)) {
			ball.setXVelocity(-ball.getXVelocity());		//reflect ball by reversing velocity
			ball.setYVelocity(ball.getYVelocity());
		}
		
		
	}
	
	public void wallCollisionCheck(){
		Iterator<Brick> topLHSbrickList = topLHSBrick.accessBrickArray().iterator();
		while(topLHSbrickList.hasNext()){
			//System.out.println("Checking");
			Brick brick = topLHSbrickList.next();
			if(ball.objectsIntersect(brick)){
				//System.out.println("hit!");
				topLHSbrickList.remove();
				ballDeflect(brick.getArrangement());
			}
		}
	}
	
	public void ballDeflect(int brickArrangement){ //0 for horizontal, 1 for vertical
		//deflect the ball based on its direction before it hits an object
		
		
		
		if(!ball.getIfTravelRight() && ball.getIfTravelUp()){  // 0,1
			//ball.setXVelocity(-ball.getXVelocity());
			System.out.println("DEF1");
			if(brickArrangement == 0){
				ball.setYVelocity(-ball.getYVelocity());
			}
			else{
				ball.setXVelocity(-ball.getXVelocity());
			}
		}
		else if(ball.getIfTravelRight() && ball.getIfTravelUp()){ // 1,1
			//ball.setXVelocity(-ball.getXVelocity());
			System.out.println("DEF2");


			if(brickArrangement == 0){
				ball.setYVelocity(-ball.getYVelocity());
			}
			else{
				ball.setXVelocity(-ball.getXVelocity());
			}
		}
		else if(!ball.getIfTravelRight() && !ball.getIfTravelUp()){ // 0,0
			//ball.setXVelocity(-ball.getXVelocity());
			System.out.println("DEF3");


			if(brickArrangement == 0){
				ball.setYVelocity(-ball.getYVelocity());
			}
			else{
				ball.setXVelocity(-ball.getXVelocity());
			}
		}
		else if(ball.getIfTravelRight() && !ball.getIfTravelUp()){ // 1,0
//			System.out.println(ball.getXVelocity());
//			ball.setXVelocity(-ball.getXVelocity());
			System.out.println("DEF4");
//			System.out.println(ball.getXVelocity());


			if(brickArrangement == 0){
				ball.setYVelocity(-ball.getYVelocity());
			}
			else{
				ball.setXVelocity(-ball.getXVelocity());
			}
		}
		else{
			System.out.println("DEF5");

			ball.setXVelocity(-ball.getXVelocity());
			ball.setYVelocity(-ball.getYVelocity());
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
		ball.SetWidth(32);
		ball.SetHeight(32);
		
		topLHSBrick = new BrickManager();
		bottomLHSBrick = new BrickManager();

		for(int i = 0; i < 180; i+=20){ //Horizontal bricks
			Brick brick = new Brick();
			brick.setImage("rsz_1rsz_brick.png");
			brick.SetxPosition(i);
			brick.SetyPosition(140);
			brick.SetWidth(32);
			brick.SetHeight(32);
			brick.setArrangement(0);
			topLHSBrick.addBrick(brick);
		}
		
		for(int j = 0; j < 140; j+=20){ //Vertical Bricks
			Brick brick = new Brick();
			brick.setImage("rsz_1rsz_brick.png");
			brick.SetxPosition(180);
			brick.SetyPosition(j);
			brick.SetHeight(32);
			brick.SetWidth(32);
			brick.setArrangement(1);
			topLHSBrick.addBrick(brick);
		}
	}

	public boolean isFinished() {
		//Not yet implemented
		return false;
	}

	public void setTimeRemaining(int seconds) {
		//Not yet implemented
	}
}