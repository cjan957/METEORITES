package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Timer {
	private int remainingSeconds;
	private boolean countdownOver;

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
		if(remainingSeconds == 3){
			gc.setFill(Color.YELLOW);
			gc.setStroke(Color.BLUE);
			gc.setLineWidth(2);
			Font theFont = Font.font("Arial", FontWeight.BOLD, 72);
			gc.setFont(theFont);
			gc.fillText("3", 1024/2, 768/2);
			gc.strokeText("3", 1024/2, 768/2);
		}
		else if(remainingSeconds == 2){
			gc.setFill(Color.YELLOW);
			gc.setStroke(Color.BLUE);
			gc.setLineWidth(2);
			Font theFont = Font.font("Arial", FontWeight.BOLD, 72);
			gc.setFont(theFont);
			gc.fillText("2", 1024/2, 768/2);
			gc.strokeText("2", 1024/2, 768/2);
		}
		else if(remainingSeconds == 1){
			gc.setFill(Color.YELLOW);
			gc.setStroke(Color.BLUE);
			gc.setLineWidth(2);
			Font theFont = Font.font("Arial", FontWeight.BOLD, 72);
			gc.setFont(theFont);
			gc.fillText("1", 1024/2, 768/2);
			gc.strokeText("1", 1024/2, 768/2);
		}
	}

}
