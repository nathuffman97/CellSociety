package CellGroup.Cell;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Matthew Dickson
 *
 * Wa-Tor implementation of the Cell class
 */
public class WaTorCell extends Cell {

	public static final int STARTING_ROUNDS_LIVED = -1;
    public static final int MINNOW = 1;
    public static final int SHARK = 2;
    private int reproduceNumber;
    private int energyRegain;
    private int startingEnergy;
    private int energyLost;

    private int nextState;
    private int roundsLived = STARTING_ROUNDS_LIVED;
    private int energy;
    
    /**
     * WaTor / Predator Prey implementation of the Cell type
     * @see Cell#Cell
     * 
     * @param simSettings Collection of global simulation settings for this sim
     */
    public WaTorCell(int initialState, Collection<Double> simSettings){
        super(initialState);
        nextState = state;
        ArrayList<Double> ss = new ArrayList<>(simSettings);
        startingEnergy = ss.get(0).intValue();
        energy = startingEnergy;
        energyRegain = ss.get(1).intValue();
        energyLost = ss.get(2).intValue();
        reproduceNumber = ss.get(3).intValue();
    }
    /**
     * Used for JUnit tests
     * @param initialState beginning state of this cell
     */
    public WaTorCell(int initialState){
    	super(initialState);
        nextState = state;
        reproduceNumber = 3;
        energyRegain = 2;
        startingEnergy = 2;
        energy = startingEnergy;
        energyLost = 1;
    }

    /**
     *Stub method not used in code execution
     *
     * @return always false
     */
    @Override
    public boolean checkUpdate(){
        return false;
    }

    /**
     *
     * @see Cell#update()
     */
    @Override
    public boolean update(){
        if(hasNotMoved && !isEmpty()){
	    	roundsLived++;
	    	updateSharkEnergy();
	        updateCellLocation();
        }
        return shouldUpdate;
    }

    /**
     * Make the current cell either vacant like it moved or a "new" animal like it reproduced
     */
    private void updateCurrentCell(){
    	if(canReproduce()){
            roundsLived = 0;
            energy = startingEnergy;
        }
        else {
            setState(EMPTY);
        }
    }

    /**
     * Determine if this cell should reproduce this turn. Must be able to move to reproduce
     */
    private boolean canReproduce(){
        return roundsLived >= reproduceNumber;
    }

    /**
     * Change the cell's effective location on the grid
     */
    private void updateCellLocation() {
    	if(isEmpty()) return;
    	for(Cell c : neighbors){
    		WaTorCell cell = (WaTorCell) c;
    		if((cell.isEmpty() && cell.getNext() == Cell.EMPTY) || (cell.isMinnow() && cell.getNext() == WaTorCell.MINNOW && this.isShark()) ){
    			if(cell.isMinnow() && isShark()) energy += energyRegain;
    			copyInfoToCell(cell, state);
    			if(canReproduce()) cell.setRoundsLived(STARTING_ROUNDS_LIVED);
    			updateCurrentCell();
    			break;
    		}
    	}
    }

    /**
     * Take energy from the shark each turn
     */
    private void updateSharkEnergy() {
    	if(isShark()) energy -= energyLost;
	}

    /**
     * Check if the cell is a shark
     *
     * @return is the cell a shark
     */
    protected boolean isShark(){
        return state == SHARK;
    }

    /**
     * Check if the cell is a minnow
     *
     * @return is the cell a minnow
     */
    protected boolean isMinnow(){
        return state == MINNOW;
    }

    /**
     * Kill a shark when it runs out of energy
     */
    private void killShark(){
    	nextState = EMPTY;
        state = EMPTY;
    }

    /**
     * Set the energy value of a shark. Used to copy over information to new cell when moving
     *
     * @param eng energy value to be set
     */
    protected void setEnergy(int eng){
        energy = eng;
    }

    /**
     * Set the number of rounds the cell has been alive. Used to copy over information to a new cell when moving
     *
     * @param rl rounds lived to be set
     */
    protected void setRoundsLived(int rl){
        roundsLived = rl;
    }

    /**
     * Copy information over to a new cell
     *
     * @param c cell to receive the new information
     */
    private void copyInfoToCell(WaTorCell cell, int newState){
    	cell.setState(state);
        cell.setEnergy(energy);
        cell.setRoundsLived(roundsLived);
        cell.setHasNotMoved(false);
    }
    
    /**
     * @see Cell#reset()
     * 
     * Sets the current state to the next state field
     */
    @Override
    public void reset(){
    	setHasNotMoved(true);
    	state =  nextState;
    	if(isShark() && energy <= 0) killShark();
    }
    
    /**
     * @see Cell#setState()
     * 
     * Will change the nextState value for this implementation rather than the actual current state
     */
    @Override
    public void setState(int myState){
    	nextState = myState;
    }
    
    /**
     * Get the state of this cell for the next round
     * 
     * @return the state of this cell in the next round
     */
    public int getNext(){
    	return nextState;
    }
    
    /**
     * Get the number of rounds this cell has lived
     * 
     * @return number of rounds lived by this cell
     */
    public int getRoundsLived(){
    	return roundsLived;
    }
    
    /**
     * Get the energy of this cell
     * 
     * @return current energy
     */
    public int getEnergy(){
    	return energy;
    }
}
