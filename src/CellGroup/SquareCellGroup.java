package CellGroup;
/**
 * Inherits from CellGroup, creates rectangles. Determines
 * coordinate points for rectangles and finds neighbors.
 * 
 * @author Natalie Huffman
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import CellGroup.Cell.Cell;
import Game.Game;

public class SquareCellGroup extends CellGroup{
	private int cellX = INNER_SIZE/cellWidth;
	private int cellY = INNER_SIZE/cellHeight;
	
	/**
	 * @see CellGroup#CellGroup
	 * @param configList
	 * @throws IOException
	 */
	public SquareCellGroup(List<Double> configList) throws IOException {
		super(configList);
	}
	
	/**
	 * @see CellGroup#calcPoints
	 */
	@Override
	protected double[] calcPoints(int num){
		double[] d = new double[8];
		initd(num, d);
		
		double[] indices = {d[0]+cellX, d[1], d[0]+cellX, d[1]+cellY, d[0], d[1]+cellY};
		
		for (int x = 0; x < indices.length; x++)
			d[x+2] = indices[x];
		return d;
	}

	private void initd(int num, double[] d){		
		if (num==0){
			d[0] = Game.SIZE-INNER_SIZE;
			d[1] = Game.SIZE-INNER_SIZE;
		}
		else if (num%cellWidth == 0){
			d[0] = grid.get(num-cellWidth).getPoints().get(6);
			d[1] = grid.get(num-cellWidth).getPoints().get(7);
		}
		else{
			d[0] = grid.get(num-1).getPoints().get(2);
			d[1] = grid.get(num-1).getPoints().get(3);
		}
	}
	
	/**
	 * @see CellGroup#neighborReturn
	 */
	@Override
	protected List<Cell> neighborReturn(int num){
		List<Cell> neighbors = new ArrayList<>();
		int leftRight = calcLeftRight(num);
		
		if (neighborConfigState != 1)
			addSides(num, neighbors);
		if (neighborConfigState != 0)
			addDiag(num, neighbors);
		
		if (num%cellWidth==0 || num%cellWidth==cellWidth-1)
		{
			remove(num, leftRight, neighbors);
			if (simType == WATOR || torus == 1)
				toroidal(num, leftRight, neighbors);
		}
		
		return neighbors;

	}
	
	private void addSides(int num, List<Cell> neighbors){
		int[] indices = {num-1, num+1, num-cellWidth, num+cellWidth};
		add(neighbors, indices);
	}
	
	private void addDiag(int num, List<Cell> neighbors){
		int[] indices = {num-cellWidth-1, num-cellWidth+1, num+cellWidth-1, num-cellWidth-1};
		add(neighbors, indices);
	}
	
	private void remove(int num, int leftRight, List<Cell> neighbors){
		int[] indices = {num+leftRight, num-cellWidth+leftRight, num+cellWidth+leftRight};
		remove(neighbors, indices);
	}
}
