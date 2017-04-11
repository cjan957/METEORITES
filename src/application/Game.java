/*
 * Game Class
 * 	
 */
package application;
import com.ttcj.view.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;


public class Game extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage gameStage) {
		ViewManager viewMgr = new ViewManager();
		viewMgr.MainMenu(gameStage);
	}

}
