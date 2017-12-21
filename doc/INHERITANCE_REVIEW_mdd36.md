Team 4:

Aaron Paskin (anp36)  
Dara Buggay (bed26)  
Kelly Zhang (kz49)

Team 12:

Matthew Dickson (mdd36)  
Natalie Huffman (nch15)  
Owen Smith (os29)  

As both Nat and I are running the back end, we had a three person coversation with Aaron about how each team is implementing the backend and kept a single Google Doc of the conversation found [here](https://docs.google.com/document/d/1cg3pmMlm3ijRz9KDcn6br4Nl6lsXQEH8Ew2FEVB5rKM/edit?usp=sharing). Since both .md files for nch15 (Natalie Huffman) and me are a copy of the single Google Doc, they will close to if not exactly identical.  

## Part 1
1. Teams 4 and 12 both encapsulate CellGroup/CellManager and Cell classes, which generically operate on and update Cells throughout a given simulation of any type. Future developers are thus able to create new simulation types that can use CellGroup/CellManager and Cell.

2. Team 4 implements inheritance at the cell group level, but both teams have a hierarchy of cell classes. In the active cell manager approach taken by group 4, cells are called on by the cell manager to update themselves to a state, while the cell hierarchy approach of group 12 has the cells check for updates and update themselves.

3. Team 12 has the CellGroup class closed, since it should be able to manage Cells of any implementation with no differences. The Cell class, on the other hand, is open, since all of the cell subclasses inherit methods from the Cell class. For Team 4, the CellManager class is open, since it changes based on the simulation being implemented, and the Cell class, which has subclasses related to implementation, is also open.

4. There is the potential for NullPointerExceptions when getting the neighbors of a cell, especially if the cell is an edge cell and has less than eight neighbors. It is also possible that a simulation might try to move a Cell to an empty location but can not find one because no empty locations exist in the grid. Additionally, the CellGroup/CellManager classes can throw an exception if the arguments received from the XML parsing class are invalid for the particular simulation.

5. In the Group 4 approach, new simulations are easy to implement by just extending the CellManager and Cell classes, which could make changing global update rules by updating one location. Conversely, Group 12's approach allows for different shaped simulations and encapsulated behavior in the Cell's actions.

## Part 2

1. The back end depends on the UI and front end to accurately and reliably display the data. Additionally, the CellGroup/CellManager classes rely on the Cell classes (and vice versa), as they rely on each others' methods.

2. These dependencies are independent of the implementation and behavior in the front end classes, but dependent for the CellGroup/CellManager and Cell classes.

3. We minimize the dependencies between back end and front end by delegating the responsibilities of each to two separate inheritances, each used by a single driver class.

4. Team 4 handles the operations on the cells, including checking neighbors and updating statuses, in its CellManager class (and its children), while Team 12 handles these operations in the Cell class (and its children). Team 12's CellGroup class uses the Cell as a tool for making updates rather than just as a data storage unit.

## Part 3

1.  
a) Make sharks/minnows reproduce  
b) User hits reset button  
c) Detect end of simulation  
d) Move minority cell to new empty cell  
e) Initialize the cells

2. Team 12 is excited about the recursive search for neighbors; Team 4 is excited about implementing the specific rules of different simulations.

3. Team 12 is not looking forward to creating an algorithm to create a list of neighbors; Team 4 is not excited to resolve the bugs that come with passing the back end data to the UI.
