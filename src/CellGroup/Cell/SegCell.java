package CellGroup.Cell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @author Matthew Dickson
 *
 * Segregation implementation of the Cell class
 */
public class SegCell extends Cell{
    public static final int ONE = 1; //Two groups to be segregated
    public static final int TWO = 2;

    private double HAPPINESS_RATIO;
    
    private LinkedList<SegCell> pastLocations = new LinkedList<>();
    
    private ArrayList<Integer> nextRound = new ArrayList<>();
    private boolean visited = false;
    
    
    /**
     * Create a Segregation implementation of the cell superclass
     * 
     * @see Cell#Cell(int)
     * 
     * @param simSettings Collection of global simulation settings for this sim
     */
    public SegCell(int initialState, Collection<Double> simSettings){
        super(initialState);
        ArrayList<Double> ss = new ArrayList<>(simSettings);
        HAPPINESS_RATIO = ss.get(0);
        setUpPastStates(ss.get(1).intValue());
    }

	private void setUpPastStates(int target) {
		for(int i = 0; i < target; i++){
        	pastLocations.add(null);
        }
	}
    
    /**
     * Used for JUnit test
     * 
     * @see Cell#Cell(int)
     */
    public SegCell(int initialState){
    	super(initialState);
    	HAPPINESS_RATIO = 0.4;
    	setUpPastStates(5);
    }
    

    /**
     *
     * @see Cell#checkUpdate()
     */
    @Override
    public boolean checkUpdate(){
        shouldUpdate = isUnhappy() && !isEmpty();
        return shouldUpdate;
    }

    /**
     *
     * @see Cell#update()
     */
    @Override
    public boolean update() throws Exception{
        if(checkUpdate()) {
            try {
                recursiveNeighborSearch(state, pastLocations);
                pushUpdateToNeighbors(state, Cell.EMPTY);
                state = Cell.EMPTY;
            } catch (Exception e) { // thrown if grid has no open spaces
                throw e;
            }
        }
        return shouldUpdate;
    }

    /**
     * Check if the cell is unhappy because it's too much in the minority
     *
     * @return if the cell's ratio of neighbors like itself is less than the required ratio to be happy
     */
    public boolean isUnhappy(){
        return getNeighborRatio() < HAPPINESS_RATIO;
    }

    /**
     * Check the ration of this's neighbors that are like it
     *
     * @return ratio of cells like this surrounding this
     */
    private double getNeighborRatio() {
    	if(hasNonzeroNeighbors()){
    		return (double) Collections.frequency(neighborState, (Integer) state) / (double) (neighborState.size() - Collections.frequency(neighborState, (Integer) Cell.EMPTY));
    	}
    	else
    	{
    		return 1.1;
    	}
    }

    /**
     * Check if my list of neighbors has an empty spot
     *
     * @return if a neighbor is empty
     */
    private boolean hasEmptyNeighbor(){
        return nextRound.contains(EMPTY);
    }

    /**
     * Conduct a depth-first-search to try and find the closes cell with an empty neighbor
     * Once found, move this to that cell. Thrown an Exception if there are no open cells.
     *
     * @param newState state of this cell to be placed in the found empty cell.
     */
    public boolean recursiveNeighborSearch(int newState, LinkedList<SegCell> oldStates){
        if(hasEmptyNeighbor()){
        	for(Cell c : neighbors){
        		SegCell sc = (SegCell) c;
        		if(sc.isEmpty() && !oldStates.contains(sc)){
                    moveToEmpty(sc, newState);
                    oldStates.pop();
                    oldStates.addLast(sc);
                    return true;
        		}
        	}
        }
        visited = true; //use getter
        for(Cell c : neighbors){
            if(!((SegCell) c).visited){
            	if(((SegCell) c).recursiveNeighborSearch(newState, oldStates)){
                    visited = false;
            		return true;
            	}
            }
        }
        return false;
    }

    /**
     * Move the cell that started the depth first search to the found empty cell
     */
    private void moveToEmpty(SegCell sc, int newState) {
        sc.pushUpdateToNeighbors(sc.getState(), newState);
        sc.setState(newState);
    }
    
    /**
     * Check if a cell has any neighbors that aren't empty
     * 
     * @return does the cell have a neighbor that isn't 0? T/F
     */
    private boolean hasNonzeroNeighbors(){
    	return (Collections.frequency(neighborState, (Integer) Cell.EMPTY) - neighborState.size() < 0.1); 
    }
    
    @Override
    public void reset(){
    	shouldUpdate = false;
    	neighborState.clear();
    	neighborState.addAll(nextRound);
    	hasNotMoved = true;
    }
    
    @Override
    public void listenForState(int newState){
    	neighborState.add(newState);
    	nextRound.add(newState);
    }
    
    @Override
    public void listenForUpdate(int oldState, int newState){
     	nextRound.remove(Integer.valueOf(oldState));
    	nextRound.add(newState);
    }
 }
