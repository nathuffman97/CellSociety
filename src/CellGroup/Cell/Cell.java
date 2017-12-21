package CellGroup.Cell;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Matthew Dickson
 *
 * Super class for all cell objects.
 * Non-cell users should ONLY call constructors, update(), pushStateToNeighbors(),
 * reset(), and the getters/setters. 
 * 
 * General Usage (pseudocode):
 * 
 * createCellList();
 * giveCellsNeighbors();
 * for (cell in cellList){
 * 	cell.pushStateToNeighbors();
 * while(1){
 *	for (cell in cellList){
 * 		cell.pushStateToNeighbors();
 * 	}
 * 	for (cell in cellList){
 * 		cell.reset();
 * 	}
 * }
 * 
 * 
 */
public abstract class Cell {

	public static final int EMPTY = 0;
	public static final double GUARANTEED_UPDATE = 1;
	
    protected ArrayList<Cell> neighbors;
    protected ArrayList<Integer> neighborState = new ArrayList<>();
    protected int state;
    protected boolean shouldUpdate;
    protected boolean hasNotMoved;

    /**
     * Initialize a cell in desired state with certain neighbors
     *
     * @param initialState beginning state of this cell, likely between 0 and 2
     */
    public Cell(int intialState) {
    	state = intialState;
        hasNotMoved = true;
	}


    /**
     *  Check if this cell should update based on its state and the state of its neighbors.
     *  Not to be called by any class that is not a child of Cell
     *
     * @return boolean corresponding to if the cell is going to update
     */
    protected abstract boolean checkUpdate(); //Maybe change to void, boolean likely never used

    /**
     * If a cell is set to update, change it to the specified state.
     * Since each subclass has different state values, this must be done by the children.
     *
     * @param newState the state to which the cell will change
     * @return Boolean corresponding to if the state updated
     */
    protected boolean changeState(int newState){
        state = newState;
        return true;
    }

    /**
     * Push the current state of this cell to all its neighbors
     * This should be called by classes not part of this inheritance
     * hierarchy
     */
    public void pushStateToNeighbors(){
        for(Cell cell : neighbors){
            cell.listenForState(state);
        }
    }

    /**
     * Receive information from surrounding cells about their state
     *
     * @param state the state passed by the neighboring cells
     */
    public void listenForState(int state){
        neighborState.add(state);
    }

    /**
     * Make the cell check the values passed by its neighbors and update based on those
     * Should be called by classes not part of this inheritance hierarchy
     *
     * @return Did the cell update? T/F
     */
    public abstract boolean update() throws Exception;

    /**
     * Reset any fields needed to update to the next round
     */
    public abstract void reset();

    /**
     * Give back the state of this cell
     *
     * @return current cell state
     */
    public int getState(){
        return state;
    }

    /**
     * Update the cell's state to the new value
     *
     * @param s new state value
     */
    public void setState(int s){
        state = s;
    }
    

    /**
     * Check if this cell is empty/dead
     *
     * @return if the cell is empty/dead
     */
    public boolean isEmpty(){
        return state == EMPTY;
    }

    /**
     * Assign neighbors to this cell
     *
     * @param myNeighbors Collection of  Cell object representing this cell's neighbors
     */
    public void setNeighbors(Collection<Cell> myNeighbors){
        neighbors = new ArrayList<>(myNeighbors);
    }
    
    /**
     * Push information about a cell change to all the cells around me
     * 
     * @param newState
     */
    public void pushUpdateToNeighbors(int oldState, int newState){
    	for(Cell c : neighbors){
    		c.listenForUpdate(oldState, newState);
    	}
    }
    
    /**
     * Update the actual states of the neighbors to this cell
     * if a neighbor's state has changed
     * 
     * @param oldState state being overwritten
     * @param newState state of the new neighbor cell
     */
    public void listenForUpdate(int oldState, int newState){
     	neighborState.remove(Integer.valueOf(oldState));
    	neighborState.add(newState);
    }
    
    /**
     * Assign if this cell has already moved. Used to prevent a cell from being update twice
     * if it jumps ahead of the update loop
     * 
     * @param newState Has this cell already moved? T/F
     */
    public void setHasNotMoved(boolean newState){
    	hasNotMoved = newState;
    }
}
