package CellGroup.Cell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;


/**
 * @author Matthew Dickson
 *
 * Fire implementation of the Cell class
 */
public class FireCell extends Cell {

    public static final int ON_FIRE = 1;
    public static final int UNBURNED = 2;
    private static final Random rand = new Random();
    
    private double probCatch;
    private int oldState = 0;
    
    /**
     * Create the Fire implementation of the Cell superclass
     *
     * @see Cell#Cell
     * @param simSettings Collection of global simulation settings for this sim
     */
    public FireCell(int initialState, Collection <Double> simSettings){
        super(initialState);
        ArrayList<Double> ss = new ArrayList<>(simSettings);
        probCatch = ss.get(0);
    }
    
    /**
     * Used for JUnit tests
     * 
     * @see Cell#Cell(int)
     * @param prob probCatch value, can be determined by the tester
     */
    public FireCell(int initialState, double prob){
    	super(initialState);
    	probCatch = prob;
    }

    /**
     * @see Cell#checkUpdate()
     */
    @Override
    protected boolean checkUpdate(){
        shouldUpdate = (anyNeighborIsOnFire() && fireCanSpread()) || isOnFire();
        return shouldUpdate;
    }

    /**
     * Check to see if any adjacent cells are burning
     *
     * @return If the collection of neighbor states has a value corresponding to being on fire.
     */
    private boolean anyNeighborIsOnFire(){
        return neighborState.contains(ON_FIRE);
    }

    /**
     * Check if this cell can burn and if the fire successfully spreads based on probCatch
     *
     * @return boolean corresponding to if the fire spreads to this cell
     */
    private boolean fireCanSpread(){
        return state == UNBURNED && rand.nextDouble() <= probCatch;

        
    }

    /**
     * Check if this cell is on fire
     *
     * @return is this cell on fire? T/F
     */
    private boolean isOnFire(){
        return state == ON_FIRE;
    }

    /**
     * @see Cell#update()
     */
    @Override
    public boolean update(){
        if(checkUpdate()){
        	oldState = state;
            state = (state == UNBURNED ? ON_FIRE : EMPTY);
        }
        return shouldUpdate;
    }
    
    @Override
    public void reset(){
    	if(shouldUpdate){
    		pushUpdateToNeighbors(oldState, state);
    	}
    }
}
