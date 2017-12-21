Team 4: Dara Buggay (deb26), Kelly Zhang (kz49)
Team 12: Owen Smith (os29)

UI/UX and XML
PART 1
1. We decided to have a separate class for the UI that controls all of the buttons and sliders the user will be interacting with. Team 4 is planning on having a menu to enable the user to switch games if he/she wants, while we will have that feature in our XML file. In addition, each team has a separate XML class that handles reading in the necessary parameters.
2. Team 4 is considering having a hierarchy within UI that will consist of different windows/stages. There will be an abstract class that is just a Stage with specific size. Then there will be a MenuWindow and SimulationWindow subclasses. SimulationWindow could also be an abstract class that has a subclass for each different type of simulation (fire, segregation, gameoflife, wator, predprey). The transitions and event handlers will all be within these window subclasses.
3. On the contrast, team 12 is just having one class for UI that handles everything, but does not handle transitions between scenes/stages.
4. Both teams 4 and 12 are having the display for the grid and buttons closed. Then any components to change the display or input values can be changed (open) by changing or editing XML files.
5. Exceptions include an invalid XML file or invalid elements within a file. They will be handled by throwing error messages.

PART 2:
1. For both teams 4 and 12, the UI class(es) are pretty independent. However, there is a dependency with CellManager class since the UI has to communicate with the CellManager to obtain the information for the next grid display (new cell statuses). The simulation subclasses are dependent on XML files for those simulations.
2. The UI/UX class dependencies are based on the implementation and behavior of the Simulation subclasses as well as the implementation of the Driver/Game main class.
3. We can minimize dependencies by having the best encapsulation possible and having each class interact (if necessary) in main. 
4. In the Window superclass, we will have a stage that is formatted with default parameters such as size/dimensions, window location, background color, etc. This will also have the grid of cells used in each simulation. The subclasses will have the specifics for that simulation/menu, with buttons, sliders, titles, and text.

PART 3:
1. 
* One: buttons on the UI for changing which simulation is running
* Two: toggle bar to change parameters such as redBlue ratio/percent sharks/probcatch, etc 
* Three: changing parameters after initialization through the XML file (while running, in between steps/when paused, before starting)
* Four: reading simulation specific XML information and displaying those values
* Five: selecting different files in the program without editing code/reset to initialization
2. We are excited about making the UI as intuitive and easy for users as possible. In particular, having different windows in a menu screen where the user can select the simulation is of particular interest to team 4. 
3. We are most worried about tying in the buttons with the grid display, i.e., having a button or toggle change the simulation