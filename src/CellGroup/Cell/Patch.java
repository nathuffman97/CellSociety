package CellGroup.Cell;

import java.util.List;

/**
 * 
 * Superclass for patch objects. These object store information about the 
 * ground like pheromone concentrations and sugar/spice quantities, as well as the
 * view from this location on the grid. The can have a state/color gradient based on
 * the values of each substance contained.
 * 
 * @author Matthew Dickson
 */
public abstract class Patch extends Cell{

	public static final int DUMMY_INIT_STATE = -1;
	
	protected int state;
	protected List<Patch> neighbors;
	protected List<Agent> myAgents;
	protected int[] patchValues;
	
	/**
	 * 
	 * Create a patch object. Is a child of cell so CellGroup can treat it the same, so an int of the 
	 * initial state must get passed to it via the constructor and it will decompose that int into the
	 * corresponding values of its patch values.
	 * 
	 * @param state current state of the patch.
	 */
	public Patch(int state){
		super(state);
		patchValues = setValsFromState();
	}
	
	/**
	 * Assign the patch values based on the current state. Will be different based on each implementation.
	 * 
	 * @return the patch values corresponding to the current state
	 */
	protected abstract int[] setValsFromState();
	
	/**
	 * Assign neighbors/view to this patch
	 * 
	 * @param list the list of desired neighbors to be set
	 */
	public void setNeighbors(List<Patch> list){
		neighbors = list;
	}
	
	/**
	 * Assign all the agents currently on this patch. This could theoretically be infinite,
	 * though most simulations put a cap on how many agents can be on a patch. 
	 * 
	 * @param list List of the agents to be assigned to this patch
	 */
	public void setAgents(List<Agent> list){
		myAgents = list;
	}
}
