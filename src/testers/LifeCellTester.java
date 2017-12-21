package testers;

import java.util.ArrayList;

import org.junit.Test;

import CellGroup.Cell.Cell;
import CellGroup.Cell.LifeCell;
import junit.framework.TestCase;

public class LifeCellTester extends TestCase{
		
	private static LifeCell[][] setUpCells(){
        LifeCell[][] cellArray = {
        		{new LifeCell(LifeCell.DEAD), new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.DEAD), new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.DEAD)},
        		{new LifeCell(LifeCell.DEAD), new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.DEAD)},
        		{new LifeCell(LifeCell.DEAD), new LifeCell(LifeCell.DEAD), new LifeCell(LifeCell.DEAD), new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.DEAD)},
        		{new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.DEAD), new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.DEAD)},
        		{new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.DEAD), new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.DEAD)}};
        
        setUpNeighbors(cellArray);
    	return cellArray;
        }

	private static void setUpNeighbors(LifeCell[][] cellArray) {
		for(int i = 0; i < cellArray.length; i++) {
            for (int j = 0; j < cellArray.length; j++) {
                ArrayList list = new ArrayList();
                if(i - 1 >= 0){
                    list.add(cellArray[i-1][j]);
                }
                if(i + 1 < cellArray.length){
                    list.add(cellArray[i+1][j]);
                }
                if(j-1 >= 0){
                    list.add(cellArray[i][j-1]);
                }
                if(j + 1 < cellArray.length){
                    list.add(cellArray[i][j+1]);
                }
                cellArray[i][j].setNeighbors(new ArrayList<Cell>(list));
            }
        }
	}
	
	@Test
	public void testBirth(){
		LifeCell[][] cellArray = {
				{new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.ALIVE)}, 
			    {new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.DEAD), new LifeCell(LifeCell.ALIVE)},
			    {new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.DEAD), new LifeCell(LifeCell.ALIVE)}
				};
		setUpNeighbors(cellArray);
		
		for(int i = 0; i < cellArray.length; i++){
			for(int j = 0; j < cellArray.length; j++){
				cellArray[i][j].pushStateToNeighbors();
			}
		}
		
		for(int i = 0; i < cellArray.length; i++){
			for(int j = 0; j < cellArray.length; j++){
				cellArray[i][j].update();
			}
		}
		for(int i = 0; i < cellArray.length; i++){
			for(int j = 0; j < cellArray.length; j++){
				cellArray[i][j].reset();
			}
		}
		System.out.println(cellArray[1][1].getState());
		assertTrue(cellArray[1][1].getState() == LifeCell.ALIVE);
	}
	
	@Test
	public void testOverpop(){
		LifeCell[][] cellArray = {
				{new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.ALIVE)}, 
				{new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.ALIVE)}, 
				{new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.ALIVE)}
		};
		
		setUpNeighbors(cellArray);
		
		for(int i = 0; i < cellArray.length; i++){
			for(int j = 0; j < cellArray.length; j++){
				cellArray[i][j].pushStateToNeighbors();
			}
		}
		
		for(int i = 0; i < cellArray.length; i++){
			for(int j = 0; j < cellArray.length; j++){
				cellArray[i][j].update();
			}
		}
		for(int i = 0; i < cellArray.length; i++){
			for(int j = 0; j < cellArray.length; j++){
				cellArray[i][j].reset();
			}
		}
		assertTrue(cellArray[1][1].getState() == LifeCell.DEAD);
	}
	
	@Test
	public void testUnderPop(){
		LifeCell[][] cellArray = {
				{new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.DEAD), new LifeCell(LifeCell.ALIVE)}, 
				{new LifeCell(LifeCell.DEAD), new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.DEAD)}, 
				{new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.ALIVE), new LifeCell(LifeCell.ALIVE)}
		};
		
		setUpNeighbors(cellArray);
		
		for(int i = 0; i < cellArray.length; i++){
			for(int j = 0; j < cellArray.length; j++){
				cellArray[i][j].pushStateToNeighbors();
			}
		}
		
		for(int i = 0; i < cellArray.length; i++){
			for(int j = 0; j < cellArray.length; j++){
				cellArray[i][j].update();
			}
		}
		for(int i = 0; i < cellArray.length; i++){
			for(int j = 0; j < cellArray.length; j++){
				cellArray[i][j].reset();
			}
		}
		assertTrue(cellArray[1][1].getState() == LifeCell.DEAD);
	}
	
	@Test
	public void testLongTerm(){
		LifeCell[][] cellArray = setUpCells();
		
		for(int i = 0; i < cellArray.length; i++){
			for(int j = 0; j < cellArray.length; j++){
				cellArray[i][j].pushStateToNeighbors();
			}
		}
		for(int k = 0; k < 5; k++){
			for(int i = 0; i < cellArray.length; i++){
				for(int j = 0; j < cellArray.length; j++){
					cellArray[i][j].update();
				}
			}
			for(int i = 0; i < cellArray.length; i++){
				for(int j = 0; j < cellArray.length; j++){
					cellArray[i][j].reset();
					System.out.print(cellArray[i][j].getState());
				}
				System.out.println();
			}
			System.out.println();
		}
	}
}


