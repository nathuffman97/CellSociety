package Util;

/**
 * Controls the user interface for the game-- file choosing,
 * buttons, the slider, etc. Exception throwing also occurs here.
 * 
 * @author Natalie Huffman
 * @author Owen Smith
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/** Class UI: Manages the buttons, sliders, choosers, and other nodes that the user interacts with. Has event handlers, booleans and constructors. 
 * Also contains indices to know where to get information from text files for appropriate nodes.
 * Assumptions: The UI is flexible to incorporate new user interfaces. 
 * Dependencies: Depends on certain information passed from game, such as the different kinds of cell states, and how the user interacts with each 
 * node in the UI. 
 * Example of how to use it: create the UI, which will set up nodes along with it. Add these nodes to the root of the scene. Return values of booleans
 * when appropriate, like if pausing the game or loading a new file.
 * Credit to Robert Duvall for file chooser and exception throwers
 * 
 * @author Natalie Huffman, Owen Smith
 */

public class UI {
	
	public static final String ERROR_MESSAGE = "I'm sorry Dave, I'm afraid I can't do that.";
	public static final int BUTTON_GAP = 50;
	public static final int BUTTON_Y_TOP = 120;
	public static final int BUTTON_X = 5;
	public static final int NUMBUTTONS = 4;
	public static final int PAUSE_DEX = 6;
	public static final int RESUME_DEX = 7;
	public static final String UITEXT_DIRECTORY = "data/Labels.txt";
	
	private List<Button> buttons = new ArrayList<Button>();
	private double oldSpeed = 1;
	private boolean restart = false;
	private boolean step = false;
	private boolean shouldSave = false;
	private List<String> UItext;
	private SpeedSlider slider;
	private ColorPicker colorPicker;
	
	
	/**
	 * Initializes slider and buttons, draws on scene
	 * 
	 * @param root		Group object to which UI objects must be added to show on scene
	 * @throws IOException	if user does not choose a valid XML file 
	 */
	public UI() throws IOException{
		Util parser = new Util();
		UItext = parser.readTextFile(new File(UITEXT_DIRECTORY));
		slider = new SpeedSlider(UItext.get(NUMBUTTONS));
		buttonInit();		
		colorPicker = new ColorPicker();
		colorPickerInit();
	}
	
	/** buttonInit: Initializes the buttons with equal spacing depending on the number of buttons present
	 *  Assumptions: makeButton is passed the correct arguments 
	 * 
	 */
	private void buttonInit(){	
		for(int i = 0; i < NUMBUTTONS; i++) {
			Button b = makeButton(BUTTON_X, BUTTON_Y_TOP+BUTTON_GAP*i);
			b.setText(UItext.get(i));
			b.setOnAction(e -> ButtonClicked(e));
			buttons.add(b);
		}
	}
	
	private void colorPickerInit() {
		colorPicker.setLayoutX(3*BUTTON_X);
		colorPicker.setLayoutY(BUTTON_Y_TOP + BUTTON_GAP*(NUMBUTTONS + 1));
	}

	/** makeButton method: makes a new button with the specified coordinates
	 * 
	 * @param x--a double for the x button coordinate
	 * @param y--a double for the y button coordinate
	 * @return -- a button to be displayed on the screen
	 */
	private Button makeButton(double x,double y) {
		Button ret = new Button();
		ret.setLayoutX(x);
		ret.setLayoutY(y);
		return ret;
	}
	
	/** ButtonClicked: an event handler that changes parameters of the simulation depending the button clicked and when it is clicked in the game
	 * @param e -- the event (the button that was clicked)
	 */
	private void ButtonClicked(ActionEvent e) {
		if (e.getSource() == buttons.get(0)) {
			if (oldSpeed == 0)
				buttons.get(0).setText(UItext.get(RESUME_DEX));
			else
				buttons.get(0).setText(UItext.get(PAUSE_DEX));
			
			pause();
		}
		else if(e.getSource() == buttons.get(1)) {
			restart = true;
			pause();
		}
		else if(e.getSource() == buttons.get(2)){
			if (oldSpeed == 0)
				pause();
			step = true;
		}
		else if(e.getSource() == buttons.get(3)) {
			shouldSave = true;
		}
	}
	
	/**
	 * @return	whether or not save buttons has been clicked
	 */
	public boolean shouldSave(){
		return shouldSave;
	}
	
	private void pause() {
		if (oldSpeed == 0)
			oldSpeed = slider.getSliderSpeed();
		else
			oldSpeed = 0;
	}
	
	/**
	 * @return	whether or not the restart button has been clicked
	 */
	public boolean restart(){
		boolean temp = restart;
		restart = false;
		return temp;
	}
	
	/**
	 * @return	whether or not the step button has been clicked
	 */
	public boolean step(){
		boolean temp = step;
		step = false;
		return temp;
	}
	
	/**
	 * @return	the current position of the slider
	 */
	public double getSpeed(){
		return oldSpeed!=0? 0: slider.getSliderSpeed();
	}
	
	/**
	 * Opens a file chooser dialog for the user to choose where they'd like to save a XML file. Can
	 * and must return null to avoid compiler errors, so calling methods must confirm they received
	 * a non-null file
	 * 
	 * @return the selected file to save. Could be null if no file is chosen.
	 */
	public File getSaveLocation(){
		FileChooser fc = configureFileChooser();
		fc.setTitle("Save Configuration");
		File f = fc.showSaveDialog(new Stage());
		return f;
	}

	private FileChooser configureFileChooser() {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().setAll(new ExtensionFilter("Text Files", Util.DATA_FILE_EXTENSION));
		fc.setInitialDirectory(new File(System.getProperty("user.dir")));
		shouldSave = false;
		return fc;
	}
	
	/**
	 * returns the buttons so they can be added to the group
	 * 
	 * @return on-screen buttons
	 */
	public List<Button> getButtons(){
		//same reasoning as getSlider
		return buttons;
	}
	
	/**
	 * returns the slider's HBox so that it can be added to the group
	 * 
	 * @return the Hbox associated with the slider
	 */
	public HBox getSlider(){
		//decided to use getter over passing in the group object,
		//which could then be changed
		return slider.getSlider();
	}
	
	/** getColorPicker--returns the colorPicker from the UI so that game can add it to the root
	 *  Assumptions: the private variable colorPicker is already initialized
	 * @return
	 */
	public ColorPicker getColorPicker() {
		return colorPicker;
	}
	
	/**
	 * Generic error thrower. Creates dialog box for user to see the error that occurred
	 * before the program takes appropriate measures to either correct the error or quit
	 * gracefully if unrecoverable. USERS MUST SPECIFY WHAT HAPPENS AFTER THIS ERROR DIALOG
	 * IS SHOWN. Method is static to allow other classes to be able to also throw errors.
	 * 
	 * @param e error thrown, used to get message of what specifically went wrong
	 */
	public static void exceptionThrower(Exception e){
		Alert err = new Alert(AlertType.ERROR);
		err.setHeaderText(ERROR_MESSAGE);
		err.setContentText(e.getMessage());
		err.show();
		e.printStackTrace();
	}
	
	/**
	 * Ask the user for a file by opening a new FileChooser. Can and must return null 
	 * to avoid compiler errors, so calling methods must confirm they received a non-null file
	 * 
	 * @return Selected file, possibly null
	 */
	public File getFileUI (){
    	FileChooser fc = configureFileChooser();
    	fc.setTitle("Open Data File");
    	File f = fc.showOpenDialog(new Stage());
    	return f;
    }
}
