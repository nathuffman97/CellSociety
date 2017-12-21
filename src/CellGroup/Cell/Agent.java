package CellGroup.Cell;


/**
 * Superclass for agent objects, ie things that can move around
 * and interact with underlying patches by consuming sugar or 
 * placing pheromones.
 * 
 * @author Matthew Dickson
 *
 */
public abstract class Agent {
	
	protected int state;
	protected boolean canMove = true;
	
	/**
	 * Constructor for agents. Only takes in state, as state is the only
	 * shared feature among agents
	 * 
	 * @param myState
	 */
	public Agent(int myState){
		state = myState;
	}
	
	/**
	 * Get the state of this agent
	 * 
	 * @return current state
	 */
	public int getState(){
		return state;
	}
	
	/**
	 * Get the boolean value of if this agent can make a move
	 * 
	 * @return the boolean value of if this agent can make a move
	 */
	protected boolean getCanMove(){
		return canMove;
	}
	
	/**
	 * Check if two agents are the same
	 * 
	 * @return if the object passed equals this object
	 */
	public boolean equals(Object o){
		return (o!= null && o instanceof Agent && (
				(Agent) o).getState() == state) &&
				((Agent) o).getCanMove() == canMove;
	}
}
