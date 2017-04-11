package application;

import java.util.ArrayList;
import java.util.Iterator;

import com.ttcj.components.Ball;
import com.ttcj.components.Base;
import com.ttcj.components.Bat;
import com.ttcj.components.Brick;
import com.ttcj.components.Sound;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GamePlaySingle {
	
	// QUICK DEBUGGING OPTIONS
	private boolean inGameBallSpeedAdjust = true;
	private boolean showGhostBall = false;


	// Instantiate game objects
	private Timer timer3sec;
	private Timer timer120sec;

	private Ball ball;
	private Ball ghostBall;
	private Bat topLHSbat;
	private Bat bottomLHSbat;
	private Bat topRHSbat;
	private Bat bottomRHSbat;

	private Base topLHSBase;
	private Base bottomLHSBase;
	private Base topRHSBase;
	private Base bottomRHSBase;
	
	private Sound paddleDeflect;
	private Sound wallHit;
	private Sound baseHit;
	private Sound countdownVoice;
	private Sound countdownSound;
	private Sound winJingleSound;
	private Sound loseJingleSound;

	private BrickBuilder gameBrick;

	private Deflect deflect;
	private View view;
		
	private Stage gameWindow;
	
	private boolean playing = true;
	private boolean longMessage;
	private String message;

	// This array list will store user input (key pressed).
	private ArrayList<String> input = new ArrayList<String>();

	// Defining constants
	private static final int WINDOW_W = 1024;
	private static final int WINDOW_H = 768;
	
	
	public void stopTheGame(){
		playing = false;
	}
	
	public void startTheGame(){
		playing = true;
	}
	
	public GamePlaySingle(int gameMode, Stage gameStage){
			view = new View();
			gameWindow = gameStage;
			startGame();
	}

	
	public void startCountDowntimeUp() {
		timer3sec.setTimeOut(true);
		startTheGame();
		view.setCountDown3IsDone();
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
		
		//when all other players are dead, and you are the only one left
		if(topLHSBase.isDead() && topRHSBase.isDead() && bottomLHSBase.isDead() && !bottomRHSBase.isDead()){
			bottomRHSBase.isWinner();
			message = "You Win";	
			longMessage = false;
			playWinSound();
			stopTheGame(); 
			
		}
		else if(bottomRHSBase.isDead()){
			message = "You Lose";
			longMessage = false;
			playLoseSound();
			stopTheGame();
		}
	}

	public void timeOutFindWinner() {

		ArrayList<Integer> scoreList = new ArrayList<Integer>();
		ArrayList<Base> baseList = new ArrayList<Base>();

		//if dead, dont put into the arrays. Arrays will be used to find who has the most bricks left
		if(!this.topLHSBase.isDead()){
			scoreList.add(this.gameBrick.getTopLHSBrick().getNumberOfBrick());
			baseList.add(this.topLHSBase);
		}
		if(!this.topRHSBase.isDead()){
			scoreList.add(this.gameBrick.getTopRHSBrick().getNumberOfBrick());
			baseList.add(this.topRHSBase);
		}
		if(!this.bottomLHSBase.isDead()){
			scoreList.add(this.gameBrick.getBottomLHSBrick().getNumberOfBrick());
			baseList.add(this.bottomLHSBase);
		}
		if(!this.bottomRHSBase.isDead()){
			scoreList.add(this.gameBrick.getBottomRHSBrick().getNumberOfBrick());
			baseList.add(this.bottomRHSBase);
		}
		
		int arraySize = scoreList.size();
		int temp = 0;
		Base tempName;

		for (int i = 0; i < arraySize; i++) {
			for (int j = 1; j < (arraySize - i); j++) {
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

		int numberOfTies = 0;
		baseList.get(0).setIsWinner(true);
		String winnerName = baseList.get(0).getBaseName();
		//System.out.println("Winner: " + baseList.get(0).getBaseName());
		
		for(int i = 1; i < arraySize; i++){
			if(scoreList.get(i) == scoreList.get(0)){
				numberOfTies++;
				baseList.get(i).setIsWinner(true);
				winnerName = winnerName + ", "+ baseList.get(i).getBaseName();
			}
		}
		
		if(numberOfTies > 1){
			message = "Tie: " + winnerName;
			longMessage = true;
		}
		else{
			message = "Winner: " + winnerName;
			longMessage = false;
		}

		//stop the game
		stopTheGame();

	}

	public void startMasterTimer() {
		// counting by one second, 120 times (2 mins)
		Timeline renderTimer = new Timeline(new KeyFrame(Duration.seconds(1), timeout -> {
			if (!view.isPause() && playing) {
				timer120sec.countDown();
				checkActualTimeRemaining();
			}
		}));
		renderTimer.setCycleCount(Timeline.INDEFINITE);
		renderTimer.play();
	}

	public void startGame() {
		// Setup gameView
		view.setupGameView(gameWindow);
		// Invoking gameInit method
		gameInit();

		timer3sec = new Timer(3, true);
		timer120sec = new Timer(120, false);

		// Counting down from 3 to 0, decrement the timer for 1 sec every one
		// second.
		playCountdownVoice();
		Timeline renderKeyFrame = new Timeline(new KeyFrame(Duration.seconds(1), timeout -> {
			timer3sec.countDown();
		}));
		renderKeyFrame.setCycleCount(3); // repeat 1 sec for 3 times
		renderKeyFrame.play();

		// Call timeUp method, after 3 seconds time is up
		Timeline countDown = new Timeline(new KeyFrame(Duration.millis(3000), timeout -> {
			startCountDowntimeUp();
			startMasterTimer();
			playCountdownSound();
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
				if(playing){
					countDown.stop();
					tick();
				}
			}
			render();	
		});

		// Play and Repeat the graphic rendering
		gameLoop.getKeyFrames().add(gameFrame);
		gameLoop.play();
	}
	
	
	public void render(){
		view.canvasGC().clearRect(0, 0, 1024, 768);

		// Check if the 3-2-1 countdown has finished, if yes then don't
		// bother trying to render the text
		if (timer3sec.getVisibility()) {
			timer3sec.renderCountDownTimer(view.canvasGC());
		}

		if (timer120sec.getVisibility()) {
			timer120sec.renderMasterTimer(view.canvasGC());
		}
		
		
		// If the player is still alive, render the base and bat, otherwise do not render.
		//bat render
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
		
		//base render
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

		ball.render(view.canvasGC());
		if(showGhostBall){
			ghostBall.render(view.canvasGC());
		}

		for (Brick brick : this.gameBrick.getTopLHSBrick().accessBrickArray()) {
			// view.canvasGC().fillRect(brick.GetxPosition(),
			// brick.GetyPosition(), brick.GetWidth(), brick.GetHeight());
			brick.render(view.canvasGC());
		}
		for (Brick brick : this.gameBrick.getBottomLHSBrick().accessBrickArray()) {
			brick.render(view.canvasGC());
		}
		for (Brick brick : this.gameBrick.getTopRHSBrick().accessBrickArray()) {
			brick.render(view.canvasGC());
		}
		for (Brick brick : this.gameBrick.getBottomRHSBrick().accessBrickArray()) {
			brick.render(view.canvasGC());
		}
		
		if(view.isPause()){
			view.canvasGC().setFill(Color.YELLOW);
			view.canvasGC().setStroke(Color.RED);
			view.canvasGC().setLineWidth(2);
			Font theFont = Font.font("Arial", FontWeight.BOLD, 50);
			view.canvasGC().setFont(theFont);
			view.canvasGC().setTextAlign(TextAlignment.CENTER);
			view.canvasGC().fillText("PAUSED", 1024/2, (768/2)-50);
			view.canvasGC().strokeText("PAUSED", 1024/2, (768/2)-50);

		}
		
		if(view.confirmationShown()){
			view.canvasGC().setFill(Color.YELLOW);
			view.canvasGC().setLineWidth(2);
			Font theFont = Font.font("Arial", FontWeight.BOLD, 30);
			view.canvasGC().setFont(theFont);
			view.canvasGC().setTextAlign(TextAlignment.CENTER);
			view.canvasGC().fillText("Press Y to Exit. ESC to Resume", 1024/2, (768/2)+10);
		}
		
		//Display message on the screen after the game has finished
		if(!playing){
			if(!longMessage){
				view.canvasGC().setFill(Color.WHITE);
				view.canvasGC().setStroke(Color.BLACK);
				view.canvasGC().setLineWidth(2);
				Font theFont = Font.font("Arial", FontWeight.BOLD, 72);
				view.canvasGC().setFont(theFont);
				view.canvasGC().setTextAlign(TextAlignment.CENTER);
				view.canvasGC().fillText(message, 1024/2, 768/2);
				view.canvasGC().strokeText(message, 1024/2, 768/2);
				
				view.canvasGC().setFill(Color.WHITE);
				view.canvasGC().setStroke(Color.BLACK);
				view.canvasGC().setLineWidth(2);
				Font theSubFont = Font.font("Arial", FontWeight.BOLD, 19);
				view.canvasGC().setFont(theSubFont);
				view.canvasGC().setTextAlign(TextAlignment.CENTER);
				view.canvasGC().fillText("Press ESC to return to the main menu", 1024/2, (768/2)+100);
				
			}
			else{
				view.canvasGC().setFill(Color.WHITE);
				view.canvasGC().setLineWidth(2);
				view.canvasGC().setTextAlign(TextAlignment.CENTER);
				Font theFont = Font.font("Arial", FontWeight.BOLD, 30);
				view.canvasGC().setFont(theFont);
				view.canvasGC().fillText(message, 1024/2, 768/2);
				
				view.canvasGC().setFill(Color.WHITE);
				view.canvasGC().setStroke(Color.BLACK);
				view.canvasGC().setLineWidth(2);
				Font theSubFont = Font.font("Arial", FontWeight.BOLD, 19);
				view.canvasGC().setFont(theSubFont);
				view.canvasGC().setTextAlign(TextAlignment.CENTER);
				view.canvasGC().fillText("Press ESC to return to the main menu", 1024/2, (768/2)+80);

			}
		}
		
	}

	// Tick, run the game by 1 frame
	public void tick() {

		// Move the ball once, checking necessary conditions.
		ball.moveThatBall();
		ghostBall.moveThatBall();
		paddleCollisionCheck();
		wallCollisionCheck();
		baseCollisionCheck();
		
		if(input.contains("Z")){
			topLHSBase.setIsDead(true);
			System.out.println("topLHS forced dead");
		}
		if(input.contains("X")){
			topRHSBase.setIsDead(true);
			System.out.println("topRHSBase forced dead");
		}
		if(input.contains("C")){
			bottomLHSBase.setIsDead(true);
			System.out.println("tobottomLHSBasepLHS forced dead");
		}
		if(input.contains("V")){
			bottomRHSBase.setIsDead(true);
			System.out.println("bottomRHSBase forced dead");
		}

		// Check user input and move the paddle accordingly
		// Positioning has been adjusted (+/- 32) to take into account the reference point of the bat (topleft corner)
		if (!topLHSBase.isDead()) {
				double batMaxX = 256;
				double batMaxY = (WINDOW_H / 3);
				double gradient = (ball.getYVelocity()) / (ball.getXVelocity());
				double C = ball.GetyPosition() - (gradient * ball.GetxPosition());
					
				//If the ball is heading toward the AI Horizontal area
				if ( ((batMaxY-C)/gradient) > 0 && ((batMaxY-C)/gradient) < batMaxX){
					if(((ball.getYVelocity() < 0) && (ball.getXVelocity() < 0))){
						topLHSbat.makeAIMoveX(((batMaxY-C)/gradient) - 32);
					}
					else if((ball.getYVelocity() < 0) && (ball.getXVelocity() > 0)){
						topLHSbat.makeAIMoveX(((batMaxY-C)/gradient) + 32);
					}
				}
				
				//determine whether the ball will travel to the AI area (y = mx+c) y path of AI valid between 0 to 255
				else if((((gradient * batMaxX) + C ) < batMaxY ) && (((gradient * batMaxX) +  C) > 0)){ 
					if(((ball.getYVelocity() < 0) && (ball.getXVelocity() < 0))){ // ball direction is top left, apply calibration
						topLHSbat.makeAIMoveY(((gradient * batMaxX) + C) + 32);
					}
					else if((ball.getYVelocity() > 0) && (ball.getXVelocity() > 0)){ // ball direction is bottom left, apply calibration
						topLHSbat.makeAIMoveY(((gradient * batMaxX) + C) - 32);
					}
				}
				
				
				//GHOST BALL TO CONFUSE AI
				double gradientGhost = (ghostBall.getYVelocity()) / (ghostBall.getXVelocity());
				double CGhostBall = ghostBall.GetyPosition() - (gradientGhost * ball.GetxPosition());
					
				//If the ball is heading toward the AI Horizontal area
				if ( ((batMaxY-CGhostBall)/gradientGhost) > 0 && ((batMaxY-CGhostBall)/gradientGhost) < batMaxX){
					if(((ghostBall.getYVelocity() < 0) && (ghostBall.getXVelocity() < 0))){
						topLHSbat.makeAIMoveX(((batMaxY-CGhostBall)/gradientGhost) + 32);
					}
					else if((ghostBall.getYVelocity() < 0) && (ghostBall.getXVelocity() > 0)){
						topLHSbat.makeAIMoveX(((batMaxY-CGhostBall)/gradientGhost) - 32);
					}
				}
				//determine whether the ball will travel to the AI area (y = mx+c) y path of AI valid between 0 to 255

				else if((((gradientGhost * batMaxX) + CGhostBall ) < batMaxY ) && (((gradientGhost * batMaxX) +  CGhostBall) > 0)){ 
					if(((ghostBall.getYVelocity() < 0) && (ghostBall.getXVelocity() < 0))){ // ball direction is top left, apply calibration
						topLHSbat.makeAIMoveY(((gradientGhost * batMaxX) + CGhostBall) + 32);
					}
					else if((ghostBall.getYVelocity() > 0) && (ghostBall.getXVelocity() > 0)){ // ball direction is bottom left, apply calibration
						topLHSbat.makeAIMoveY(((gradientGhost * batMaxX) + CGhostBall) - 32);
					}
				}
		}
		
		if (!bottomLHSBase.isDead()) {
			double batMaxX = 256;
			double batMinY = (WINDOW_H - (WINDOW_H/3) - 32);
			double gradient = (ball.getYVelocity()) / (ball.getXVelocity());
			double C = ball.GetyPosition() - (gradient * ball.GetxPosition());
				
			//If the ball is heading toward the AI Horizontal area
			if ( ((batMinY-C)/gradient) > 0 && ((batMinY-C)/gradient) < batMaxX){
				if(((ball.getYVelocity() > 0) && (ball.getXVelocity() < 0))){
					bottomLHSbat.makeAIMoveX(((batMinY-C)/gradient) - 32);
				}
				else if((ball.getYVelocity() > 0) && (ball.getXVelocity() > 0)){
					bottomLHSbat.makeAIMoveX(((batMinY-C)/gradient) + 32);
				}
			}
			
			//determine whether the ball will travel to the AI vertical area (y = mx+c) y path of AI valid between 0 to 255
			else if((((gradient * batMaxX) + C ) > batMinY ) && (((gradient * batMaxX) +  C) < WINDOW_H)){ 
				if(((ball.getYVelocity() > 0) && (ball.getXVelocity() < 0))){ //
					bottomLHSbat.makeAIMoveY(((gradient * batMaxX) + C) - 32);
				}
				else if((ball.getYVelocity() < 0) && (ball.getXVelocity() < 0)){ // 
					bottomLHSbat.makeAIMoveY(((gradient * batMaxX) + C) + 32);
				}
			}

		}
		
		if (!topRHSBase.isDead()) {
				double batMinX = (WINDOW_W - (WINDOW_H/3) - 32);
				double batMaxY = (WINDOW_H / 3);
				double gradient = (ball.getYVelocity()) / (ball.getXVelocity());
				double C = ball.GetyPosition() - (gradient * ball.GetxPosition());
				
				//determine whether the ball will travel to the AI area (y = mx+c) y path of AI valid between 0 to 255
				//736 is the AI x Position when it can only move vertically		
				if((((gradient * batMinX) + C ) < batMaxY ) && (((gradient * batMinX) +  C) > 0)){ 
					if(((ball.getYVelocity() < 0) && (ball.getXVelocity() > 0))){ // ball traveling to top right
						topRHSbat.makeAIMoveY(((gradient * 736) + C) + 30);
					}
					else if((ball.getYVelocity() > 0) && (ball.getXVelocity() > 0)){ // ball traveling to bottom right, apply calibration
						topRHSbat.makeAIMoveY(((gradient * 736) + C) - 30);
					}
				}	
				//If the ball is heading toward the AI Horizontal area
				else if ( ((batMaxY-C)/gradient) > batMinX && ((batMaxY-C)/gradient) < WINDOW_W){
					if(((ball.getYVelocity() < 0) && (ball.getXVelocity() > 0))){
						topRHSbat.makeAIMoveX(((batMaxY-C)/gradient) +32);
					}
					else if((ball.getYVelocity() < 0) && (ball.getXVelocity() < 0)){
						topRHSbat.makeAIMoveX(((batMaxY-C)/gradient) +32);
					}
				}
				
				
				double ghostGradient = (ghostBall.getYVelocity()) / (ghostBall.getXVelocity());
				double ghostC = ghostBall.GetyPosition() - (ghostGradient * ghostBall.GetxPosition());
				
				//determine whether the ball will travel to the AI area (y = mx+c) y path of AI valid between 0 to 255
				//736 is the AI x Position when it can only move vertically		
				if((((ghostGradient * batMinX) + ghostC ) < batMaxY ) && (((ghostGradient * batMinX) +  ghostC) > 0)){ 
					if(((ghostBall.getYVelocity() < 0) && (ghostBall.getXVelocity() > 0))){ // ball traveling to top right
						topRHSbat.makeAIMoveY(((ghostGradient * 736) + ghostC) + 30);
					}
					else if((ghostBall.getYVelocity() > 0) && (ghostBall.getXVelocity() > 0)){ // ball traveling to bottom right, apply calibration
						topRHSbat.makeAIMoveY(((ghostGradient * 736) + ghostC) - 30);
					}
				}	
				//If the ball is heading toward the AI Horizontal area
				else if ( ((batMaxY-ghostC)/ghostGradient) > batMinX && ((batMaxY-ghostC)/ghostGradient) < WINDOW_W){
					if(((ghostBall.getYVelocity() < 0) && (ghostBall.getXVelocity() > 0))){
						topRHSbat.makeAIMoveX(((batMaxY-ghostC)/ghostGradient) +32);
					}
					else if((ghostBall.getYVelocity() < 0) && (ghostBall.getXVelocity() < 0)){
						topRHSbat.makeAIMoveX(((batMaxY-ghostC)/ghostGradient) +32);
					}
				}
				
				
			}
		
		if (!bottomRHSBase.isDead()) {
			int increment = 7;
			if (input.contains("LEFT")) {
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
			if (input.contains("RIGHT")) {
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

		Iterator<Brick> topLHSBrickList = this.gameBrick.getTopLHSBrick().accessBrickArray().iterator();
		Iterator<Brick> bottomLHSbrickList = this.gameBrick.getBottomLHSBrick().accessBrickArray().iterator();
		Iterator<Brick> topRHSBrickList = this.gameBrick.getTopRHSBrick().accessBrickArray().iterator();
		Iterator<Brick> bottomRHSBrickList = this.gameBrick.getBottomRHSBrick().accessBrickArray().iterator();

		while (topRHSBrickList.hasNext()) {
			Brick brick = topRHSBrickList.next();
			if (ball.objectsIntersect(brick)) {
				deflect.deflectTheBall(ball, brick.getArrangement());
				topRHSBrickList.remove();
				this.gameBrick.getTopRHSBrick().removeBrick();
				playWallHitSound();
				System.out.println("1 Brick destroyed, " + this.gameBrick.getTopRHSBrick().getNumberOfBrick() + " left: (TopRHS)");
			}
		}
		while (topLHSBrickList.hasNext()) {
			Brick brick = topLHSBrickList.next();
			if (ball.objectsIntersect(brick)) {
				deflect.deflectTheBall(ball, brick.getArrangement());
				topLHSBrickList.remove();
				this.gameBrick.getTopLHSBrick().removeBrick();
				playWallHitSound();
				System.out.println("1 Brick destroyed, " + this.gameBrick.getTopLHSBrick().getNumberOfBrick() + " left: (TopLHS)");

			}
		}
		while (bottomLHSbrickList.hasNext()) {
			Brick brick = bottomLHSbrickList.next();
			if (ball.objectsIntersect(brick)) {
				deflect.deflectTheBall(ball, brick.getArrangement());
				bottomLHSbrickList.remove();
				this.gameBrick.getBottomLHSBrick().removeBrick();
				playWallHitSound();
				System.out.println("1 Brick destroyed, " + this.gameBrick.getBottomLHSBrick().getNumberOfBrick() + " left: (BottomLHS)");

			}
		}
		while (bottomRHSBrickList.hasNext()) {
			Brick brick = bottomRHSBrickList.next();
			if (ball.objectsIntersect(brick)) {
				deflect.deflectTheBall(ball, brick.getArrangement());
				bottomRHSBrickList.remove();
				this.gameBrick.getBottomRHSBrick().removeBrick();
				playWallHitSound();
				System.out.println("1 Brick destroyed, " + this.gameBrick.getBottomRHSBrick().getNumberOfBrick() + " left: (BottomRHS)");

			}
		}
		deflect.setTempDir(99, 99);
	}

	public void gameInit() {
		// Create objects needed for the game play
		// with necessary properties. <Image, xPos, yPos, isAI, location>
		topLHSbat = new Bat("paddle_32.png", WINDOW_H / 3, WINDOW_H / 3, true, Bat.batPosition.TopLEFT);	
		bottomLHSbat = new Bat("paddle_32.png", WINDOW_H / 3, 768 - WINDOW_H/3 - 32, true, Bat.batPosition.BottomLEFT);
		topRHSbat = new Bat("paddle_32.png", 1024 - WINDOW_H / 3 - 32, WINDOW_H / 3, true, Bat.batPosition.TopRIGHT);
		bottomRHSbat = new Bat("paddle_32.png", 1024 - WINDOW_H / 3 - 32, 768 - WINDOW_H / 3 - 32, false, Bat.batPosition.BottomRIGHT);
		ball = new Ball("b10008.png", WINDOW_W / 2 - 32, WINDOW_H / 2, 32, 32);
		ghostBall = new Ball("b10008.png", WINDOW_W / 2 - 32, WINDOW_H / 2 - 16, 32, 32);

		
		 paddleDeflect = new Sound("Sounds/paddleDeflect.wav");
		 wallHit = new Sound("Sounds/wallHit.wav");
		 baseHit = new Sound("Sounds/baseHit.aiff");
		 countdownVoice = new Sound("Sounds/countdownVoice.wav");
		 countdownSound = new Sound("Sounds/countdownSoundStart.wav");
		 winJingleSound = new Sound("Sounds/winJingle.wav");
		 loseJingleSound = new Sound("Sounds/loseJingle.wav");

		baseInit();	
		gameBrick = new BrickBuilder();
		deflect = new Deflect();
	}

	public void baseInit() {
		topLHSBase = new Base("Blue", "planet1_64.png", 0, 0, 64, 64);
		topRHSBase = new Base("Green", "planet2_64.png", WINDOW_W - 64, 0, 64, 64);
		bottomLHSBase = new Base("Yellow", "planet3_64.png", 0, WINDOW_H - 64, 64, 64);
		bottomRHSBase = new Base("Red", "planet4_64.png", WINDOW_W - 64, WINDOW_H - 64, 64, 64);
	}

	public boolean isFinished() {
		return false;
	}

	public void setTimeRemainingToFive() {
		timer120sec.setTime(2);
	}

	private void playPaddleDeflectSound() {
		this.paddleDeflect.playSound();
	}

	private void playWallHitSound() {
		this.wallHit.playSound();
	}

	private void playBaseHitSound() { 
		this.baseHit.playSound();
	}
	
	private void playCountdownVoice() {
		this.countdownVoice.playSound();
	}
	
	private void playCountdownSound() {
		this.countdownSound.playSound();
	}
	
	private void playWinSound() {
		this.winJingleSound.playSound();
	}
	
	private void playLoseSound() {
		this.loseJingleSound.playSound();
	}
	
}
