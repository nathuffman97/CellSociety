package CellGroup.Cell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RPSCell extends Cell{
	
	public static final int ROCK = 1;
	public static final int PAPER = 2;
	public static final int SCISSORS = 3;
	private static final Random rn = new Random();

	private int beats;
	private int losesTo;
	private int fadeDist;

	/**
	 * Create a RPS sim cell
	 * 
	 * @param intialState first state of this cell
	 * @param simSettings remainder of a collection parsed from XML to give this unit its behavior.
	 */
	public RPSCell(int intialState, Collection<Double> simSettings) {
		super(intialState);
		beats = setBeats();
		losesTo = setLoses();
		ArrayList<Double> ss = new ArrayList<>(simSettings);
		fadeDist = ss.get(0).intValue();
	}
	
	protected int setBeats(){
		if(state == ROCK) return SCISSORS;
		if(state == PAPER) return ROCK;
		if(state == SCISSORS) return PAPER;
		return -1;
	}
	
	protected int setLoses(){
		if(state == ROCK) return PAPER;
		if(state == PAPER) return SCISSORS;
		if(state == SCISSORS) return ROCK;
		return -1;
	}

	@Override
	protected boolean checkUpdate() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	/**
	 * 
	 * @see Cell#update();
	 * 
	 */
	public boolean update() throws Exception {
		RPSCell cell = (RPSCell) neighbors.get(getRandIndex());
		
		if(cell.isEmpty() && fadeDist > 0 ){ 
			copyTo(cell);
			return true;
			}
		else if(canBeatCell(cell)){
			eatCell();
			cell.getEaten(state);
			return true;
		}
		else if(isBeatenBy(cell)){
			cell.eatCell();
			this.getEaten(cell.getState());
		}
		return false;
	}
	
	private boolean isBeatenBy(RPSCell cell) {
		return cell.getState() == losesTo;
	}

	protected void getEaten(int newPossibleState) {
		fadeDist--;
		if(fadeDist == 0){
			state = newPossibleState;
			setBeats();
		}
	}

	private boolean canBeatCell(RPSCell cell) {
		return cell.getState() == beats;
	}

	private void copyTo(RPSCell cell) {
		cell.setFade(fadeDist - 1);
		cell.setState(state);
		cell.setBeats();
	}
	
	/**
	 * Set the fade distance of this cell. Increasing it allows it to spread further.
	 * 
	 * @param myFadeDist new fade distance.
	 */
	protected void setFade(int myFadeDist) {
		fadeDist = myFadeDist;
	}
	
	/**
	 * 
	 * 
	 */
	protected void eatCell() {
		fadeDist++;
	}

	private int getRandIndex() {
		return rn.nextInt(neighbors.size());
	}

	@Override
	/**
	 * 
	 * @see Cell#reset()
	 * 
	 */
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}
