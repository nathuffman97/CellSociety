 package Util;
 
/**
 * Creates the slider that controls the in-game update speed,
 * passes back to UI current slider position
 */
 
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

public class SpeedSlider {
	
	public static final int PADDING = 5;
	public static final int SPACING = 10;
	public static final double INCREMENT = 0.2;
	public static final int START_VAL = 1;
	public static final int[] COORDINATES = {0,65};
	private Slider slider;
	private Label sliderLabel;
	private HBox hb;
	
	/**
	 * Instantiates slider object
	 * 
	 * @param label		Slider description
	 */
	public SpeedSlider(String label) {
		makeSlider(0, 2*START_VAL, START_VAL, INCREMENT);
		sliderLabel = new Label(label);
		initHBox();
	}
	
	private void initHBox(){
		hb = new HBox(sliderLabel,slider);
		hb.setSpacing(SPACING);
		hb.setPadding(new Insets(PADDING));
		hb.setLayoutX(COORDINATES[0]);
		hb.setLayoutY(COORDINATES[1]);
	}
	
	private void makeSlider(int min, int max, int startingvalue, double increment) {
		slider = new Slider(min, max, startingvalue);
		slider.setMajorTickUnit(increment);
		slider.setMinorTickCount(0);
		slider.setSnapToTicks(true);
	}
	
	/**
	 * @return	current position of slider thumb
	 */
	public double getSliderSpeed() {
		return slider.getValue();
	}
	
	/**
	 * @return		the HBox that the slider is in
	 */
	public HBox getSlider(){
		return hb;
	}

}
