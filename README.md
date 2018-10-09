# Frozen-Tetris
Tetris (Frozen Edition)

How to play:

The goal of the game is to orient the shapes so that entire lines are filled. These lines are then cleared and depending on how many lines are cleared at once, the score will increase. Use the left and right arrow keys to move the shape sideways. Use the up arrow key to rotate clockwise. Use the down key to move the shape down quickly. Use the space key to drop down the shape instantly. The Pause/Resume button will show the instructions and control keys. The restart button will restart the game.

Classes:

Game: This sets the framework for the buttons, the GameCourt game (where Tetris is actually played), and interacts with GameCourt to pause/resume/restart. The window is not resizable and the background color of the buttons JPanel is set.

GameCourt: This is where all the action is. 
- The game starts by setting a current shape and the next shape to be used. Then, everytime actionPerformed() is called, the shape moves down one line if it can. Once it can no longer move down, the checkLines() method checks through each row of the matrix starting from the bottom and sees if there is a full line. If there is a full line, it shifts all the upper rows down by one, and updates the number of lines cleared accordingly.  It keeps going until it reaches the top of the matrix. After that, it generates a new next shape and uses the previous next shape as the current shape to fall down. If, when it generates a new shape and tries to move it, it immediately is stopped, then the game is over.
- The score is calculated based on how many lines were cleared at once, and the level increases every 10 lines. The timer's delay decreases (so the shapes move faster) everytime the level increases, stopping at level 10.
- The paint function uses the drawUnit() method to draw each individual unit of whichever shape as according to the positions on the 2d array board. It also draws a box around the actual matrix that the game is played on. Beside it, it draws the next shape, a border around that shape, and shows the current level, the lines cleared, and the score, as well as the cute TETRIS Elsa picture. If the game is paused, 
- The board is a 10 by 18 (x by y) array that stores each individual unit of whichever shape. Each position on the array will hold a number 0-7 that corresponds to a type of shape. When the checkLines() method runs, if a line of the array holds 0 (no shape) then it cannot be cleared. Essentially, each falling shape is a pattern of 4 units.

Shape: This contains all the properties of the shapes. There is a record of the coordinates of each type of shape, which has (0,0) as the center of rotation. The class has methods to get various coordinates, generate new random shapes, and set a certain type of shape. The rotate() method rotates clockwise by switching the x coordinate with the negative y coordinate.

More details are commented in the actual code.
