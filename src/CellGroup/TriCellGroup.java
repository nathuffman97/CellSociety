package CellGroup;

/**
 * Inherits from CellGroup, creates triangles. Determines
 * coordinate points for triangles and finds neighbors.
 * 
 * This class, along with HexCellGroup, demonstrates good super/sub class split-up.
 * The super class is not necessary here to understand how this class works, because
 * these subclasses deal only with the elements of the super CellGroup class that have
 * to do with the Polygons. Therefore I believe they and their function are understandable
 * on their own. The HexCellGroup class comment elaborates on why these classes 
 * represent flexible code and why it would be easy to add new features to these sections
 * of the project.
 * 
 * @author Natalie Huffman
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import CellGroup.Cell.Cell;
import Game.Game;

public class TriCellGroup extends CellGroup{
    private double triWidth = (double)INNER_SIZE*2/(2+(double)cellWidth);
    private double triHeight = (double)INNER_SIZE/cellHeight;
	
    /**
     * @see CellGroup#CellGroup
     * @param configList
     * @throws IOException
     */
	public TriCellGroup(List<Double> configList) throws IOException {
		super(configList);
	}
	
	/**
	 * @see CellGroup#calcPoints
	 */
	@Override
	protected double[] calcPoints(int num){
		int i = iFromNum(num);
		int j = jFromNum(num);
		double[] d = initd(num, i, j);
		int upDown = isUp(i,j)? -1:1;
		
		d[2] = d[0] - upDown*triWidth/2;
		d[3] = d[1] - upDown*triHeight;
		d[4] = d[0] + upDown*triWidth/2;
		d[5] = d[3];
		
		return d;
	}
	
	/**
	 * Initializes the starting point of the triangle, off which all the others are based
	 * 
	 * @param num		the index number of the particular triangle
	 * @param i			the row number corresponding to the index number
	 * @param j			the column number corresponding to the index number
	 * @return			the first two of the 12 coordinate points
	 */
	private double[] initd(int num, int i, int j){	
		double[] d = new double[6];
		
		if (i == 0 && j == 0)
		{
			d[0] = Game.SIZE-INNER_SIZE+triWidth/2;
			d[1] = Game.SIZE-INNER_SIZE;
		}
		else if (j == 0){
			double offset = isUp(i,j)? 0:(2*triHeight);
			
			d[0] = grid.get(num-cellWidth).getPoints().get(0);
			d[1] = grid.get(num-cellWidth).getPoints().get(1)+ offset;
		}
		else{
			if (isUp(i,j)){
				d[0] = grid.get(num-1).getPoints().get(4);
				d[1] = grid.get(num-1).getPoints().get(5);
			}
			else{
				d[0] = grid.get(num-1).getPoints().get(2);
				d[1] = grid.get(num-1).getPoints().get(3);
			}
		}
		
		return d;
	}	
	
	/**
	 * @see CellGroup#neighborReturn
	 */
	@Override
	protected List<Cell> neighborReturn(int num) {
		List<Cell> neighbors = new ArrayList<Cell>();
		int upDown = isUp(iFromNum(num), jFromNum(num))? 1:-1;
		int leftRight = calcLeftRight(num);
		
		if (neighborConfigState != 1)
			addSides(num, upDown, neighbors);
		if (neighborConfigState != 0)
			addDiag(num, upDown, neighbors);
		
		if (num%cellWidth==0 || num%cellWidth==cellWidth-1)
		{
			remove(num, upDown, leftRight, neighbors);
			if (simType == WATOR || torus == 1)
				toroidal(num, leftRight, neighbors);
		}
		
		return neighbors;
	}
	
	/**
	 * Adds all neighbors that touch the triangle on a side
	 * 
	 * @param num			the index number of the particular triangle
	 * @param upDown		whether the triangle points up or down
	 * @param neighbors		the list of neighbors to add to
	 */
	private void addSides(int num, int upDown, List<Cell> neighbors){
		int[] indices = {num-1, num+1, num-upDown*cellWidth};
		add(neighbors, indices);
	}
	
	/**
	 * Adds all neighbors that touch the triangle on a diagonal
	 * 
	 * @param num			the index number of the particular triangle
	 * @param upDown		whether the triangle points up or down
	 * @param neighbors		the list of neighbors to add to
	 */
	private void addDiag(int num, int upDown, List<Cell> neighbors){
		int[] indices = {num-2, num+2, num+upDown*cellWidth, num+upDown*cellWidth-1, num+upDown*cellWidth-2, num+upDown*cellWidth+1, num+upDown*cellWidth+2, num-upDown*cellWidth-1, num-upDown*cellWidth+1};
		add(neighbors, indices);
	}
	
	/**
	 * Removes all invalid neighbors for triangles on an edge of the grid,
	 * whose neighbors in an array do not physically touch them in grid
	 * format 
	 * 
	 * @param num			the index number of the particular triangle
	 * @param upDown		whether the triangle points up or down
	 * @param leftRight		whether the triangle is on the left side or the right side of the grid
	 * @param neighbors		the list of neighbors to add to
	 */
	private void remove(int num, int upDown, int leftRight, List<Cell> neighbors){
		int[] indices = {num+leftRight, num+leftRight*2, num-upDown+leftRight, num+upDown+leftRight, num+upDown+leftRight*2};
		remove(neighbors, indices);			
	}
	
	/**
	 * @param i		the row number corresponding to the index number
	 * @param j		the column number corresponding to the index number
	 * @return		whether the triangle points up or down
	 */
	private boolean isUp(int i, int j){
		return i%2==0 && j%2==0 || i%2==1 && j%2==1;
	}

}
