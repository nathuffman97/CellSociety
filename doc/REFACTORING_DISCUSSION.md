Matthew Dickson, Natalie Huffman, Owen Smith

# Duplication Refactoring

We chose to refactor CellGroup, HexCellGroup, TriCellGroup, and SquareCellGroup because the constructors and some of the methods in each of them 
were very similar. For example, the drawCells method is identical between HexCellGroup and TriCellGroup are identical. 
As a result, we decided to make SquareCellGroup a polygon as well so that we can have methods common to all three different shapes of cells. We put 
these common methods and constructors in the CellGroup class so that all of its children can inherit those methods. 

We also chose to refactor SquareCellGroup and HexCellGroup when testing for edge cases. There are multiple condition statements that check to see if 
certain indices of cells in the cell array in CellGroup should be added to the list of neighbors for these cells at the edge. Depending on where this 
cell is in the grid, there were different conditional statements used. We decided instead to add all of the normal neighboring cells and then remove the 
ones that wouldn't fall into the grid because there would be fewer conditional statements being used, and the code within them was less alike.

# Checklist Refactoring

We chose to make the Util class static instead of creating a new Util class each time. This is because the Util class is really only used to return a 
list of parameters that the CellGroup class will use to make the game, and there are no inputs to its constructors. The Util class will always be the 
same because its parser method works the same. 

In addition, we fixed the use of concrete lists. There were only three cases in which there were specific lists instead of general lists, so we quickly 
changed those. 