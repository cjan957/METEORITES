/*
 * Game Class
 * 	
 */
package application;
import com.ttcj.view.MainMenuManager;
import javafx.application.Application;
import javafx.stage.Stage;


public class Game extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage gameStage) {
		MainMenuManager viewMgr = new MainMenuManager();
		viewMgr.MainMenu(gameStage);
	}

}
