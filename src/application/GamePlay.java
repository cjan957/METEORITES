package application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

import com.ttcj.components.Ball.basePosition;
import com.ttcj.components.Base;
import com.ttcj.components.Brick;
import com.ttcj.components.Powerup;
import com.ttcj.components.Timer;
import com.ttcj.view.GameView;

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
	private boolean forceDeadkey = true;
	
	// Defining constants
	private static final int WINDOW_W = 1024;
	private static final int WINDOW_H = 768;
	private static final float speedIncrementOverTime = 0.013f;
	private static final int paddleSpeed = 7;
	private static int powerupTime_Time = ThreadLocalRandom.current().nextInt(15, 40 + 1);
	private static int powerupTime_Frozen = ThreadLocalRandom.current().nextInt(60, 110 + 1);
	
	private int startPowerup;
	
	private boolean freezeThePaddle = false;

	// Instantiate game objects
	private Timer timer3sec;
	private Timer timer120sec;	
	private ComponentManager gameComponent;
	private Base topLHSBase, bottomLHSBase, topRHSBase, bottomRHSBase;
	private BrickBuilder gameBrick;
	private Deflect deflect;

	private Stage gameWindow;
	private GameView view;
	
	//State game mode and whether the game is still going
	private boolean playing = true;
	
	public enum gameMode{
		SINGLE, MULTI
	}
	public gameMode currentGameMode;

	//messages controller
	private boolean longMessage;
	private String message;
	
	// This array list will store user input (key pressed).
	private ArrayList<String> input = new ArrayList<String>();
	
	/*
	 * -------------------------------------- GAMEPLAY CONST--------------------------------------
	 */
	public GamePlay(int gameModeNum, Stage gameStage){
			view = new GameView(); //create the gameView 
			gameWindow = gameStage;
			if(gameModeNum == 0){ //game mode 0 = single
				currentGameMode = gameMode.SINGLE;
			}
			else{ //game mode 1 = multi-player
				currentGameMode = gameMode.MULTI;
			}
			startGame();
	}	
	
	/*
	 * --------------------------------------TIMER RELEVANT METHODS--------------------------------------
	 */
	public void startThreeSecondsTimer(){
		//Initilse timer objects
		timer3sec = new Timer(3, true);
		timer120sec = new Timer(120, false);
		gameComponent.getcountdownVoice().playSound();
		
		// Counting down from 3 to 0, decrement the timer for 1 sec every one
		// second.
		Timeline renderKeyFrame = new Timeline(new KeyFrame(Duration.seconds(1), timeout -> {
			timer3sec.countDown();
		}));
		renderKeyFrame.setCycleCount(3); // repeat 1 sec for 3 times
		renderKeyFrame.play();
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
	
	public void startCountDowntimeUp() {
		timer3sec.setTimeOut(true);
		startTheGame();
		
		//Notify the view that the 3s counter has finished
		view.setCountDown3IsDone();
		
		//Hide the 3s timer and show 120s timers on the screen.
		timer3sec.setVisibility(false);
		timer120sec.setVisibility(true);
	}

	public void checkActualTimeRemaining() {
		//If the game has finished (timer <= 0), check for winner. Otherwise,
		//the game is still going so check if there's any in-game winner.
		if (timer120sec.getTime() <= 0) {
			timeOutFindWinner();
		} else {
			//Display powerup when the initialised time for each powerup has reached  
			if(timer120sec.getTime() == powerupTime_Time){
				this.gameComponent.getPowerupTime().setVisibility(true);
			}
			else if(timer120sec.getTime() == powerupTime_Frozen){
				this.gameComponent.getPowerupFrozen().setVisibility(true);
			}
			timeLeftFindWinner();
		}
	}
	
	//Set the time remaining determined by the powerup
	public void setTimeRemaining(int time){
		if(timer120sec.getTime() < 12){
			timer120sec.setTime(1);
		}
		else{
			timer120sec.setTime(timer120sec.getTime() + time);
		}
	}
	
	public void setTimeRemainingToTwo() {
		timer120sec.setTime(2);
	}
	
	/*
	 * --------------------------------------GAME PLAY METHODS--------------------------------------
	 */
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
			gameComponent.getcountdownSound().playSound();
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
	
	
	/*
	 * --------------------------------------GAME INITIALISERS --------------------------------------
	 */
	
	public void gameInit() {	
		//Create game components in gameComponent class, what type of objects
		//will be created depends on the game mode
		gameComponent = new ComponentManager(currentGameMode);
		
		//Setup the base
		baseInit();	
		
		//Construct Bricks
		gameBrick = new BrickBuilder();
		deflect = new Deflect();
	}

	public void baseInit() {
		// Base name, Image, xPos, yPos, width and height of base
		topLHSBase = new Base("Blue", "planet1_64.png", 0, 0, 64, 64);
		topRHSBase = new Base("Green", "planet2_64.png", WINDOW_W - 64, 0, 64, 64);
		bottomLHSBase = new Base("Yellow", "planet3_64.png", 0, WINDOW_H - 64, 64, 64);
		bottomRHSBase = new Base("Red", "planet4_64.png", WINDOW_W - 64, WINDOW_H - 64, 64, 64);
	}
	
	
	/*
	 * --------------------------------------GAME CONTROLS--------------------------------------
	 */

	public void stopTheGame(){
		playing = false;
		view.setGameFinished();
	}
	
	public void startTheGame(){
		playing = true;
	}
	
	
	/*
	 * --------------------------------------FIND WINNERS IN GAME--------------------------------------
	 */
	
	public void timeLeftFindWinner() {
		if(currentGameMode == gameMode.MULTI){
			//Check whether there's only 1 player left in the game
			if (!topLHSBase.isDead() && topRHSBase.isDead() && bottomLHSBase.isDead() && bottomRHSBase.isDead()) {
				topLHSBase.setIsWinner(true);
				message = "Winner: "+topLHSBase.getBaseName();	
				longMessage = false;
				gameComponent.getloseJingleSound().playSound();
				stopTheGame();
	
			} else if (topLHSBase.isDead() && !topRHSBase.isDead() && bottomLHSBase.isDead() && bottomRHSBase.isDead()) {
				topRHSBase.setIsWinner(true);
				longMessage = false;
				message = "Winner: "+topRHSBase.getBaseName();
				gameComponent.getloseJingleSound().playSound();
				stopTheGame();
	
			} else if (topLHSBase.isDead() && topRHSBase.isDead() && !bottomLHSBase.isDead() && bottomRHSBase.isDead()) {
				bottomLHSBase.setIsWinner(true);
				longMessage = false;
				message = "Winner: "+bottomLHSBase.getBaseName();
				gameComponent.getwinJingleSound().playSound();
				stopTheGame();
	
			} else if (topLHSBase.isDead() && topRHSBase.isDead() && bottomLHSBase.isDead() && !bottomRHSBase.isDead()) {
				bottomRHSBase.setIsWinner(true);
				longMessage = false;
				message = "Winner: "+bottomRHSBase.getBaseName();
				gameComponent.getwinJingleSound().playSound();
				stopTheGame();
			}
			else if(bottomLHSBase.isDead() && bottomRHSBase.isDead()){
				longMessage = true;
				message = "Winner: AI";
				System.out.println("AIs Win");
				gameComponent.getloseJingleSound().playSound();
				stopTheGame();
			}
		}
		else{ //Must be single player mode
			//when all other players are dead, and you are the only one left: You win
			if (topLHSBase.isDead() && topRHSBase.isDead() && bottomLHSBase.isDead() && !bottomRHSBase.isDead()){
				bottomRHSBase.isWinner();
				message = "You Win";	
				longMessage = false;
				gameComponent.getwinJingleSound().playSound();
				stopTheGame(); 	
			}
			else if (bottomRHSBase.isDead()){
				message = "You Lose";
				longMessage = false;
				gameComponent.getloseJingleSound().playSound();
				stopTheGame();
			}
		}
	}

	/*
	 * --------------------------------------FIND WINNER (GAMEOVER)--------------------------------------
	 */
	
	public void timeOutFindWinner() {

		ArrayList<Integer> scoreList = new ArrayList<Integer>();
		ArrayList<Base> baseList = new ArrayList<Base>();

		//Store players and their scores (number of bricks left) into arrays if they are not dead
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

		//Sort to find who has the most number of bricks left
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
		
		//Index 0, is the winner
		baseList.get(0).setIsWinner(true);
		String winnerName = baseList.get(0).getBaseName();
		//System.out.println("Winner: " + baseList.get(0).getBaseName());
		
		//Now find whether other players have the same number of bricks left as the winner.
		//(Determine whether the game is a draw.)
		for(int i = 1; i < arraySize; i++){
			if(scoreList.get(i) == scoreList.get(0)){
				numberOfTies++;
				baseList.get(i).setIsWinner(true);
				winnerName = winnerName + ", "+ baseList.get(i).getBaseName();
			}
		}
		
		//If there's a tie, prepare string to display their names
		if(numberOfTies > 0){
			message = "Tie: " + winnerName;
			longMessage = true;
			if(currentGameMode == gameMode.SINGLE){
				//Play appropriate sound when in single player mode.
				if(winnerName.contains(bottomRHSBase.getBaseName())){
					gameComponent.getwinJingleSound().playSound();;
				} else {
					gameComponent.getloseJingleSound().playSound();
				}
			} else if (currentGameMode == gameMode.MULTI){
				//Play appropriate sound when in 2 player mode
				if(winnerName.contains(bottomRHSBase.getBaseName()) || winnerName.contains(bottomLHSBase.getBaseName())){
					gameComponent.getwinJingleSound().playSound();
				} else {
					gameComponent.getloseJingleSound().playSound();
				}
			}
		}
		else{
			//If there's only one winner, save the player's name to the string, and play 
			//appropriate sound
			message = "Winner: " + winnerName;
			longMessage = false;
			if(currentGameMode == gameMode.SINGLE){
				if(winnerName == bottomRHSBase.getBaseName()){
					gameComponent.getwinJingleSound().playSound();
				} else {
					gameComponent.getloseJingleSound().playSound();
				}
			} else if (currentGameMode == gameMode.MULTI){
				//Play appropriate sound when in 2 player mode
				if(winnerName == bottomRHSBase.getBaseName() || winnerName == bottomLHSBase.getBaseName()){
					gameComponent.getwinJingleSound().playSound();
				} else {
					gameComponent.getloseJingleSound().playSound();
				}
			}
		}
		stopTheGame();
	}


/*
 * --------------------------------------RENDERING--------------------------------------
 */
	
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
		
		if(gameComponent.getPowerupTime().isVisible()){
			gameComponent.getPowerupTime().render(view.canvasGC());
		}
		if(gameComponent.getPowerupFrozen().isVisible()){
			gameComponent.getPowerupFrozen().render(view.canvasGC());
		}
		
		//BAT
		gameComponent.gettopLHSbat().render(view.canvasGC());
		gameComponent.getbottomLHSbat().render(view.canvasGC());
		gameComponent.gettopRHSbat().render(view.canvasGC());
		gameComponent.getbottomRHSbat().render(view.canvasGC());
		
		//BASE
		// If the player is still alive, render the base , otherwise do not render.
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

		//Brick 
		for (Brick brick : this.gameBrick.getTopLHSBrick().accessBrickArray()) {
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
			//Render pause message on the screen
			Font theFont = Font.font("Arial", FontWeight.BOLD, 50);
			renderSpecificText(Color.YELLOW,Color.RED,2,theFont,TextAlignment.CENTER,"PAUSED",1024/2,(768/2)-50);
		}
		
		if(view.confirmationShown()){
			//Render instruction
			Font theFont = Font.font("Arial", FontWeight.BOLD, 30);
			renderSpecificText(Color.YELLOW,Color.YELLOW,1,theFont,TextAlignment.CENTER,"Press Y to Exit. ESC to Resume", 1024/2,(768/2)+10);
		}
		
		//Display a message on the screen after the game has finished. 
		//What will be display here depends on what's in 'message' variable
		if(!playing){
			//If the message is not too long, display it with a big font
			if(!longMessage){
				Font theFont = Font.font("Arial", FontWeight.BOLD, 72);
				Font theSubFont = Font.font("Arial", FontWeight.BOLD, 20);
				renderSpecificText(Color.WHITE,Color.BLACK,2,theFont,TextAlignment.CENTER,message,1024/2,768/2);
				renderSpecificText(Color.WHITE,Color.WHITE,1,theSubFont,TextAlignment.CENTER,"Press ESC to return to the main menu", 1024/2, (768/2)+100);			
			}
			else{
				Font theFont = Font.font("Arial", FontWeight.BOLD, 43);
				Font theSubFont = Font.font("Arial", FontWeight.BOLD, 20);
				renderSpecificText(Color.WHITE,Color.BLACK,2,theFont,TextAlignment.CENTER,message,1024/2,768/2);
				renderSpecificText(Color.WHITE,Color.WHITE,1,theSubFont,TextAlignment.CENTER,"Press ESC to return to the main menu",1024/2,(768/2)+80);
			}
		}
	}
	
	public void renderSpecificText(Color color, Color strokeColor, int lineWidth, Font theFont, TextAlignment align, String message, int posX, int posY){
		//Render text
		view.canvasGC().setFill(color);
		view.canvasGC().setStroke(strokeColor);
		view.canvasGC().setLineWidth(lineWidth);
		view.canvasGC().setFont(theFont);
		view.canvasGC().setTextAlign(align);
		view.canvasGC().fillText(message, posX, posY);
		view.canvasGC().strokeText(message, posX, posY);
	}
	
	
/*
 * --------------------------------------POWERUPS--------------------------------------
 */
	
	//Check if the ball collides with the Frozen powerup on the screen
	public void powerupFrozenCollisionCheck(){
		if (gameComponent.getBall().objectsIntersectBallAndPowerup(gameComponent.getPowerupFrozen())) {
				freezeThePaddle = true;
				gameComponent.getPowerupFrozen().setVisibility(false);
				startPowerup = timer120sec.getTime(); //get the time the powerup was collected
		}
	}
	
	//Check if the ball collides with the time powerup
	public void powerupTimeCollisionCheck(){
		if(gameComponent.getBall().objectsIntersectBallAndPowerup(gameComponent.getPowerupTime())){
			setTimeRemaining(-10);
			gameComponent.getPowerupTime().setVisibility(false);
		}
	}
	
	public void frozenBatUnfreeze(){
		//Check if it has already been 5 seconds since the frozen power up was collected
		if(timer120sec.getTime() <= (startPowerup - 5)){
			freezeThePaddle = false;
		}
	}
	
	/*
	 * --------------------------------------RUN A GAME FRAME--------------------------------------
	 */

	// Tick, run the game by 1 frame
	public void tick() {

		// Move the ball once, checking necessary conditions.
		gameComponent.getBall().moveThatBall();
		gameComponent.getGhostBall().moveThatBall();
		paddleCollisionCheck();
		wallCollisionCheck();
		baseCollisionCheck();
		frozenBatUnfreeze();
		
		//If the powerups are visible on the screen, check for the collision
		//with the ball
		if(gameComponent.getPowerupFrozen().isVisible()){
			powerupFrozenCollisionCheck();
		}
		if(gameComponent.getPowerupTime().isVisible()){
			powerupTimeCollisionCheck();
		}
		
		//TODO: Debugging option
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

		if(!freezeThePaddle){
			//This bat will always be AI controlled
			if(gameComponent.gettopLHSbat().isAIBat()){
				//Using a line equation y = mx + c, determine whether the ball is traveling toward the AI base
				double batMaxX = 256;
				double batMaxY = (WINDOW_H / 3);
				double gradient = (gameComponent.getBall().getYVelocity()) / (gameComponent.getBall().getXVelocity());
				double C = gameComponent.getBall().GetyPosition() - (gradient * gameComponent.getBall().GetxPosition());
				
				double gradientGhost = (gameComponent.getGhostBall().getYVelocity()) / (gameComponent.getGhostBall().getXVelocity());
				double CGhostBall = gameComponent.getGhostBall().GetyPosition() - (gradientGhost * gameComponent.getGhostBall().GetxPosition());
					
				//GHOST BALL
				//If the ball is heading toward the AI Horizontal area
				if ( ((batMaxY-CGhostBall)/gradientGhost) > 0 && ((batMaxY-CGhostBall)/gradientGhost) < batMaxX){
					if(((gameComponent.getGhostBall().getYVelocity() < 0) && (gameComponent.getGhostBall().getXVelocity() < 0))){
						//If it does, move the AI paddle to protect its base.
						gameComponent.gettopLHSbat().makeAIMoveX(((batMaxY-CGhostBall)/gradientGhost) - 32);
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
				

				//If the ball is heading toward the AI Horizontal area
				if ( ((batMaxY-C)/gradient) > 0 && ((batMaxY-C)/gradient) < batMaxX){
					if(((gameComponent.getBall().getYVelocity() < 0) && (gameComponent.getBall().getXVelocity() < 0))){
						gameComponent.gettopLHSbat().makeAIMoveX(((batMaxY-C)/gradient) - 32);
					}
					else if((gameComponent.getBall().getYVelocity() < 0) && (gameComponent.getBall().getXVelocity() > 0)){
						gameComponent.gettopLHSbat().makeAIMoveX(((batMaxY-C)/gradient) - 32);
					}
				}
				//determine whether the ball will travel to the AI area (y = mx+c) y path of AI valid between 0 to 255
				else if((((gradient * batMaxX) + C ) < batMaxY ) && (((gradient * batMaxX) +  C) > 0)){ 
					if(((gameComponent.getBall().getYVelocity() < 0) && (gameComponent.getBall().getXVelocity() < 0))){ 
						// ball direction is top left, apply calibration
						gameComponent.gettopLHSbat().makeAIMoveY(((gradient * batMaxX) + C) + 32);
					}
					else if((gameComponent.getBall().getYVelocity() > 0) && (gameComponent.getBall().getXVelocity() > 0)){ 
						// ball direction is bottom left, apply calibration
						gameComponent.gettopLHSbat().makeAIMoveY(((gradient * batMaxX) + C) - 32);
					}
				}			
			}
			
			if(!gameComponent.getbottomLHSbat().isAIBat()){
				if (input.contains("A")) {
					if ( (gameComponent.getbottomLHSbat().GetxPosition() == (WINDOW_H/3)) && (gameComponent.getbottomLHSbat().GetyPosition() <= 768) && (gameComponent.getbottomLHSbat().GetyPosition() >= 768 - WINDOW_H/3 - 32) ) {	//vertical movement up
						gameComponent.getbottomLHSbat().SetyPosition((int) (gameComponent.getbottomLHSbat().GetyPosition() - paddleSpeed));	
					}
					if ( (gameComponent.getbottomLHSbat().GetxPosition() == (WINDOW_H/3 )) && (gameComponent.getbottomLHSbat().GetyPosition() < (768 - WINDOW_H/3 - 32)) ) {	//if it overshoots max point on vertical movement going up
						gameComponent.getbottomLHSbat().SetxPosition((int) (WINDOW_H/3));
						gameComponent.getbottomLHSbat().SetyPosition((int) (768 - WINDOW_H/3 - 32));	
					}
					if ( (gameComponent.getbottomLHSbat().GetxPosition() >= 0) && (gameComponent.getbottomLHSbat().GetxPosition() <= WINDOW_H/3) && (gameComponent.getbottomLHSbat().GetyPosition() == 768 - WINDOW_H/3 - 32) ) {//horizontal movement left
						gameComponent.getbottomLHSbat().SetxPosition((int) (gameComponent.getbottomLHSbat().GetxPosition() - paddleSpeed));
					}
					if ( (gameComponent.getbottomLHSbat().GetxPosition() < 0) && (gameComponent.getbottomLHSbat().GetyPosition() == 768 - WINDOW_H/3 - 32) ){ //overshoot max pt on horizontal going left
						gameComponent.getbottomLHSbat().SetxPosition((int) (0));
						gameComponent.getbottomLHSbat().SetyPosition((int) (768 - WINDOW_H/3 - 32));
					} 
				}
				if (input.contains("D")) {
					if ( (gameComponent.getbottomLHSbat().GetyPosition() == (768 - WINDOW_H/3 - 32 )) && (gameComponent.getbottomLHSbat().GetxPosition() >= 0) && (gameComponent.getbottomLHSbat().GetxPosition() <= WINDOW_H/3) ) {	//horizontal move right
						gameComponent.getbottomLHSbat().SetxPosition((int) (gameComponent.getbottomLHSbat().GetxPosition() + paddleSpeed));			
					}
					if ( (gameComponent.getbottomLHSbat().GetyPosition() == (768 - WINDOW_H/3 - 32)) && (gameComponent.getbottomLHSbat().GetxPosition() > WINDOW_H/3) ) {	//if it overshoots max point on horizontal movement going right
						gameComponent.getbottomLHSbat().SetxPosition((int) (WINDOW_H/3));
						gameComponent.getbottomLHSbat().SetyPosition((int) (768-WINDOW_H/3 - 32));				
					}
					if ( (gameComponent.getbottomLHSbat().GetyPosition() >= 768 - WINDOW_H/3 - 32) && (gameComponent.getbottomLHSbat().GetyPosition() <= 768 - 32) && (gameComponent.getbottomLHSbat().GetxPosition() == WINDOW_H/3) ) {	//vertical move down
						gameComponent.getbottomLHSbat().SetyPosition((int) (gameComponent.getbottomLHSbat().GetyPosition() + paddleSpeed));
					}
					if ( (gameComponent.getbottomLHSbat().GetyPosition() > 768) && (gameComponent.getbottomLHSbat().GetxPosition() == WINDOW_H/3) ){ //overshoot max pt on vertical going down
						gameComponent.getbottomLHSbat().SetxPosition((int) (WINDOW_H/3));
						gameComponent.getbottomLHSbat().SetyPosition((int) (768 - 32));
					} 
				}
			}
			else{
				//If this is an AI controlled bat: (This bat doesn't track ghost Ball's path)
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
						gameComponent.getbottomLHSbat().makeAIMoveX(((batMinY-C)/gradient) - 32);
					}
				}
				
				//determine whether the ball will travel to the AI vertical area (y = mx+c)
				else if((((gradient * batMaxX) + C ) > batMinY ) && (((gradient * batMaxX) +  C) < WINDOW_H)){ 
					if(((gameComponent.getBall().getYVelocity() > 0) && (gameComponent.getBall().getXVelocity() < 0))){ //
						gameComponent.getbottomLHSbat().makeAIMoveY(((gradient * batMaxX) + C) - 32);
					}
					else if((gameComponent.getBall().getYVelocity() < 0) && (gameComponent.getBall().getXVelocity() < 0)){ // 
						gameComponent.getbottomLHSbat().makeAIMoveY(((gradient * batMaxX) + C) - 32);
					}
				}	
			}
		
			if(!gameComponent.gettopRHSbat().isAIBat()){
				//Manual control can be added if needed, room for future improvement.
			}
			//If this paddle is AI controlled!
			else{
				double batMinX = (WINDOW_W - (WINDOW_H/3) - 32);
				double batMaxY = (WINDOW_H / 3);
				double gradient = (gameComponent.getBall().getYVelocity()) / (gameComponent.getBall().getXVelocity());
				double C = gameComponent.getBall().GetyPosition() - (gradient * gameComponent.getBall().GetxPosition());
				
				
				double ghostGradient = (gameComponent.getGhostBall().getYVelocity()) / (gameComponent.getGhostBall().getXVelocity());
				double ghostC = gameComponent.getGhostBall().GetyPosition() - (ghostGradient * gameComponent.getGhostBall().GetxPosition());
				
				//GHOST BALL
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
			}		
			//This base will always be player's controlled
			if (input.contains("LEFT")) {
				if ( (gameComponent.getbottomRHSbat().GetxPosition() == (1024 - WINDOW_H/3 - 32)) && (gameComponent.getbottomRHSbat().GetyPosition() >= 768 - WINDOW_H/3 - 32 ) && (gameComponent.getbottomRHSbat().GetyPosition() <= 768 - 32) ) {		//vertical down
					gameComponent.getbottomRHSbat().SetyPosition((int) (gameComponent.getbottomRHSbat().GetyPosition() + paddleSpeed));		
				}
				if ( (gameComponent.getbottomRHSbat().GetxPosition() == (1024 - WINDOW_H/3 - 32)) && (gameComponent.getbottomRHSbat().GetyPosition() > 768 - 32) ) {	//if it overshoots max point on vertical down
					gameComponent.getbottomRHSbat().SetxPosition((int) (1024 - WINDOW_H/3 - 32));
					gameComponent.getbottomRHSbat().SetyPosition((int) (768 - 32));		
				}
				if ( (gameComponent.getbottomRHSbat().GetxPosition() >= 1024 - WINDOW_H/3 - 32) && (gameComponent.getbottomRHSbat().GetxPosition() <= 1024 - 32) && (gameComponent.getbottomRHSbat().GetyPosition() == 768 - WINDOW_H/3 - 32) ) {	//horizontal left
					gameComponent.getbottomRHSbat().SetxPosition((int) (gameComponent.getbottomRHSbat().GetxPosition() - paddleSpeed));
				}
				if ( (gameComponent.getbottomRHSbat().GetxPosition() < 1024 - WINDOW_H/3 - 32) && (gameComponent.getbottomRHSbat().GetyPosition() == 768 - WINDOW_H/3 - 32) ){ //overshoot max pt on horizontal left 
					gameComponent.getbottomRHSbat().SetxPosition((int) (1024 - WINDOW_H/3 - 32));
					gameComponent.getbottomRHSbat().SetyPosition((int) (768 - WINDOW_H/3 - 32));
				} 
			}
			if (input.contains("RIGHT")) {
				if ( (gameComponent.getbottomRHSbat().GetyPosition() == (768 - WINDOW_H/3 - 32)) && (gameComponent.getbottomRHSbat().GetxPosition() >= 1024 - WINDOW_H/3 - 32) && (gameComponent.getbottomRHSbat().GetxPosition() <= 1024 - 32) ) {	//horizontal right
					gameComponent.getbottomRHSbat().SetxPosition((int) (gameComponent.getbottomRHSbat().GetxPosition() + paddleSpeed));			
				}
				if ( (gameComponent.getbottomRHSbat().GetyPosition() == (768 - WINDOW_H/3 - 32 )) && (gameComponent.getbottomRHSbat().GetxPosition() > 1024 - 32) ) {	//if it overshoots max point on horizontal right
					gameComponent.getbottomRHSbat().SetxPosition((int) (1024 - 32));
					gameComponent.getbottomRHSbat().SetyPosition((int) (768 - WINDOW_H/3 - 32));				
				}
				if ( (gameComponent.getbottomRHSbat().GetyPosition() >= 768 - WINDOW_H/3 - 32) && (gameComponent.getbottomRHSbat().GetyPosition() <= 768 - 32) && (gameComponent.getbottomRHSbat().GetxPosition() == 1024 - WINDOW_H/3 - 32) ) {	//vertical up
					gameComponent.getbottomRHSbat().SetyPosition((int) (gameComponent.getbottomRHSbat().GetyPosition() - paddleSpeed));
				}
				if ( (gameComponent.getbottomRHSbat().GetyPosition() < 768 - WINDOW_H/3 - 32) && (gameComponent.getbottomRHSbat().GetxPosition() == 1024 - WINDOW_H/3 - 32) ){ //overshoot max pt on vertical up
					gameComponent.getbottomRHSbat().SetxPosition((int) (1024 - WINDOW_H/3 - 32));
					gameComponent.getbottomRHSbat().SetyPosition((int) (768 - WINDOW_H/3 - 32));
				} 
			}
	//	}
		}
		
		// TODO: DEBUG ONLY
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
	
	/*
	 * --------------------------------------COLLISIONS--------------------------------------
	 */

	public void paddleCollisionCheck() {
		//If the ball collides with any of the bat, change its velocity.
			if (gameComponent.getBall().objectsIntersectBallAndPaddle(gameComponent.gettopLHSbat()) ||
					gameComponent.getBall().objectsIntersectBallAndPaddle(gameComponent.getbottomLHSbat()) ||
					gameComponent.getBall().objectsIntersectBallAndPaddle(gameComponent.gettopRHSbat()) ||
					gameComponent.getBall().objectsIntersectBallAndPaddle(gameComponent.getbottomRHSbat())) {	
				changeBallVelocity();
			}
	}
	
	public void changeBallVelocity(){
		//Increases ball speed by incrementing/decrementing its x and y velocity 
		gameComponent.getBall().setXVelocity(-gameComponent.getBall().getXVelocity());
		if(gameComponent.getBall().getXVelocity() > 0){
			gameComponent.getBall().setXVelocity((float)(gameComponent.getBall().getXVelocity() + speedIncrementOverTime));
		}
		else{
			gameComponent.getBall().setXVelocity((float)(gameComponent.getBall().getXVelocity() - speedIncrementOverTime));
		}
		if(gameComponent.getBall().getYVelocity() > 0){
			gameComponent.getBall().setYVelocity((float)(gameComponent.getBall().getYVelocity() + speedIncrementOverTime));
		}
		else{
			gameComponent.getBall().setYVelocity((float)(gameComponent.getBall().getYVelocity() - speedIncrementOverTime));
		}
		gameComponent.getpaddleDeflectSound().playSound();
	}

	public void baseCollisionCheck() {
		//If the base is already dead, don't bother checking for base collision again
		if (!topLHSBase.isDead()) {
			if (gameComponent.getBall().objectsIntersectBallAndBase(topLHSBase)) {
				topLHSBase.setIsDead(true);
				gameComponent.gettopLHSbat().setAIBat(true);
				//Play hit sound
				gameComponent.getbaseHitSound().playSound();
			}
		}
		if (!topRHSBase.isDead()) {
			if (gameComponent.getBall().objectsIntersectBallAndBase(topRHSBase)) {
				topRHSBase.setIsDead(true);
				gameComponent.gettopRHSbat().setAIBat(true);
				gameComponent.getbaseHitSound().playSound();
			}
		}

		if (!bottomRHSBase.isDead()) {
			if (gameComponent.getBall().objectsIntersectBallAndBase(bottomRHSBase)) {
				bottomRHSBase.setIsDead(true);
				gameComponent.getbaseHitSound().playSound();
			}
		}

		if (!bottomLHSBase.isDead()) {
			if (gameComponent.getBall().objectsIntersectBallAndBase(bottomLHSBase)) {
				bottomLHSBase.setIsDead(true);
				if(currentGameMode == gameMode.SINGLE){
					gameComponent.getbottomLHSbat().setAIBat(true);
				}
				gameComponent.getbaseHitSound().playSound();
			}
		}
	}

	public void wallCollisionCheck() {
		
		//Create iterators which will be used to work with the actual array  
		Iterator<Brick> topLHSBrickList = this.gameBrick.getTopLHSBrick().accessBrickArray().iterator();
		Iterator<Brick> bottomLHSbrickList = this.gameBrick.getBottomLHSBrick().accessBrickArray().iterator();
		Iterator<Brick> topRHSBrickList = this.gameBrick.getTopRHSBrick().accessBrickArray().iterator();
		Iterator<Brick> bottomRHSBrickList = this.gameBrick.getBottomRHSBrick().accessBrickArray().iterator();

		while (topRHSBrickList.hasNext()) {
			//Check all bricks of each base for collision
			Brick brick = topRHSBrickList.next();
			//if the ball intersects with a brick (in this case TRHS brick), deflect the ball, remove the brick from the array and play sound
			if (gameComponent.getBall().objectsIntersect(brick)) {
				//Depending on the brick type, the ball will deflect differently. 
				//This is why brick.getArrangement argument is needed
				deflect.deflectTheBall(gameComponent.getBall(), brick.getArrangement());
				topRHSBrickList.remove();
				this.gameBrick.getTopRHSBrick().removeBrick();
				gameComponent.getwallHitSound().playSound();
			}
		}
		while (topLHSBrickList.hasNext()) {
			Brick brick = topLHSBrickList.next();
			if (gameComponent.getBall().objectsIntersect(brick)) {
				deflect.deflectTheBall(gameComponent.getBall(), brick.getArrangement());
				topLHSBrickList.remove();
				this.gameBrick.getTopLHSBrick().removeBrick();
				gameComponent.getwallHitSound().playSound();
			}
		}
		while (bottomLHSbrickList.hasNext()) {
			Brick brick = bottomLHSbrickList.next();
			if (gameComponent.getBall().objectsIntersect(brick)) {
				deflect.deflectTheBall(gameComponent.getBall(), brick.getArrangement());
				bottomLHSbrickList.remove();
				this.gameBrick.getBottomLHSBrick().removeBrick();
				gameComponent.getwallHitSound().playSound();
			}
		}
		while (bottomRHSBrickList.hasNext()) {
			Brick brick = bottomRHSBrickList.next();
			if (gameComponent.getBall().objectsIntersect(brick)) {
				deflect.deflectTheBall(gameComponent.getBall(), brick.getArrangement());
				bottomRHSBrickList.remove();
				this.gameBrick.getBottomRHSBrick().removeBrick();
				gameComponent.getwallHitSound().playSound();
			}
		}
		
		//Reset ball previous velocity after all Brick List has been checked.
		//For more info, please jump to Deflect class
		deflect.setTempDir(99, 99);
	}


}
