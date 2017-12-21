package CellGroup.Cell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Matthew Dickson
 *
 * Game of Life implementation of the Cell class
 */
public class LifeCell extends Cell {

    public static final int DEAD = 1;
    public static final int ALIVE = 2;
    
    private int REPRODUCTION_THRESHOLD;
    private int OVERPOP_THRESHOLD;
    private int UNDERPOP_THRESHOLD;
    
    private int newState = 0;

    /**
     * Create the Game of Life implementation of the Cell superclass
     *
     * @see Cell#Cell
     * @param simSettings Collection of global simulation settings for this sim
     */
    public LifeCell(int initialState, Collection<Double> simSettings){
        super(initialState);
        newState = initialState;
        ArrayList<Double> ss = new ArrayList<>(simSettings);
        UNDERPOP_THRESHOLD = ss.get(0).intValue();
        OVERPOP_THRESHOLD = ss.get(1).intValue();
        REPRODUCTION_THRESHOLD = ss.get(2).intValue();
    }
    
    /**
     * Used for JUnit test
     * 
     * @see Cell#Cell(int)
     */
    public LifeCell(int initialState){
    	super(initialState);
    	 UNDERPOP_THRESHOLD = 2;
         OVERPOP_THRESHOLD = 3;
         REPRODUCTION_THRESHOLD = 3;
         newState = initialState;
    }
    
    /**
     * @see Cell#checkUpdate()
     */
    @Override
    public boolean checkUpdate(){
        shouldUpdate = isDying() ^ isBorn();
        return shouldUpdate;
    }

    /**
     *
     * @see Cell#update()
     */
    @Override
    public boolean update(){
        if(checkUpdate()){
            newState = (isDead() ? ALIVE : DEAD);
        }
        return shouldUpdate;
    }

    /**
     * Check if this cell should die
     *
     * @return is the cell alive and one of the death conditions are met? T/F
     */
    private boolean isDying(){
        int aliveNeighbors = getAliveNeighborsCount();
        return isAlive() && (isUnderpopulated(aliveNeighbors) || isOverpopulated(aliveNeighbors));
    }

    /**
     * Check is this cell will be reborn
     *
     * @return is the cell dead and is the birth condition met? T/F
     */
    private boolean isBorn(){
        return isDead() && canReproduce();
    }

    /**
     *
     * @return can the cells around this one reproduce here? T/F
     */
    private boolean canReproduce(){
        return getAliveNeighborsCount() == REPRODUCTION_THRESHOLD;
    }

    /**
     * Check if the cell is being overpopulated by its neighbors
     *
     * @param aliveNeighbors number of living neighbors
     * @return is the cell overpopulated? T/F
     */
    private boolean isOverpopulated(int aliveNeighbors){
        return aliveNeighbors > OVERPOP_THRESHOLD;
    }

    /**
     * Check if the cell is underpopulated
     *
     * @param aliveNeighbors number of living neighbors
     * @return is the cell underpopulated? T/F
     */
    private boolean isUnderpopulated(int aliveNeighbors){
        return aliveNeighbors < UNDERPOP_THRESHOLD;
    }

    /**
     * Parse the states contained in neighborState and find the number alive
     *
     * @return number of living neighbors
     */
    private int getAliveNeighborsCount(){
    	return Collections.frequency(neighborState, ALIVE);
//    	aliveNeighbors = 0;
//    	
//    	for(Cell c : neighbors){
//    		if(c.getState() == LifeCell.ALIVE) aliveNeighbors++;
//    	}
//    	
//        return aliveNeighbors;
    }

    //Check if this cell is alive
    public boolean isAlive(){
        return state == ALIVE;
    }

    //Check if this cell is dead
    private boolean isDead(){
        return state == DEAD;
    }
    
    @Override
    public void reset(){
		pushUpdateToNeighbors(state, newState);
		state = newState;

    }
}
