package Game; 
/**
 * Main game class, scene/stage is created here, creates UI and cellgroup
 * and calls upon them to initiate on-screen updates. All 
 * drawing occurs here.
 * 
 * @author Natalie Huffman
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import CellGroup.CellGroup;
import CellGroup.HexCellGroup;
import CellGroup.SquareCellGroup;
import CellGroup.TriCellGroup;
import Util.AreaChartSample;
import Util.UI;
import Util.Util;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game extends Application implements Runnable{
	public static final int MIN_POSSIBLE_INFO = 10;
	public static final int PAUSE_FONT_SIZE = 30;
	public static final String GAME_TITLE = "CellSociety";
	public static final int PAUSETEXT_DEX = 0;
	public static final int ENDTEXT_DEX = 1;
	public static final int INVALIDTEXT_DEX = 2;
	public static final int GRAPHTITLE_DEX = 5;
	public static final double UPDATE_FREQ = 1;
	public static final int PAUSETEXT_X = 30;
	public static final int PAUSETEXT_Y = 60;
	public static final int SIZE = 700;
	public static final Paint BACKGROUND = Color.LIGHTGREEN;
	public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int CONSTANT_SPEED = 60;
    public static final String INTERFACE_FILE = "data/InterfaceText.txt";
    
    private Group root = new Group();
	private Util util  = new Util();
	private double speed = 1;
	private double totalTime = 0;
	private double nextUpdate = UPDATE_FREQ;
	private double cellShapes;
	private List<String> simText = new ArrayList<String>();
	private Text pauseText = new Text(PAUSETEXT_X,PAUSETEXT_Y,"");
    private Boolean stopGraphing = false;
    private Queue<Number> dataQ= new ConcurrentLinkedQueue<Number>();
    
	private UI userInterface;
    private CellGroup cells;
	private Scene myScene;
	private Stage myStage;
	private List<String> cellStateNames;
	private AreaChartSample aCS;
    	
	/**
	 * Creates scene, draws objects
	 * @param Stage		stage that is displayed
	 */
	@Override
	public void start(Stage s) throws Exception {
		myStage = s;
		s.setTitle(GAME_TITLE);
		initGame(SIZE, SIZE, BACKGROUND);
		myStage.setScene(myScene);
		myScene.setOnKeyPressed(e -> cells.handleKeyPress(e.getCode()));
		myScene.setOnKeyReleased(e -> cells.handleKeyRelease(e.getCode()));
		myStage.show();
		
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
				e -> {step(SECOND_DELAY);});
		Timeline animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		
		animation.getKeyFrames().add(frame);
		animation.play();
	}

	private void endScreen(){
		pauseText.setText(simText.get(ENDTEXT_DEX));
	}
	
    private void initGame(int width, int height, Paint background){
    	myScene = new Scene(root, width, height, background);
    	
    	try {
			userInterface = new UI();
    		simText = util.readTextFile(new File(INTERFACE_FILE));
			readXML();
		} catch (IOException e) {
			UI.exceptionThrower(e);
		}
    	
    	setUpGraph();
    	draw();
    }
    
    private void restart(){
		try {
			readXML();
			totalTime = 0;
			nextUpdate = UPDATE_FREQ;
			root.getChildren().clear();
			setUpGraph();
			draw();
		} catch (IOException e) {
			UI.exceptionThrower(e);
			restart();
		}
    }
    
    private void readXML() throws IOException{
    	File dataFile = userInterface.getFileUI();
    	if(dataFile == null) throw new IOException("File is null!");
    	
    	List<String> data = util.parseText(dataFile);
    	if (data.size()<MIN_POSSIBLE_INFO) throw new IOException("Invalid parameters in XML file!");
    	
    	LinkedList<Double> configInfo = new LinkedList<>();
    	for (String s: data)
    		configInfo.add(Double.parseDouble(s));
    	
    	cellStateNames = util.getCellStateNames();
    	cellShapes = configInfo.pop();

    	cells = new TriCellGroup(configInfo);
    	//initCells(cellShapes, configInfo);
    }
    
    private void initCells(double type, List<Double> configInfo) throws IOException{
    	switch ((int) type){
    	case 0:
    		cells = new SquareCellGroup(configInfo);
    		break;
    	case 1:
    		cells = new HexCellGroup(configInfo);
    		break;
    	case 2:
    		cells = new TriCellGroup(configInfo);
    		break;
    	}
    }
    
    /**
     * Sets up the graph of the cell states over time 
     * Assumptions: util can correctly determine the titles of each series of cell states to be displayed
     */
    private void setUpGraph() {
    	String title;
		try {
			title = util.readTextFile(new File(UI.UITEXT_DIRECTORY)).get(GRAPHTITLE_DEX);
			aCS = new AreaChartSample(cellStateNames,title);
		} catch (FileNotFoundException e) {
			aCS = new AreaChartSample(cellStateNames, "ERROR ON TITLE");
		}
		root.getChildren().add(aCS.getAreaChart());
    	plot();
    }
    
    private void plot() {
		int[] cellStates = cells.getCellStates();
		for(int i = 0; i < cellStates.length; i++) {
        	dataQ.add(cellStates[i]);
		}
		aCS.addDataToSeries(dataQ);
	}
    
    private void draw(){
    	pauseText.setFont(Font.font(Font.getDefault().toString(), PAUSE_FONT_SIZE));
    	
    	for (Polygon p: cells.drawShapes())
	    	root.getChildren().add(p);
    	
    	for (Button b: userInterface.getButtons())
    		root.getChildren().add(b);
    	
    	root.getChildren().add(userInterface.getSlider());
    	root.getChildren().add(pauseText);    	
    }
    
	private void step(double elapsedTime){
		speed = userInterface.getSpeed();
		
		if (speed != 0)
		{
			totalTime+=elapsedTime;
			pauseText.setText("");
			stopGraphing = false;
		}
		try{
			if (totalTime>nextUpdate){
				cells.update();
				if(!stopGraphing) 
					plot();
				nextUpdate+=UPDATE_FREQ/speed;
			}
			else if (userInterface.step()) {
				cells.update();
				plot();
			}
			else if (speed==0) {
				pauseText.setText(simText.get(PAUSETEXT_DEX));
				stopGraphing = true;
			}
		} catch(Exception e){
			UI.exceptionThrower(e);
			endScreen();
		}
		
		if (userInterface.restart())
			restart();
		if(userInterface.shouldSave())
			saveToFile();	

	}

	private void saveToFile(){
		speed = 0;
		File f = userInterface.getSaveLocation();
		if (f != null) {
			try {
				List<String> simInfo = new LinkedList<String>();
				simInfo.addAll(cells.getSimInfoCopy());
				simInfo.add(0, "" + (int) cellShapes);
				util.saveFile(simInfo, f);
			} catch (IOException e) {
				UI.exceptionThrower(e);
			}
		}
	}
	
	/**
	 * Create game to run in a thread
	 * 
	 * @param s stage to use
	 */
	public Game(Stage s){
		myStage = s;
	}
	@Override
	/**
	 * Run this game from a thread by calling start
	 * 
	 */
	public void run() {
		try {
			start(myStage);
		} catch (Exception e) {
			UI.exceptionThrower(e);
		}
	}
}
