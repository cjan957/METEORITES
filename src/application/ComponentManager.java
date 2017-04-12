package application;

import com.ttcj.components.Ball;
import com.ttcj.components.Base;
import com.ttcj.components.Bat;
import com.ttcj.components.Sound;

import application.GamePlay.gameMode;

public class ComponentManager {
	
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
		
	private static final int WINDOW_W = 1024;
	private static final int WINDOW_H = 768;
	
	public ComponentManager(gameMode currentGameMode){
		gameInitialise(currentGameMode);
	}
	
	public void gameInitialise(gameMode currentGameMode){
		// Create objects needed for the game play
		// with necessary properties. <Image, xPos, yPos, isAI, location>
		if(currentGameMode == gameMode.MULTI){ //(2AIs vs 2 Humans)
			topLHSbat = new Bat("paddle_32.png", WINDOW_H / 3, WINDOW_H / 3, true, Bat.batPosition.TopLEFT);	
			bottomLHSbat = new Bat("paddle_32.png", WINDOW_H / 3, 768 - WINDOW_H/3 - 32, false, Bat.batPosition.BottomLEFT);
			topRHSbat = new Bat("paddle_32.png", 1024 - WINDOW_H / 3 - 32, WINDOW_H / 3, true, Bat.batPosition.TopRIGHT);
			bottomRHSbat = new Bat("paddle_32.png", 1024 - WINDOW_H / 3 - 32, 768 - WINDOW_H / 3 - 32, false, Bat.batPosition.BottomRIGHT);
		}
		else{ //single player init (3 AIs , 1 Human)
			topLHSbat = new Bat("paddle_32.png", WINDOW_H / 3, WINDOW_H / 3, true, Bat.batPosition.TopLEFT);	
			bottomLHSbat = new Bat("paddle_32.png", WINDOW_H / 3, 768 - WINDOW_H/3 - 32, true, Bat.batPosition.BottomLEFT);
			topRHSbat = new Bat("paddle_32.png", 1024 - WINDOW_H / 3 - 32, WINDOW_H / 3, true, Bat.batPosition.TopRIGHT);
			bottomRHSbat = new Bat("paddle_32.png", 1024 - WINDOW_H / 3 - 32, 768 - WINDOW_H / 3 - 32, false, Bat.batPosition.BottomRIGHT);
		}
		
		//Balls
		ball = new Ball("b10008.png", WINDOW_W / 2 - 32, WINDOW_H / 2 - 16, 32, 32);
		ghostBall = new Ball("b10008.png", WINDOW_W / 2 - 32, WINDOW_H / 2 - 16, 32, 32);
		
		 //Sounds
		 paddleDeflect = new Sound("Sounds/paddleDeflect.wav");
		 wallHit = new Sound("Sounds/wallHit.wav");
		 baseHit = new Sound("Sounds/baseHit.aiff");
		 countdownVoice = new Sound("Sounds/countdownVoice.wav");
		 countdownSound = new Sound("Sounds/countdownSoundStart.wav");
		 winJingleSound = new Sound("Sounds/winJingle.wav");
		 loseJingleSound = new Sound("Sounds/loseJingle.wav");
	}
		
	//Balls Getter
	public Ball getBall(){
		return ball;
	}
	
	public Ball getGhostBall(){
		return ghostBall;
	}
	
	//Bats Getter
	public Bat gettopLHSbat(){
		return topLHSbat;
	}
	
	public Bat getbottomLHSbat(){
		return bottomLHSbat;
	}
	
	public Bat gettopRHSbat(){
		return topRHSbat;
	}
	
	public Bat getbottomRHSbat(){
		return bottomRHSbat;
	}
	
	//Bases Getter
	public Base gettopLHSBase(){
		return topLHSBase;
	}
	
	public Base getbottomLHSBase(){
		return bottomLHSBase;
	}
	
	public Base gettopRHSBase(){
		return topRHSBase;
	}
	
	public Base getbottomRHSBase(){
		return bottomRHSBase;
	}
	
	//Sounds getter
	public Sound getpaddleDeflectSound(){
		return paddleDeflect;
	}
	
	public Sound getwallHitSound(){
		return wallHit;
	}
	
	public Sound getbaseHitSound(){
		return baseHit;
	}
	
	public Sound getcountdownVoice(){
		return countdownVoice;
	}
	
	public Sound getcountdownSound(){
		return countdownSound;
	}
	
	public Sound getwinJingleSound(){
		return winJingleSound;
	}
	
	public Sound getloseJingleSound(){
		return loseJingleSound;
	}
	
	
}
