package CellGroup;
/**
 * This class inherits from CellGroup, and creates hexagons. It determines
 * coordinate points for hexagons and finds neighbors. 
 * 
 * This class, along with the other subCellGroup classes, demonstrates encapsulation 
 * and good super/sub class implementation. An examination of this class and the other 
 * subCellGroup classes shows that they perform the same operation, but with different
 * numbers, that is, the classes are structured in the same way. This consistency of 
 * code makes it easy to implement additional Polygon subclasses, and it makes the code 
 * readable and easily understandable. It also serves to emphasize that these two classes
 * are serving the same function, except with different shapes. Additionally, the setup
 * of the neighborReturn, addSides, and remove methods demonstrate how easy it would be
 * to add additional edge typing (sphere or Klein bottle or cross-surface, etc) or neighbor
 * configuration (just horizontal, just vertical, just knights, etc).
 * 
 * @author Natalie Huffman
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import CellGroup.Cell.Cell;
import Game.Game;
import Util.UI;
import Util.XMLException;

public class HexCellGroup extends CellGroup{
	private double hexWidth = INNER_SIZE/(1+(double)3*cellWidth);
	private double hexHeight = INNER_SIZE/(2*cellHeight);
	
	/**
	 * Constructor, calls super class 
	 * 
	 * @param configList	contains all specifications from the XML file
	 * @throws IOException	if neighbor state specifications is "diagonal", 
	 * 						because hexagons do not have the equivalent of a
	 * 						hexagon on their diagonal
	 */
	public HexCellGroup(List<Double> configList) throws IOException {
		super(configList);
		if(neighborConfigState == 1) UI.exceptionThrower(new XMLException("Hexagons have no diagonals, nothing will interact or spread!"));
	}
	
	
	/**
	 * Calculates the twelve x and y coordinate points of a hexagon of a given index
	 * 
	 * @param num		the index number of the particular hexagon
	 */
	@Override
	protected double[] calcPoints(int num){
		double[] d = initd(num);
		
		double[] indices = {d[0]+hexWidth, d[1]-hexHeight, d[0]+3*hexWidth, d[1]-hexHeight, d[0]+4*hexWidth, d[1], d[0]+3*hexWidth, d[1]+hexHeight, d[0]+hexWidth, d[1]+hexHeight};
		
		for (int k = 0; k < indices.length; k++){
			d[k+2] = indices[k];
		}
		
		return d;
	}
	
	/**
	 * Initializes the starting point of the hexagon, off which all the others are based
	 * 
	 * @param num		the index number of the particular hexagon
	 * @return			the first two of the 12 coordinate points
	 */
	private double[] initd(int num){
		double[] d = new double[12];
		
		if (num == 0)
		{
			d[0] = Game.SIZE-INNER_SIZE;
			d[1] = Game.SIZE-INNER_SIZE+hexHeight;
		}		
		else if (num%cellWidth==0){
			d[0] = grid.get(num-cellWidth).getPoints().get(0);
			d[1] = grid.get(num-cellWidth).getPoints().get(1)+2*hexHeight;
		}
		else if ((num%cellWidth)%2 == 1){
			d[0] = grid.get(num-1).getPoints().get(8);
			d[1] = grid.get(num-1).getPoints().get(9);
		}
		else {
			d[0] = grid.get(num-1).getPoints().get(4);
			d[1] = grid.get(num-1).getPoints().get(5);
		}
		
		return d;
	}

	/**
	 * Returns all the neighbors of a hexagon of a given index
	 * 
	 * @param num		the index number of the particular hexagon
	 * @return			a list of all that hexagon's neighbors
	 */
	@Override
	protected List<Cell> neighborReturn(int num){
		List<Cell> neighbors = new ArrayList<>();
		int evenOdd = jFromNum(num)%2==0? -1:1;
		int leftRight = calcLeftRight(num);
		
		if (neighborConfigState != 1)
			addSides(num, evenOdd, neighbors);
		
		if (num%cellWidth==0 || num%cellWidth==cellWidth-1)
		{
			remove(num, evenOdd, leftRight, neighbors);
			if (simType == WATOR || torus == 1)
				toroidal(num, leftRight, neighbors);
		}
		
		return neighbors;
	}
	
	/**
	 * Adds all neighbors that touch the hexagon on a side
	 * 
	 * @param num			the index number of the particular hexagon
	 * @param evenOdd		whether the hexagon is at an even or odd position in the row
	 * @param neighbors		the list of neighbors to add to
	 */
	private void addSides(int num, int evenOdd, List<Cell> neighbors){
		int[] indices = {num-1, num+1, num-cellWidth, num+cellWidth, num+evenOdd*cellWidth-1, num+evenOdd*cellWidth+1};
		add(neighbors, indices);
	}
	
	/**
	 * Removes all invalid neighbors for hexagons on an edge of the grid,
	 * whose neighbors in an array do not physically touch them in grid
	 * format 
	 * 
	 * @param num			the index number of the particular hexagon
	 * @param evenOdd		whether the hexagon is at an even or odd position in the row
	 * @param leftRight		whether the hexagon is on the left side or the right side of the grid
	 * @param neighbors		the list of neighbors to add to
	 */
	private void remove(int num, int evenOdd, int leftRight, List<Cell> neighbors){
		int[] indices = {num+leftRight, num+evenOdd*cellWidth+leftRight};
		remove(neighbors, indices);
	}

}
