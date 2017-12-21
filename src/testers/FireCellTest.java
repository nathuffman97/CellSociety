package testers;

import java.util.ArrayList;

import org.junit.Test;

import CellGroup.Cell.Cell;
import CellGroup.Cell.FireCell;
import junit.framework.TestCase;

public class FireCellTest extends TestCase{
	
	public static double probCatch = 0.95;

	
	private static FireCell[][] setUpCells(){
        FireCell[][] cellArray = {{new FireCell(1, probCatch), new FireCell(2, probCatch), new FireCell(1,probCatch), new FireCell(0,probCatch), new FireCell(1,1)},
        		{new FireCell(1, probCatch), new FireCell(0, probCatch), new FireCell(1, probCatch), new FireCell(2, probCatch), new FireCell(1, probCatch)},
        		{new FireCell(2, probCatch), new FireCell(2, probCatch), new FireCell(2, probCatch), new FireCell(1, probCatch), new FireCell(2, probCatch)},
        		{new FireCell(1, probCatch), new FireCell(2, probCatch), new FireCell(2, probCatch), new FireCell(2, probCatch), new FireCell(1, probCatch)},
        		{new FireCell(2, probCatch), new FireCell(2, probCatch), new FireCell(1, probCatch), new FireCell(1, probCatch), new FireCell(2, probCatch)}};
        
        setUpNeighbors(cellArray);
    	return cellArray;
        }

	private static void setUpNeighbors(FireCell[][] cellArray) {
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
	public void testSpread(){
		FireCell[][] cellArray = setUpCells();
		for(int k = 0; k < 10; k++){
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
					System.out.print(cellArray[i][j].getState());
				}
				System.out.println();
			}
			System.out.println("\n");
		}
	}
	
	@Test
	public void testEmptyWontBurn(){
		FireCell[][] cellArray = new FireCell[3][3];
		for(int i = 0; i < cellArray.length; i++){
			for(int j = 0; j < cellArray.length; j++){
				cellArray[i][j] = new FireCell(2, probCatch);
			}
		}
		cellArray[0][0] = new FireCell(1, probCatch);
		cellArray[1][1] = new FireCell(0, probCatch);
		setUpNeighbors(cellArray);
		for(int k = 0; k < 10; k++){
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
			
			assertTrue(cellArray[1][1].getState() == 0);
		}
	}
}
