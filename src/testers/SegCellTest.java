package testers;

import java.util.ArrayList;

import org.junit.Test;

import CellGroup.Cell.Cell;
import CellGroup.Cell.SegCell;
import junit.framework.TestCase;

public class SegCellTest extends TestCase{
	
	 private boolean[][][] boolArray= {
			 {
	    		 {false, true, false, false, false},
	    		 {false, false, false, true, false},
	    		 {true, false, false, true, false},
	    		 {true, false, false, false, false},
	    		 {false, false, false, false, false}
			 }, {
				 {false, false, false, true, false},
				 {true, true, false, false, false},
				 {false, false, false, false, false},
				 {false, false, false, false, false},
				 {true, true, false, false, false}
			 }
     };
	 
	private static SegCell[][] setUpCells(){
	        SegCell[][] cellArray = {
	        		{new SegCell(1), new SegCell(2), new SegCell(1), new SegCell(0), new SegCell(1)},
	        		{new SegCell(1), new SegCell(0), new SegCell(1), new SegCell(2), new SegCell(1)},
	        		{new SegCell(2), new SegCell(2), new SegCell(0), new SegCell(1), new SegCell(0)},
	        		{new SegCell(1), new SegCell(2), new SegCell(0), new SegCell(0), new SegCell(1)},
	        		{new SegCell(0), new SegCell(2), new SegCell(1), new SegCell(1), new SegCell(0)}
        		};
	        
	   
	        
	        
	        setUpNeighbors(cellArray);
	    	return cellArray;
	        }

	private static void setUpNeighbors(SegCell[][] cellArray) {
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
	public void testPreserveEmpty(){
		SegCell[][] cellArray = setUpCells();
		for(int i = 0; i < cellArray.length; i++){
			for(int j = 0; j < cellArray.length; j++){
				cellArray[i][j].pushStateToNeighbors();
			}
		}
		for(int k = 0; k < 10; k++){
			for(int i = 0; i < cellArray.length; i++){
				for(int j = 0; j < cellArray.length; j++){
					try{
						cellArray[i][j].update();
					} catch(Exception e){
						System.out.println(e.getMessage());
					}
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
			
			int empty = 0;
			for(int i = 0; i < cellArray.length; i++){
				for(int j = 0; j < cellArray.length; j++){
					if(cellArray[i][j].isEmpty()) empty++;
				}
			}
			assertTrue(empty == 8);
		}	
	}
	
	@Test
	public void testMoveWhenUphappy(){
		SegCell[][] cellArray = setUpCells();
		for(int k = 0; k < 10; k++){
			for(int i = 0; i < cellArray.length; i++){
				for(int j = 0; j < cellArray.length; j++){
					try{
						int beginState = cellArray[i][j].getState();
						boolean b = cellArray[i][j].checkUpdate();
						cellArray[i][j].update();
						int endState = cellArray[i][j].getState();
						if(b){
							assertTrue(endState == Cell.EMPTY);
						} else assertTrue(beginState == endState);
					} catch(Exception e){
						System.out.println(e.getMessage());
					}
				}
			}
		}
	}
	
	
	@Test
	public void testUnhappy() throws Exception{
		System.out.println("\n\n\n\n\n\n\n\n testing uhappy...\n");
		SegCell[][] cellArray = setUpCells();
		for(int i = 0; i < cellArray.length; i++){
			for(int j = 0; j < cellArray.length; j++){
				cellArray[i][j].pushStateToNeighbors();
			}
		}
		for(int k = 0; k < 2; k++){
			for(int i = 0; i < cellArray.length; i++){
				for(int j = 0; j < cellArray.length; j++){
					System.out.println(i + ", " + j);
					assertTrue(cellArray[i][j].checkUpdate() == boolArray[k][i][j] );
					boolean b = cellArray[i][j].update();
					assertTrue(b == boolArray[k][i][j] );
					if(b != boolArray[k][i][j]) return;
				}
			}
			for(int i = 0; i < cellArray.length; i++){
				for(int j = 0; j < cellArray.length; j++){
					cellArray[i][j].reset();
					System.out.print(cellArray[i][j].getState());
				}
				System.out.println();
			}
		}
	}
	
	@Test
	public void testUnhappyEdgeCase(){
		SegCell[][] cellArray = {
				{new SegCell(0), new SegCell(0), new SegCell(0)},
				{new SegCell(0), new SegCell(1), new SegCell(0)},
				{new SegCell(0), new SegCell(2), new SegCell(0)},
		};
		setUpNeighbors(cellArray);
		for(int i = 0; i < cellArray.length; i++){
			for(int j = 0; j < cellArray.length; j++){
				cellArray[i][j].pushStateToNeighbors();
			}
		}
		assertTrue(cellArray[1][1].checkUpdate());
		assertTrue(cellArray[2][1].checkUpdate());
	}
}
