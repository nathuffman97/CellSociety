package testers;

import java.util.ArrayList;

import org.junit.Test;

import CellGroup.Cell.Cell;
import CellGroup.Cell.WaTorCell;
import junit.framework.TestCase;

public class WaTorCellTest extends TestCase{
	
	
	private WaTorCell[][] setUpCells(){
		WaTorCell[][] cellArray = {
				{new WaTorCell(1), new WaTorCell(1), new WaTorCell(2), new WaTorCell(1), new WaTorCell(0)},
				{new WaTorCell(0), new WaTorCell(1), new WaTorCell(1), new WaTorCell(0), new WaTorCell(0)},
				{new WaTorCell(1), new WaTorCell(1), new WaTorCell(2), new WaTorCell(2), new WaTorCell(0)},
				{new WaTorCell(2), new WaTorCell(1), new WaTorCell(2), new WaTorCell(1), new WaTorCell(1)},
				{new WaTorCell(1), new WaTorCell(0), new WaTorCell(0), new WaTorCell(2), new WaTorCell(2)},
		};
		setUpNeighbors(cellArray);
		return cellArray;
	}
	
	
	private void setUpNeighbors(Cell[][] cellArray) {
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
	public void testMovement(){
		WaTorCell[][] cellArray = setUpCells();
		
		
	    	for(int k = 0; k < 4; k++){	    		
		    	int shark = 0;
		    	for(int i = 0; i < cellArray.length; i++){
		    		for(int j = 0; j < cellArray.length; j++){
		    			try{
		    				cellArray[i][j].update();
		    			} catch(Exception e){e.getMessage();}
		    		}		    		
		    	}
		    	
		    	System.out.println();
		    	
		    	for(int i = 0; i < cellArray.length; i++){
		    		for(int j = 0; j < cellArray.length; j++){
		    				cellArray[i][j].reset();
		    				if(cellArray[i][j].getState() == WaTorCell.SHARK) shark++;
		    				System.out.print(cellArray[i][j].getState());
	    			}
		    		System.out.println();
	    		}
		    	if(k == 0) assertTrue (shark == 7);
		    	if(k == 1) assertTrue(shark == 4);
		    	if(k == 2) assertTrue(shark == 4);
		    	if(k == 3) assertTrue(shark == 6);
		    	System.out.println();
    	}
	}
	
	
	@Test
	public void testDeath(){
		WaTorCell[][] myCell = {
				{new WaTorCell(2), new WaTorCell(0)},
				{new WaTorCell(0), new WaTorCell(0)}
		};
		
		setUpNeighbors(myCell);
		for(int k = 0; k < 4; k++){
			for(int i = 0; i < myCell.length; i++){
	    		for(int j = 0; j < myCell.length; j++){
	    			myCell[i][j].update();
	    		}
    		}
			
			for(int i = 0; i < myCell.length; i++){
	    		for(int j = 0; j < myCell.length; j++){
	    			myCell[i][j].reset();
	    		}
	    		System.out.println();
    		}
			System.out.println();
		}
		int shark = 0;
		for(int i = 0; i < myCell.length; i++){
    		for(int j = 0; j < myCell.length; j++){
    			if(myCell[i][j].getState() == WaTorCell.SHARK) shark++;
    		}
		}
		assertTrue(shark == 0);
	}
	
	@Test
	public void testReproduce(){
		WaTorCell[][] cellArray = {
				{new WaTorCell(1), new WaTorCell(0)},
				{new WaTorCell(0), new WaTorCell(0)}
		};
		setUpNeighbors(cellArray);
		
		for(int k = 0; k < 4; k++){
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
		}
		
		int fish = 0;
		for(int i = 0; i < cellArray.length; i++){
			for(int j = 0; j < cellArray.length; j++){
				if(cellArray[i][j].getState() == WaTorCell.MINNOW) fish++;
			}
			System.out.println();
		}
		assertTrue(fish == 2);
	}
}