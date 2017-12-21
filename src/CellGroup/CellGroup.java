package CellGroup;
/**
 * Creates ArrayList of Cells and ArrayList of polygons. Controls and 
 * maintains cells (calls update and reset) and controls and maintains 
 * polygons (draws and updates color according to corresponding cell).
 * Also checks inputs and throws IOExceptions if invalid. Inherited
 * by shape-defining subclasses.
 * 
 * @author Natalie Huffman
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import CellGroup.Cell.Cell;
import CellGroup.Cell.FireCell;
import CellGroup.Cell.LifeCell;
import CellGroup.Cell.RPSCell;
import CellGroup.Cell.SegCell;
import CellGroup.Cell.WaTorCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;

public abstract class CellGroup {
	public static final int NONSTATE_PARAM_NUM = 3;
	public static final int INNER_SIZE = 560;
	public static final int RPS = 5;
	public static final int SEG = 4;
	public static final int WATOR = 3;
	public static final int LIFE = 2;
	public static final int FIRE = 1;
	public static final Paint[] STATE_COLORS = {Color.WHITE, Color.RED, Color.BLUE, Color.GREEN};
	public static final List<KeyCode> ALLOWED_KEYS = Arrays.asList( new KeyCode[] {KeyCode.E, KeyCode.R, KeyCode.B, KeyCode.G});
	
	private LinkedList<Double> configInfo = new LinkedList<Double>();
	private double[] cellRatio;
	protected List<Cell> cellList;
	protected int cellWidth;
	protected int cellHeight;
	protected int simType;
	protected List<Polygon> grid;
	private int[] cellStates;
	private int numStates;
	private int stroke;
	protected int neighborConfigState;
	protected int torus;
	private KeyCode currentlyPressed;
	private int numSpecifiedCells;

	/**
	 * Pulls parameters from list to store locally if needed, creates cells
	 * 
	 * @param configList	List containing all instance variables + extra info
	 * 						needed by Cell classes; this extra info is unique to each subclass
	 * @throws IOException	if received parameters are invalid 
	 */
	public CellGroup(List<Double> configList) throws IOException{
		configInfo = (LinkedList<Double>) configList;

		initInstanceVar();
		checkValidValues();
		
		initCells();
	}
	
	private void initInstanceVar() throws IOException{			
		stroke = configInfo.removeFirst().intValue();
		neighborConfigState = configInfo.removeFirst().intValue();
		torus = configInfo.removeFirst().intValue();
		simType = configInfo.removeFirst().intValue();	
		cellWidth = configInfo.removeFirst().intValue();
		cellHeight = configInfo.removeFirst().intValue();
		numStates = configInfo.removeFirst().intValue();
		numSpecifiedCells = configInfo.removeFirst().intValue();
		cellList = Arrays.asList(new Cell[cellWidth*cellHeight]);

		initSpecifiedCells();
		
		if (configInfo.size()<numStates) throw new IOException("Not enough inputs passed!");

		cellStates = new int[numStates];
		for (int i = 0; i < numStates; i++)
			cellStates[i] = configInfo.removeFirst().intValue();		

		int totalCells = cellWidth * cellHeight;
		cellRatio = new double[cellStates.length+1];
		cellRatio[0] = 0;
		
		for(int i = 1; i < cellRatio.length; i++){
			cellRatio[i] = (double)cellStates[i-1] / totalCells + cellRatio[i-1];
		}
	}
	
	private void initSpecifiedCells() throws IOException{		
		for (int i = 0; i < numSpecifiedCells; i++){
			int state = configInfo.removeFirst().intValue();
			int dex = configInfo.removeFirst().intValue();
			if (state < 0 || state >= numStates || !isValid(dex))
				throw new IOException("Invalid XML specifications!");

			cellList.set(dex, cellType(state));
		}
	}
	
	private void checkValidValues() throws IOException{
		if (cellWidth <= 0 || cellHeight <= 0 || 
				simType <= 0 ||
				stroke < 0 || stroke > 1 || 
				torus < 0 || torus > 1 ||
				numStates <= 0 )
			throw new IOException("Invalid XML specifications!");
		
		int total = 0;
		for (int i: cellStates)
			total+=i;
		total+=numSpecifiedCells;
		if (total != cellWidth*cellHeight) throw new IOException("Invalid XML speficiations! Incorrect number of cells for the specified grid.");
	}
	
	/**
	 * creates and initializes array of cell objects
	 */
	protected void initCells(){
		int[] tempStates = Arrays.copyOf(cellStates, cellStates.length);
		for (int i = 0; i < cellWidth*cellHeight; i++){
			if (cellList.get(i) == null){
				int state = pickState(tempStates);
				cellList.set(i, cellType(state));
			}
		}
		
		neighborFill();
		
		for (Cell c:cellList)
			c.pushStateToNeighbors();

	}
	
	private int pickState(int[] tempStates){
		Random rand = new Random();
		while(true){
			double x = rand.nextDouble();
			for(int i = 0; i < cellRatio.length-1; i++){
				if(x > cellRatio[i] && x <= cellRatio[i+1] && tempStates[i] > 0){
					tempStates[i]--;
					return i;
				}
			}
		}
	}
	
	private Cell cellType(int state){
		switch (simType){
			case FIRE: return new FireCell(state, configInfo);
			case LIFE: return new LifeCell(state, configInfo);
			case WATOR: return new WaTorCell(state, configInfo);
			case SEG: return new SegCell(state, configInfo);
			case RPS: return new RPSCell(state, configInfo);
		}
		return null;
	}
	
	private void neighborFill(){
		for (int i = 0; i < cellWidth*cellHeight; i++){
			cellList.get(i).setNeighbors(neighborReturn(i));
		}
	}
	
	/**
	 * finds neighboring cells based on cell index and algorithm
	 * specified by xml file
	 * 
	 * @param i		the index of the cell
	 * @return		a list of neighboring cells
	 */
	protected abstract List<Cell> neighborReturn(int i);
	
	/**
	 * calculates state proportions for the cells
	 * 
	 * @return	how many of cells each state are currently present 
	 */
	public int[] getCellStates() {
		int[] stateCounts = new int[numStates];
		for(Cell cell : cellList) {
			stateCounts[cell.getState()] += 1;
		}
		return stateCounts;
	}
	
	/**
	 * Instantiates 2D Rectangle object and draws them on the scene
	 * 
	 * @param root		Group object to which Rectangles must be added to show on scene
	 */
	public List<Polygon> drawShapes(){
		grid = Arrays.asList(new Polygon[cellWidth*cellHeight]);
		
		for (int i = 0; i < cellWidth*cellHeight; i++){
    		grid.set(i, new Polygon(calcPoints(i)));
    		int index = i;
    		grid.get(i).setOnMouseClicked(e -> handleMouse(e, index));
	    	if (stroke==1)
	    		grid.get(i).setStroke(Color.WHITE);
    	}
    	
		updateColor();
		
		return grid;
	}
	
	/**
	 * Calculates the coordinate points for a polygon of any particular index
	 * 
	 * @param i		index number of cell
	 * @return		list of x and y coordinate points
	 */
	protected abstract double[] calcPoints(int i);
	
	private void updateColor(){
		for (int i = 0; i < cellWidth; i ++)
			for (int j = 0; j < cellHeight; j++)
				grid.get(i*cellHeight+j).setFill(STATE_COLORS[cellList.get(i*cellHeight+j).getState()]);
	}
	
	/**
	 * Calls on each cell to update, then calls on each cell to push its
	 * updated state to all of its neighbors. Also updates color of Rectangles
	 * 
	 * @throws Exception
	 */
	public void update() throws Exception {
		for (Cell c: cellList)
			c.update();
		
		for (Cell c: cellList)
			c.reset();
		
		updateColor();
	}
	
	/**
	 * Get all the needed information from cell group to save the current simulation state.
	 * 
	 * @return List containing all data from cell group needed to recreate this state
	 */
	public List<String> getSimInfoCopy(){
		List<String> returnList = new ArrayList<>();
		addGlobalSimData(returnList);
		returnList.add("" + cellWidth * cellHeight); // tell the parser how much data to expect
		for(Cell c : cellList) returnList.add("" + c.getState());
		for(Double d : configInfo) returnList.add(d.toString());

		return returnList;
	}

	private void addGlobalSimData(List<String> returnList) {
		returnList.add("" + stroke);
		returnList.add("" + neighborConfigState);
		returnList.add("" + torus);
		returnList.add("" + simType);
		returnList.add("" + cellWidth);
		returnList.add("" + cellHeight);
		returnList.add("" + (int)numStates);
	}
	
	/**
	 * Checks index validity for cellList
	 * 
	 * @param dex	the index number of the cell
	 * @return	whether or not the index is a valid cell
	 */
	protected boolean isValid(int dex){
		return dex>= 0 && dex<cellWidth*cellHeight;
	}
	
	/**
	 * Returns row number from index number
	 * 
	 * @param num	the index number of the cell
	 * @return	the associated row number
	 */
	protected int iFromNum(int num){
		int i = num/cellWidth;
		return i;
	}
	
	/**
	 * Returns column number from index number
	 * 
	 * @param num	the index number of the cell
	 * @return	the associated column number
	 */
	protected int jFromNum(int num){
		int j = num%cellWidth;
		return j;
	}
	
	/**
	 * Checks for left vs right sided cells
	 * 
	 * @param num	the index number of the cell
	 * @return	whether an edge polygon is on the left or the right 
	 */
	protected int calcLeftRight(int num){
		return num%cellWidth==0? -1: 1;
	}
	
	/**
	 * adds cells of index specified to the arraylist
	 * 
	 * @param neighbors		the list of neighbors
	 * @param indices		indices of cells to add
	 */
	protected void add(List<Cell> neighbors, int[] indices){
		for (int dex: indices)
			if (isValid(dex))
				neighbors.add(cellList.get(dex));
	}
	
	/**
	 * removes cells of index specified from the arraylist
	 * 
	 * @param neighbors		the list of neighbors
	 * @param indices		indices of cells to remove
	 */
	protected void remove(List<Cell> neighbors, int[] indices){
		for (int dex: indices)
			if (isValid(dex))
				neighbors.add(cellList.get(dex));
	}
	
	/**
	 * adds opposite side cells if toroidal edges are selected
	 * 
	 * @param num		the index number of the cell
	 * @param leftRight		whether a cell is on the left edge or the right edge
	 * @param neighbors		the list of neighbors
	 */
	protected void toroidal(int num, int leftRight, List<Cell> neighbors){
		int[] dex = {num-leftRight*cellWidth+leftRight};
		add(neighbors, dex);
	}
	
	protected void handleMouse(MouseEvent e, int index){
		if(currentlyPressed != null){
			int newState = getNewState();
			if(newState < numStates){
				updateSingleCell(index, newState);
			}
		}
	}

	private void updateSingleCell(int index, int newState) {
		int oldState = cellList.get(index).getState();
		cellList.get(index).setState(newState);
		cellList.get(index).pushUpdateToNeighbors(oldState, newState);
		updateOneColor(index);
	}
	
	private void updateOneColor(int index) {
		grid.get(index).setFill(STATE_COLORS[cellList.get(index).getState()]);
	}

	private int getNewState() {
		return ALLOWED_KEYS.indexOf(currentlyPressed);
	}
	
	/**
	 * 
	 * Assign what key is pressed and make sure it's valid for the sim.
	 * 
	 * @param code keycode of pressed key
	 */
	public void handleKeyPress(KeyCode code){
		if(currentlyPressed == null && ALLOWED_KEYS.contains(code)){
			currentlyPressed = code;
		}
	}
	
	/**
	 * 
	 * Unassign what key is pressed.
	 * 
	 * @param code keycode of released key
	 */
	public void handleKeyRelease(KeyCode code){
		if(currentlyPressed == code){
			currentlyPressed = null;
		}
	}
}