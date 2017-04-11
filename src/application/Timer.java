package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Timer {
	private int remainingSeconds;
	private boolean countdownOver;
	boolean showTimerOnScreen;
	
	public Timer(int initialTime, boolean visibility){
		remainingSeconds = initialTime;
		showTimerOnScreen = visibility;
	}

	
	public void setVisibility(boolean value){
		showTimerOnScreen = value;
	}
	
	public boolean getVisibility(){
		return showTimerOnScreen;
	}
	
	public void setTime(int seconds){
		remainingSeconds = seconds;
	}
	
	public int getTime(){
		return remainingSeconds;
	}
	
	public void countDown(){
		remainingSeconds--;
	}
	
	public void setTimeOut(boolean value){
		countdownOver = value;
	}
	
	public boolean isTimeOut(){
		return countdownOver;
	}
	
	public void renderCountDownTimer(GraphicsContext gc){
		gc.setFill(Color.YELLOW);
		gc.setStroke(Color.BLUE);
		gc.setLineWidth(2);
		Font theFont = Font.font("Arial", FontWeight.BOLD, 72);
		gc.setFont(theFont);
		gc.fillText(Integer.toString(this.remainingSeconds), (1024/2)-15, 768/2 - 30);
		gc.strokeText(Integer.toString(this.remainingSeconds), (1024/2)-15, 768/2 - 30);
	}
	
	public void renderMasterTimer(GraphicsContext gc){
		gc.setFill(Color.WHITE);
		Font theFont = Font.font("Arial", FontWeight.BOLD, 32);
		gc.setFont(theFont);
		gc.fillText(Integer.toString(this.remainingSeconds), (gc.getCanvas().getWidth()/2)-15, 50);
	}

}
