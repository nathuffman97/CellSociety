import Game.Game;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Crate a launch screen for the game to prevent crashes when no data file is selected.
 * Could possibly implement two two files side by side if the team chooses to do that.
 * 
 * @author Matthew Dickson
 *
 */
public class SplashScreen extends Application{
	public static final int BUTTON_PADDING = 20;
	public static final int SIZE = Game.SIZE;
	private Stage thisWindow;
	private Button oneFile = new Button();
	private Button twoFiles = new Button();
	private VBox buttons = new VBox();

	
	/**
	 * Start the screen on the splash screen.
	 * 
	 */
	@Override
	public void start(Stage s) throws Exception {
		thisWindow = s;
		makeButtons();
		prettifyWindow();
		thisWindow.show();
	}
	
	
	private void prettifyWindow(){
		thisWindow.setTitle(Game.GAME_TITLE);
		thisWindow.setScene(new Scene(buttons, SIZE, SIZE));
		buttons.setAlignment(Pos.CENTER);
		VBox.setMargin(oneFile, new Insets(BUTTON_PADDING));
		VBox.setMargin(twoFiles, new Insets(BUTTON_PADDING));
	}
	
	private void makeButtons(){
		oneFile.setText("Launch One Simulation");
		oneFile.setOnAction(e -> launchOneFile());
		twoFiles.setOnAction(e-> launchTwoFiles());
		twoFiles.setText("Launch Two Simulations");
		buttons.getChildren().addAll(oneFile, twoFiles);
	}

	private void launchTwoFiles() {
		Thread t1 = new Thread(new Game(new Stage()));
		Thread t2 = new Thread(new Game(thisWindow));
		t1.run();
		t2.run();
	}

	private void launchOneFile() {
		Thread thread = new Thread(new Game(thisWindow));
		thread.run();
	}
	
	/**
	 * Launch the program
	 * 
	 * @param args String[] of information read from the command line
	 */
	public static void main(String[] args){
		launch(args);
	}

}
