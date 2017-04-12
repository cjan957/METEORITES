package application;

import java.util.ArrayList;
import java.util.Iterator;
import com.ttcj.components.Base;
import com.ttcj.components.Brick;
import com.ttcj.components.Timer;
import com.ttcj.view.View;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GamePlay {
	
	// QUICK DEBUGGING OPTIONS
	private boolean inGameBallSpeedAdjust = true;
	private boolean showGhostBall = false;
	private boolean forceDeadkey = false;

	// Instantiate game objects
	private Timer timer3sec;
	private Timer timer120sec;
	
	private ComponentManager gameComponent;

	private Base topLHSBase;
	private Base bottomLHSBase;
	private Base topRHSBase;
	private Base bottomRHSBase;
		
	private BrickBuilder gameBrick;
	private Deflect deflect;

	private Stage gameWindow;
	private View view;
	
	//State game mode and whether the game is still going
	private boolean playing = true;
	
	public enum gameMode{
		SINGLE, MULTI
	}
	public gameMode currentGameMode;

	//message controller
	private boolean longMessage;
	private String message;
	
	// This array list will store user input (key pressed).
	private ArrayList<String> input = new ArrayList<String>();

	// Defining constants
	private static final int WINDOW_W = 1024;
	private static final int WINDOW_H = 768;
	
	
	public GamePlay(int gameModeNum, Stage gameStage){
			view = new View();
			gameWindow = gameStage;
			if(gameModeNum == 0){ //game mode 0 = single
				currentGameMode = gameMode.SINGLE;
			}
			else{ //game mode 1 = multi-player
				currentGameMode = gameMode.MULTI;
			}
			startGame();
	}
	
	public void startThreeSecondsTimer(){
		//Initilse timer objects
		timer3sec = new Timer(3, true);
		timer120sec = new Timer(120, false);
		playCountdownVoice();
		
		// Counting down from 3 to 0, decrement the timer for 1 sec every one
		// second.
		Timeline renderKeyFrame = new Timeline(new KeyFrame(Duration.seconds(1), timeout -> {
			timer3sec.countDown();
		}));
		renderKeyFrame.setCycleCount(3); // repeat 1 sec for 3 times
		renderKeyFrame.play();
	}
	
	public void startGame() {		
		// Setup gameView
		view.setupGameView(gameWindow);
		
		// Invoking gameInit method. 
		gameInit();
		startThreeSecondsTimer();

		// Call timeUp methods, after 3 seconds time is up. 
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
			// Render each object on canvas using GraphicContext (gc) set up
			// from the View class. Clear canvas with transparent color after
			// each frame
			render();			
		});

		// Play and Repeat the graphic rendering
		gameLoop.getKeyFrames().add(gameFrame);
		gameLoop.play();
	}
	
	public void gameInit() {	
		//Create game components in gameComponent class, what type of objects
		//will be created depends on the game mode
		gameComponent = new ComponentManager(currentGameMode);
		
		//Base Maker
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

	public void stopTheGame(){
		playing = false;
		view.setGameFinished();
	}
	
	public void startTheGame(){
		playing = true;
	}
	
	public void startCountDowntimeUp() {
		timer3sec.setTimeOut(true);
		startTheGame();
		
		//Notify the view that the 3s counter has finished
		view.setCountDown3IsDone();
		
		//Hide and show relevant timers on the screen.
		timer3sec.setVisibility(false);
		timer120sec.setVisibility(true);
	}

	public void checkActualTimeRemaining() {
		//If the game has finished (timer = 0), check for winner. Otherwise,
		//the game is still going so check if there's any in-game winner.
		if (timer120sec.getTime() == 0) {
			timeOutFindWinner();
		} else {
			timeLeftFindWinner();
		}
	}

	public void timeLeftFindWinner() {
		if(currentGameMode == gameMode.MULTI){
			if (!topLHSBase.isDead() && topRHSBase.isDead() && bottomLHSBase.isDead() && bottomRHSBase.isDead()) {
				topLHSBase.setIsWinner(true);
				message = "Winner: "+topLHSBase.getBaseName();	
				longMessage = false;
				playLoseSound();
				stopTheGame();
	
			} else if (topLHSBase.isDead() && !topRHSBase.isDead() && bottomLHSBase.isDead() && bottomRHSBase.isDead()) {
				topRHSBase.setIsWinner(true);
				message = "Winner: "+topRHSBase.getBaseName();
				playLoseSound();
				stopTheGame();
	
			} else if (topLHSBase.isDead() && topRHSBase.isDead() && !bottomLHSBase.isDead() && bottomRHSBase.isDead()) {
				bottomLHSBase.setIsWinner(true);
				message = "Winner: "+bottomLHSBase.getBaseName();
				playWinSound();
				stopTheGame();
	
			} else if (topLHSBase.isDead() && topRHSBase.isDead() && bottomLHSBase.isDead() && !bottomRHSBase.isDead()) {
				bottomRHSBase.setIsWinner(true);
				message = "Winner: "+bottomRHSBase.getBaseName();
				playWinSound();
				stopTheGame();
			}
			else if(bottomLHSBase.isDead() && bottomRHSBase.isDead()){
				message = "Blue and Green Win";
				System.out.println("AIs Win");
				playLoseSound();
				stopTheGame();
			}
		}
		else{ //Must be single player mode
			
			//when all other players are dead, and you are the only one left: You win
			if (topLHSBase.isDead() && topRHSBase.isDead() && bottomLHSBase.isDead() && !bottomRHSBase.isDead()){
				bottomRHSBase.isWinner();
				message = "You Win";	
				longMessage = false;
				playWinSound();
				stopTheGame(); 	
			}
			else if (bottomRHSBase.isDead()){
				message = "You Lose";
				longMessage = false;
				playLoseSound();
				stopTheGame();
			}
		}
	}

	public void timeOutFindWinner() {

		ArrayList<Integer> scoreList = new ArrayList<Integer>();
		ArrayList<Base> baseList = new ArrayList<Base>();

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
			if(currentGameMode == gameMode.SINGLE){
				//Play appropriate sound when in single player mode.
				if(winnerName.contains(bottomRHSBase.getBaseName())){
					playWinSound();
				} else {
					playLoseSound();
				}
			} else if (currentGameMode == gameMode.MULTI){
				//Play appropriate sound when in 2 player mode
				if(winnerName.contains(bottomRHSBase.getBaseName()) || winnerName.contains(bottomLHSBase.getBaseName())){
					playWinSound();
				} else {
					playLoseSound();
				}
			}
		}
		else{
			message = "Winner: " + winnerName;
			longMessage = false;
			if(currentGameMode == gameMode.SINGLE){
				if(winnerName == bottomRHSBase.getBaseName()){
					playWinSound();
				} else {
					playLoseSound();
				}
			} else if (currentGameMode == gameMode.MULTI){
				//Play appropriate sound when in 2 player mode
				if(winnerName == bottomRHSBase.getBaseName() || winnerName == bottomLHSBase.getBaseName()){
					playWinSound();
				} else {
					playLoseSound();
				}
			}
		}
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
		
		//BAT
		if (!topLHSBase.isDead()) {
			gameComponent.gettopLHSbat().render(view.canvasGC());
		}
		if (!bottomLHSBase.isDead()) {
			gameComponent.getbottomLHSbat().render(view.canvasGC());
		}
		if (!topRHSBase.isDead()) {
			gameComponent.gettopRHSbat().render(view.canvasGC());
		}
		if (!bottomRHSBase.isDead()) {
			gameComponent.getbottomRHSbat().render(view.canvasGC());
		}
		
		//BASE
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

		//Ball
		gameComponent.getBall().render(view.canvasGC());		
		if(showGhostBall){
			gameComponent.getGhostBall().render(view.canvasGC());
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
		
		//Display a message on the screen after the game has finished. 
		//What will be display here depends on what's in 'message' variable
		if(!playing){
			//If the message is not too long, display it with a big font
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
		gameComponent.getBall().moveThatBall();
		gameComponent.getGhostBall().moveThatBall();
		paddleCollisionCheck();
		wallCollisionCheck();
		baseCollisionCheck();
		
		//TODO: Add debugging option
		if(forceDeadkey){
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
		}

		// Check user input and move the paddle accordingly
		// Positioning has been adjusted (+/- 32) to take into account the reference point of the bat (topleft corner)
		if (!topLHSBase.isDead()) {
			if(!gameComponent.gettopLHSbat().isAIBat()){
				//If not AI Bat, let the keyboard controls the bat
				int increment = 7;
				if (input.contains("Q")) {
					if ( (gameComponent.gettopLHSbat().GetxPosition() == (WINDOW_H/3 )) && (gameComponent.gettopLHSbat().GetyPosition() >= 0) && (gameComponent.gettopLHSbat().GetyPosition() <= WINDOW_H/3) ) {		//vertical down
						gameComponent.gettopLHSbat().SetyPosition((int) (gameComponent.gettopLHSbat().GetyPosition() + increment));				
					}
					if ( (gameComponent.gettopLHSbat().GetxPosition() == (WINDOW_H/3 )) && (gameComponent.gettopLHSbat().GetyPosition() > WINDOW_H/3) ) {	//if it overshoots max point on vertical down
						gameComponent.gettopLHSbat().SetxPosition((int) (WINDOW_H/3));
						gameComponent.gettopLHSbat().SetyPosition((int) (WINDOW_H/3));		
					}
					if ( (gameComponent.gettopLHSbat().GetxPosition() >= 0) && (gameComponent.gettopLHSbat().GetxPosition() <= WINDOW_H/3) && (gameComponent.gettopLHSbat().GetyPosition() == WINDOW_H/3) ) {	//horizontal left
						gameComponent.gettopLHSbat().SetxPosition((int) (gameComponent.gettopLHSbat().GetxPosition() - increment));
					}
					if ( (gameComponent.gettopLHSbat().GetxPosition() < 0) && (gameComponent.gettopLHSbat().GetyPosition() == WINDOW_H/3) ){ //overshoot max point on horizontal left
						gameComponent.gettopLHSbat().SetxPosition((int) (0));
						gameComponent.gettopLHSbat().SetyPosition((int) (WINDOW_H/3));
					} 
				}
				if (input.contains("E")) {
					if ( (gameComponent.gettopLHSbat().GetyPosition() == (WINDOW_H/3 )) && (gameComponent.gettopLHSbat().GetxPosition() >= 0) && (gameComponent.gettopLHSbat().GetxPosition() <= WINDOW_H/3) ) {	//horizontal right
						gameComponent.gettopLHSbat().SetxPosition((int) (gameComponent.gettopLHSbat().GetxPosition() + increment));			
					}
					if ( (gameComponent.gettopLHSbat().GetyPosition() == (WINDOW_H/3 )) && (gameComponent.gettopLHSbat().GetxPosition() > WINDOW_H/3) ) {	//overshoots max point on horizontal movement going right
						gameComponent.gettopLHSbat().SetxPosition((int) (WINDOW_H/3));
						gameComponent.gettopLHSbat().SetyPosition((int) (WINDOW_H/3));				
					}
					if ( (gameComponent.gettopLHSbat().GetyPosition() >= 0) && (gameComponent.gettopLHSbat().GetyPosition() <= WINDOW_H/3) && (gameComponent.gettopLHSbat().GetxPosition() == WINDOW_H/3) ) {	//vertical up
						gameComponent.gettopLHSbat().SetyPosition((int) (gameComponent.gettopLHSbat().GetyPosition() - increment));
					}
					if ( (gameComponent.gettopLHSbat().GetyPosition() < 0) && (gameComponent.gettopLHSbat().GetxPosition() == WINDOW_H/3) ){ //overshoot max point on vertical up
						gameComponent.gettopLHSbat().SetxPosition((int) (WINDOW_H/3));
						gameComponent.gettopLHSbat().SetyPosition((int) (0));
					} 
				}
			}
			else{
				//If this is an AI's bat. Let the AI do the work 
				double batMaxX = 256;
				double batMaxY = (WINDOW_H / 3);
				double gradient = (gameComponent.getBall().getYVelocity()) / (gameComponent.getBall().getXVelocity());
				double C = gameComponent.getBall().GetyPosition() - (gradient * gameComponent.getBall().GetxPosition());
					
				//If the ball is heading toward the AI Horizontal area
				if ( ((batMaxY-C)/gradient) > 0 && ((batMaxY-C)/gradient) < batMaxX){
					if(((gameComponent.getBall().getYVelocity() < 0) && (gameComponent.getBall().getXVelocity() < 0))){
						gameComponent.gettopLHSbat().makeAIMoveX(((batMaxY-C)/gradient) + 32);
					}
					else if((gameComponent.getBall().getYVelocity() < 0) && (gameComponent.getBall().getXVelocity() > 0)){
						gameComponent.gettopLHSbat().makeAIMoveX(((batMaxY-C)/gradient) - 32);
					}
				}
				//determine whether the ball will travel to the AI area (y = mx+c) y path of AI valid between 0 to 255

				else if((((gradient * batMaxX) + C ) < batMaxY ) && (((gradient * batMaxX) +  C) > 0)){ 
					if(((gameComponent.getBall().getYVelocity() < 0) && (gameComponent.getBall().getXVelocity() < 0))){ // ball direction is top left, apply calibration
						gameComponent.gettopLHSbat().makeAIMoveY(((gradient * batMaxX) + C) + 32);
					}
					else if((gameComponent.getBall().getYVelocity() > 0) && (gameComponent.getBall().getXVelocity() > 0)){ // ball direction is bottom left, apply calibration
						gameComponent.gettopLHSbat().makeAIMoveY(((gradient * batMaxX) + C) - 32);
					}
				}
				
				
				double gradientGhost = (gameComponent.getGhostBall().getYVelocity()) / (gameComponent.getGhostBall().getXVelocity());
				double CGhostBall = gameComponent.getGhostBall().GetyPosition() - (gradientGhost * gameComponent.getGhostBall().GetxPosition());
					
				//If the ball is heading toward the AI Horizontal area
				if ( ((batMaxY-CGhostBall)/gradientGhost) > 0 && ((batMaxY-CGhostBall)/gradientGhost) < batMaxX){
					if(((gameComponent.getGhostBall().getYVelocity() < 0) && (gameComponent.getGhostBall().getXVelocity() < 0))){
						gameComponent.gettopLHSbat().makeAIMoveX(((batMaxY-CGhostBall)/gradientGhost) + 32);
					}
					else if((gameComponent.getGhostBall().getYVelocity() < 0) && (gameComponent.getGhostBall().getXVelocity() > 0)){
						gameComponent.gettopLHSbat().makeAIMoveX(((batMaxY-CGhostBall)/gradientGhost) - 32);
					}
				}
				//determine whether the ball will travel to the AI's area (using y = mx+c) y path of AI valid between 0 to 255
				else if((((gradientGhost * batMaxX) + CGhostBall ) < batMaxY ) && (((gradientGhost * batMaxX) +  CGhostBall) > 0)){ 
					if(((gameComponent.getGhostBall().getYVelocity() < 0) && (gameComponent.getGhostBall().getXVelocity() < 0))){ // ball direction is top left, apply calibration
						gameComponent.gettopLHSbat().makeAIMoveY(((gradientGhost * batMaxX) + CGhostBall) + 32);
					}
					else if((gameComponent.getGhostBall().getYVelocity() > 0) && (gameComponent.getGhostBall().getXVelocity() > 0)){ // ball direction is bottom left, apply calibration
						gameComponent.gettopLHSbat().makeAIMoveY(((gradientGhost * batMaxX) + CGhostBall) - 32);
					}
				}
				
			}
		}
		
		if (!bottomLHSBase.isDead()) {
			if(!gameComponent.getbottomLHSbat().isAIBat()){
				int increment = 7;
				if (input.contains("A")) {
					if ( (gameComponent.getbottomLHSbat().GetxPosition() == (WINDOW_H/3)) && (gameComponent.getbottomLHSbat().GetyPosition() <= 768) && (gameComponent.getbottomLHSbat().GetyPosition() >= 768 - WINDOW_H/3 - 32) ) {	//vertical movement up
						gameComponent.getbottomLHSbat().SetyPosition((int) (gameComponent.getbottomLHSbat().GetyPosition() - increment));	
					}
					if ( (gameComponent.getbottomLHSbat().GetxPosition() == (WINDOW_H/3 )) && (gameComponent.getbottomLHSbat().GetyPosition() < (768 - WINDOW_H/3 - 32)) ) {	//if it overshoots max point on vertical movement going up
						gameComponent.getbottomLHSbat().SetxPosition((int) (WINDOW_H/3));
						gameComponent.getbottomLHSbat().SetyPosition((int) (768 - WINDOW_H/3 - 32));	
					}
					if ( (gameComponent.getbottomLHSbat().GetxPosition() >= 0) && (gameComponent.getbottomLHSbat().GetxPosition() <= WINDOW_H/3) && (gameComponent.getbottomLHSbat().GetyPosition() == 768 - WINDOW_H/3 - 32) ) {//horizontal movement left
						gameComponent.getbottomLHSbat().SetxPosition((int) (gameComponent.getbottomLHSbat().GetxPosition() - increment));
					}
					if ( (gameComponent.getbottomLHSbat().GetxPosition() < 0) && (gameComponent.getbottomLHSbat().GetyPosition() == 768 - WINDOW_H/3 - 32) ){ //overshoot max pt on horizontal going left
						gameComponent.getbottomLHSbat().SetxPosition((int) (0));
						gameComponent.getbottomLHSbat().SetyPosition((int) (768 - WINDOW_H/3 - 32));
					} 
				}
				if (input.contains("D")) {
					if ( (gameComponent.getbottomLHSbat().GetyPosition() == (768 - WINDOW_H/3 - 32 )) && (gameComponent.getbottomLHSbat().GetxPosition() >= 0) && (gameComponent.getbottomLHSbat().GetxPosition() <= WINDOW_H/3) ) {	//horizontal move right
						gameComponent.getbottomLHSbat().SetxPosition((int) (gameComponent.getbottomLHSbat().GetxPosition() + increment));			
					}
					if ( (gameComponent.getbottomLHSbat().GetyPosition() == (768 - WINDOW_H/3 - 32)) && (gameComponent.getbottomLHSbat().GetxPosition() > WINDOW_H/3) ) {	//if it overshoots max point on horizontal movement going right
						gameComponent.getbottomLHSbat().SetxPosition((int) (WINDOW_H/3));
						gameComponent.getbottomLHSbat().SetyPosition((int) (768-WINDOW_H/3 - 32));				
					}
					if ( (gameComponent.getbottomLHSbat().GetyPosition() >= 768 - WINDOW_H/3 - 32) && (gameComponent.getbottomLHSbat().GetyPosition() <= 768 - 32) && (gameComponent.getbottomLHSbat().GetxPosition() == WINDOW_H/3) ) {	//vertical move down
						gameComponent.getbottomLHSbat().SetyPosition((int) (gameComponent.getbottomLHSbat().GetyPosition() + increment));
					}
					if ( (gameComponent.getbottomLHSbat().GetyPosition() > 768) && (gameComponent.getbottomLHSbat().GetxPosition() == WINDOW_H/3) ){ //overshoot max pt on vertical going down
						gameComponent.getbottomLHSbat().SetxPosition((int) (WINDOW_H/3));
						gameComponent.getbottomLHSbat().SetyPosition((int) (768 - 32));
					} 
				}
			}
			else{
				//If this is an AI controlled bat: (This bat does't track ghost Ball's path)
				double batMaxX = 256;
				double batMinY = (WINDOW_H - (WINDOW_H/3) - 32);
				double gradient = (gameComponent.getBall().getYVelocity()) / (gameComponent.getBall().getXVelocity());
				double C = gameComponent.getBall().GetyPosition() - (gradient * gameComponent.getBall().GetxPosition());
					
				//If the ball is heading toward the AI Horizontal area
				if ( ((batMinY-C)/gradient) > 0 && ((batMinY-C)/gradient) < batMaxX){
					if(((gameComponent.getBall().getYVelocity() > 0) && (gameComponent.getBall().getXVelocity() < 0))){
						gameComponent.getbottomLHSbat().makeAIMoveX(((batMinY-C)/gradient) - 32);
					}
					else if((gameComponent.getBall().getYVelocity() > 0) && (gameComponent.getBall().getXVelocity() > 0)){
						gameComponent.getbottomLHSbat().makeAIMoveX(((batMinY-C)/gradient) + 32);
					}
				}
				
				//determine whether the ball will travel to the AI vertical area (y = mx+c)
				else if((((gradient * batMaxX) + C ) > batMinY ) && (((gradient * batMaxX) +  C) < WINDOW_H)){ 
					if(((gameComponent.getBall().getYVelocity() > 0) && (gameComponent.getBall().getXVelocity() < 0))){ //
						gameComponent.getbottomLHSbat().makeAIMoveY(((gradient * batMaxX) + C) - 32);
					}
					else if((gameComponent.getBall().getYVelocity() < 0) && (gameComponent.getBall().getXVelocity() < 0)){ // 
						gameComponent.getbottomLHSbat().makeAIMoveY(((gradient * batMaxX) + C) + 32);
					}
				}	
			}
		}
		
		if (!topRHSBase.isDead()) {
			if(!gameComponent.gettopRHSbat().isAIBat()){
				int increment = 7;
				if (input.contains("NUMPAD7")) {
					if ( (gameComponent.gettopRHSbat().GetxPosition() == (1024 - WINDOW_H/3 - 32)) && (gameComponent.gettopRHSbat().GetyPosition() >= 0) && (gameComponent.gettopRHSbat().GetyPosition() <= WINDOW_H/3) ) {		//vertical up
						gameComponent.gettopRHSbat().SetyPosition((int) (gameComponent.gettopRHSbat().GetyPosition() - increment));		
					}
					if ( (gameComponent.gettopRHSbat().GetxPosition() == (1024 - WINDOW_H/3 - 32)) && (gameComponent.gettopRHSbat().GetyPosition() < 0) ) {	//if it overshoots max point on vertical up
						gameComponent.gettopRHSbat().SetxPosition((int) (1024 - WINDOW_H/3 - 32));
						gameComponent.gettopRHSbat().SetyPosition((int) (0));		
					}
					if ( (gameComponent.gettopRHSbat().GetxPosition() >= 1024 - WINDOW_H/3 - 32) && (gameComponent.gettopRHSbat().GetxPosition() <= 1024 - 32) && (gameComponent.gettopRHSbat().GetyPosition() == WINDOW_H/3) ) {	//horizontal left
						gameComponent.gettopRHSbat().SetxPosition((int) (gameComponent.gettopRHSbat().GetxPosition() - increment));
					}
					if ( (gameComponent.gettopRHSbat().GetxPosition() < 1024 - WINDOW_H/3 - 32) && (gameComponent.gettopRHSbat().GetyPosition() == WINDOW_H/3) ){ //overshoot max pt on horizontal left 
						gameComponent.gettopRHSbat().SetxPosition((int) (1024 - WINDOW_H/3 - 32));
						gameComponent.gettopRHSbat().SetyPosition((int) (WINDOW_H/3));
					} 
				}
				if (input.contains("NUMPAD9")) {
					if ( (gameComponent.gettopRHSbat().GetyPosition() == (WINDOW_H/3)) && (gameComponent.gettopRHSbat().GetxPosition() >= 1024 - WINDOW_H/3 - 32) && (gameComponent.gettopRHSbat().GetxPosition() <= 1024 - 32) ) {	//horizontal right
						gameComponent.gettopRHSbat().SetxPosition((int) (gameComponent.gettopRHSbat().GetxPosition() + increment));			
					}
					if ( (gameComponent.gettopRHSbat().GetyPosition() == (WINDOW_H/3 )) && (gameComponent.gettopRHSbat().GetxPosition() > 1024 - 32) ) {	//if it overshoots max point on horizontal right
						gameComponent.gettopRHSbat().SetxPosition((int) (1024 - 32));
						gameComponent.gettopRHSbat().SetyPosition((int) (WINDOW_H/3));				
					}
					if ( (gameComponent.gettopRHSbat().GetyPosition() >= 0) && (gameComponent.gettopRHSbat().GetyPosition() <= WINDOW_H/3) && (gameComponent.gettopRHSbat().GetxPosition() == 1024 - WINDOW_H/3 - 32) ) {	//vertical down
						gameComponent.gettopRHSbat().SetyPosition((int) (gameComponent.gettopRHSbat().GetyPosition() + increment));
					}
					if ( (gameComponent.gettopRHSbat().GetyPosition() > WINDOW_H/3) && (gameComponent.gettopRHSbat().GetxPosition() == 1024 - WINDOW_H/3 - 32) ){ //overshoot max pt on vertical down
						gameComponent.gettopRHSbat().SetxPosition((int) (1024 - WINDOW_H/3 - 32));
						gameComponent.gettopRHSbat().SetyPosition((int) (WINDOW_H/3));
					} 
				}
			}
			//If this paddle is AI controlled!
			else{
				double batMinX = (WINDOW_W - (WINDOW_H/3) - 32);
				double batMaxY = (WINDOW_H / 3);
				double gradient = (gameComponent.getBall().getYVelocity()) / (gameComponent.getBall().getXVelocity());
				double C = gameComponent.getBall().GetyPosition() - (gradient * gameComponent.getBall().GetxPosition());
				
				//determine whether the ball will travel to the AI area (y = mx+c) y path of AI valid between 0 to 255
				//736 is the AI x Position when it can only move vertically		
				if((((gradient * batMinX) + C ) < batMaxY ) && (((gradient * batMinX) +  C) > 0)){ 
					if(((gameComponent.getBall().getYVelocity() < 0) && (gameComponent.getBall().getXVelocity() > 0))){ // ball traveling to top right
						gameComponent.gettopRHSbat().makeAIMoveY(((gradient * 736) + C) + 30);
					}
					else if((gameComponent.getBall().getYVelocity() > 0) && (gameComponent.getBall().getXVelocity() > 0)){ // ball traveling to bottom right, apply calibration
						gameComponent.gettopRHSbat().makeAIMoveY(((gradient * 736) + C) - 30);
					}
				}	
				//If the ball is heading toward the AI Horizontal area
				else if ( ((batMaxY-C)/gradient) > batMinX && ((batMaxY-C)/gradient) < WINDOW_W){
					if(((gameComponent.getBall().getYVelocity() < 0) && (gameComponent.getBall().getXVelocity() > 0))){
						gameComponent.gettopRHSbat().makeAIMoveX(((batMaxY-C)/gradient) +32);
					}
					else if((gameComponent.getBall().getYVelocity() < 0) && (gameComponent.getBall().getXVelocity() < 0)){
						gameComponent.gettopRHSbat().makeAIMoveX(((batMaxY-C)/gradient) +32);
					}
				}
				
				double ghostGradient = (gameComponent.getGhostBall().getYVelocity()) / (gameComponent.getGhostBall().getXVelocity());
				double ghostC = gameComponent.getGhostBall().GetyPosition() - (ghostGradient * gameComponent.getGhostBall().GetxPosition());
				
				//determine whether the ball will travel to the AI area (y = mx+c) y path of AI valid between 0 to 255
				//736 is the AI x Position when it can only move vertically		
				if((((ghostGradient * batMinX) + ghostC ) < batMaxY ) && (((ghostGradient * batMinX) +  ghostC) > 0)){ 
					if(((gameComponent.getGhostBall().getYVelocity() < 0) && (gameComponent.getGhostBall().getXVelocity() > 0))){ // ball traveling to top right
						gameComponent.gettopRHSbat().makeAIMoveY(((ghostGradient * 736) + ghostC) + 30);
					}
					else if((gameComponent.getGhostBall().getYVelocity() > 0) && (gameComponent.getGhostBall().getXVelocity() > 0)){ // ball traveling to bottom right, apply calibration
						gameComponent.gettopRHSbat().makeAIMoveY(((ghostGradient * 736) + ghostC) - 30);
					}
				}	
				//If the ball is heading toward the AI Horizontal area
				else if ( ((batMaxY-ghostC)/ghostGradient) > batMinX && ((batMaxY-ghostC)/ghostGradient) < WINDOW_W){
					if(((gameComponent.getGhostBall().getYVelocity() < 0) && (gameComponent.getGhostBall().getXVelocity() > 0))){
						gameComponent.gettopRHSbat().makeAIMoveX(((batMaxY-ghostC)/ghostGradient) +32);
					}
					else if((gameComponent.getGhostBall().getYVelocity() < 0) && (gameComponent.getGhostBall().getXVelocity() < 0)){
						gameComponent.gettopRHSbat().makeAIMoveX(((batMaxY-ghostC)/ghostGradient) +32);
					}
				}
			}
		}
		
		if (!bottomRHSBase.isDead()) {		
			//This base will always be player's controlled
			int increment = 7;
			if (input.contains("LEFT")) {
				if ( (gameComponent.getbottomRHSbat().GetxPosition() == (1024 - WINDOW_H/3 - 32)) && (gameComponent.getbottomRHSbat().GetyPosition() >= 768 - WINDOW_H/3 - 32 ) && (gameComponent.getbottomRHSbat().GetyPosition() <= 768 - 32) ) {		//vertical down
					gameComponent.getbottomRHSbat().SetyPosition((int) (gameComponent.getbottomRHSbat().GetyPosition() + increment));		
				}
				if ( (gameComponent.getbottomRHSbat().GetxPosition() == (1024 - WINDOW_H/3 - 32)) && (gameComponent.getbottomRHSbat().GetyPosition() > 768 - 32) ) {	//if it overshoots max point on vertical down
					gameComponent.getbottomRHSbat().SetxPosition((int) (1024 - WINDOW_H/3 - 32));
					gameComponent.getbottomRHSbat().SetyPosition((int) (768 - 32));		
				}
				if ( (gameComponent.getbottomRHSbat().GetxPosition() >= 1024 - WINDOW_H/3 - 32) && (gameComponent.getbottomRHSbat().GetxPosition() <= 1024 - 32) && (gameComponent.getbottomRHSbat().GetyPosition() == 768 - WINDOW_H/3 - 32) ) {	//horizontal left
					gameComponent.getbottomRHSbat().SetxPosition((int) (gameComponent.getbottomRHSbat().GetxPosition() - increment));
				}
				if ( (gameComponent.getbottomRHSbat().GetxPosition() < 1024 - WINDOW_H/3 - 32) && (gameComponent.getbottomRHSbat().GetyPosition() == 768 - WINDOW_H/3 - 32) ){ //overshoot max pt on horizontal left 
					gameComponent.getbottomRHSbat().SetxPosition((int) (1024 - WINDOW_H/3 - 32));
					gameComponent.getbottomRHSbat().SetyPosition((int) (768 - WINDOW_H/3 - 32));
				} 
			}
			if (input.contains("RIGHT")) {
				if ( (gameComponent.getbottomRHSbat().GetyPosition() == (768 - WINDOW_H/3 - 32)) && (gameComponent.getbottomRHSbat().GetxPosition() >= 1024 - WINDOW_H/3 - 32) && (gameComponent.getbottomRHSbat().GetxPosition() <= 1024 - 32) ) {	//horizontal right
					gameComponent.getbottomRHSbat().SetxPosition((int) (gameComponent.getbottomRHSbat().GetxPosition() + increment));			
				}
				if ( (gameComponent.getbottomRHSbat().GetyPosition() == (768 - WINDOW_H/3 - 32 )) && (gameComponent.getbottomRHSbat().GetxPosition() > 1024 - 32) ) {	//if it overshoots max point on horizontal right
					gameComponent.getbottomRHSbat().SetxPosition((int) (1024 - 32));
					gameComponent.getbottomRHSbat().SetyPosition((int) (768 - WINDOW_H/3 - 32));				
				}
				if ( (gameComponent.getbottomRHSbat().GetyPosition() >= 768 - WINDOW_H/3 - 32) && (gameComponent.getbottomRHSbat().GetyPosition() <= 768 - 32) && (gameComponent.getbottomRHSbat().GetxPosition() == 1024 - WINDOW_H/3 - 32) ) {	//vertical up
					gameComponent.getbottomRHSbat().SetyPosition((int) (gameComponent.getbottomRHSbat().GetyPosition() - increment));
				}
				if ( (gameComponent.getbottomRHSbat().GetyPosition() < 768 - WINDOW_H/3 - 32) && (gameComponent.getbottomRHSbat().GetxPosition() == 1024 - WINDOW_H/3 - 32) ){ //overshoot max pt on vertical up
					gameComponent.getbottomRHSbat().SetxPosition((int) (1024 - WINDOW_H/3 - 32));
					gameComponent.getbottomRHSbat().SetyPosition((int) (768 - WINDOW_H/3 - 32));
				} 
			}
		}
		
		// TODO: DEBUG ONLY, remove when deliver
		if (inGameBallSpeedAdjust) {
			if (input.contains("DOWN")) { // slow down
				gameComponent.getBall().setXVelocity(((float) (gameComponent.getBall().getXVelocity() / 1.1)));
				gameComponent.getBall().setYVelocity(((float) (gameComponent.getBall().getYVelocity() / 1.1)));
			} else if (input.contains("UP")) { // go faster!
				gameComponent.getBall().setXVelocity((float) (gameComponent.getBall().getXVelocity() * 1.1));
				gameComponent.getBall().setYVelocity((float) (gameComponent.getBall().getYVelocity() * 1.1));
			}
		}

		if (input.contains("PAGE_DOWN")) {
			this.setTimeRemainingToTwo();
		}
	}

	public void paddleCollisionCheck() {

		if (!topLHSBase.isDead()) {
			if (gameComponent.getBall().objectsIntersectBallAndPaddle(gameComponent.gettopLHSbat())) {
				gameComponent.getBall().setXVelocity(-gameComponent.getBall().getXVelocity());
				playPaddleDeflectSound();
			}
		}
		if (!bottomLHSBase.isDead()) {
			if (gameComponent.getBall().objectsIntersectBallAndPaddle(gameComponent.getbottomLHSbat())) {
				gameComponent.getBall().setXVelocity(-gameComponent.getBall().getXVelocity());
				playPaddleDeflectSound();
			}	
		}
		if (!topRHSBase.isDead()) {
			if (gameComponent.getBall().objectsIntersectBallAndPaddle(gameComponent.gettopRHSbat())) {
				gameComponent.getBall().setXVelocity(-gameComponent.getBall().getXVelocity());
				playPaddleDeflectSound();
			}
		}
		if (!bottomRHSBase.isDead()) {
			if (gameComponent.getBall().objectsIntersectBallAndPaddle(gameComponent.getbottomRHSbat())) {
				gameComponent.getBall().setXVelocity(-gameComponent.getBall().getXVelocity());
				playPaddleDeflectSound();
			}	
		}
	}

	public void baseCollisionCheck() {
		if (!topLHSBase.isDead()) {
			if (gameComponent.getBall().objectsIntersectBallAndBase(topLHSBase)) {
				topLHSBase.setIsDead(true);
				playBaseHitSound();
				System.out.println("Blue");
			}
		}
		if (!topRHSBase.isDead()) {
			if (gameComponent.getBall().objectsIntersectBallAndBase(topRHSBase)) {
				topRHSBase.setIsDead(true);
				playBaseHitSound();
				System.out.println("Green");
			}
		}

		if (!bottomRHSBase.isDead()) {
			if (gameComponent.getBall().objectsIntersectBallAndBase(bottomRHSBase)) {
				bottomRHSBase.setIsDead(true);
				playBaseHitSound();
				System.out.println("Red");
			}
		}

		if (!bottomLHSBase.isDead()) {
			if (gameComponent.getBall().objectsIntersectBallAndBase(bottomLHSBase)) {
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
			if (gameComponent.getBall().objectsIntersect(brick)) {
				deflect.deflectTheBall(gameComponent.getBall(), brick.getArrangement());
				topRHSBrickList.remove();
				this.gameBrick.getTopRHSBrick().removeBrick();
				playWallHitSound();
				System.out.println("1 Brick destroyed, " + this.gameBrick.getTopRHSBrick().getNumberOfBrick() + " left: (TopRHS)");
			}
		}
		while (topLHSBrickList.hasNext()) {
			Brick brick = topLHSBrickList.next();
			if (gameComponent.getBall().objectsIntersect(brick)) {
				deflect.deflectTheBall(gameComponent.getBall(), brick.getArrangement());
				topLHSBrickList.remove();
				this.gameBrick.getTopLHSBrick().removeBrick();
				playWallHitSound();
				System.out.println("1 Brick destroyed, " + this.gameBrick.getTopLHSBrick().getNumberOfBrick() + " left: (TopLHS)");

			}
		}
		while (bottomLHSbrickList.hasNext()) {
			Brick brick = bottomLHSbrickList.next();
			if (gameComponent.getBall().objectsIntersect(brick)) {
				deflect.deflectTheBall(gameComponent.getBall(), brick.getArrangement());
				bottomLHSbrickList.remove();
				this.gameBrick.getBottomLHSBrick().removeBrick();
				playWallHitSound();
				System.out.println("1 Brick destroyed, " + this.gameBrick.getBottomLHSBrick().getNumberOfBrick() + " left: (BottomLHS)");

			}
		}
		while (bottomRHSBrickList.hasNext()) {
			Brick brick = bottomRHSBrickList.next();
			if (gameComponent.getBall().objectsIntersect(brick)) {
				deflect.deflectTheBall(gameComponent.getBall(), brick.getArrangement());
				bottomRHSBrickList.remove();
				this.gameBrick.getBottomRHSBrick().removeBrick();
				playWallHitSound();
				System.out.println("1 Brick destroyed, " + this.gameBrick.getBottomRHSBrick().getNumberOfBrick() + " left: (BottomRHS)");

			}
		}
		deflect.setTempDir(99, 99);
	}

	public boolean isFinished() {
		return false;
	}

	public void setTimeRemainingToTwo() {
		timer120sec.setTime(2);
	}

	private void playPaddleDeflectSound() {
		gameComponent.getpaddleDeflectSound().playSound();
	}

	private void playWallHitSound() {
		gameComponent.getwallHitSound().playSound();
	}

	private void playBaseHitSound() {
		gameComponent.getbaseHitSound().playSound();
	}
	
	private void playCountdownVoice() {
		gameComponent.getcountdownVoice().playSound();
	}
	
	private void playCountdownSound() {
		gameComponent.getcountdownSound().playSound();
	}
	
	private void playWinSound() {
		gameComponent.getwinJingleSound().playSound();
	}
	
	private void playLoseSound() {
		gameComponent.getloseJingleSound().playSound();
	}
}
