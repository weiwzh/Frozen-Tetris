/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

//Everything game related
@SuppressWarnings("serial")
public class GameCourt extends JPanel implements ActionListener{

  //Setting Game variables
	private static final int COURT_WIDTH = 10;     //size of matrix in terms
	private static final int COURT_HEIGHT = 18;    //of blocks
	private Shape curShape;
	private Shape nextShape;
	private int curX;
	private int curY;
  private boolean hasFallen = false;    //stopped falling yet?
  private boolean isPaused = false;
  private int linesCleared = 0;           
  private static final int[] lineScores = {0,59,113,331,1217};//how many points 
  private int score = 0;   //scored based on how many lines cleared at one time
  private int [][] board;                //keeps track of what shape is where
	private boolean playing = false; // whether the game is running
  private int level = 1;
  private Timer timer;
  private BufferedImage block = null;
  private BufferedImage name = null;
  private BufferedImage intro = null;
  private BufferedImage gameOver = null;

	public GameCourt(Game parent) {
	  
    setFocusable(true);
    
		//Create timer based on level
		timer = new Timer(550-(Math.min(level, 10)-1)*50,this);
		timer.start(); 
    
		//Set shapes
    this.curShape = new Shape(0); 
    this.nextShape = new Shape(0);
    nextShape.setRandomShape();
    
    // Load in images (block decoration, TETRIS + Elsa image)
    try {
      block = ImageIO.read(new File("tetris_block.png"));
      name = ImageIO.read(new File("tetris_name.png"));
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
    }

    //What to do depending on keys pressed
		addKeyListener(new KeyAdapter(){
      public void keyPressed(KeyEvent e) {
        if (!playing || isPaused || curShape.getType() == 0) {
          return;     //don't do anything if ^ happens
        }
        int KC = e.getKeyCode();
        if (KC == KeyEvent.VK_LEFT){
          move(curShape, curX - 1, curY);
        }
        else if (KC == KeyEvent.VK_RIGHT){
          move(curShape, curX + 1, curY);
        }
        else if (KC == KeyEvent.VK_DOWN){ //pushes block down faster
          move(curShape, curX, curY - 2);
        }
        else if (KC == KeyEvent.VK_UP) {
          move(curShape.rotate(), curX, curY);
        }
        else if (KC == KeyEvent.VK_SPACE) { //drops block down directly
          dropDown();
        }
      } 
		});
		
	}

	//Try to move shape
  public boolean move (Shape shape, int x, int y) {
    //Checks each unit of the given shape
    for(int i = 0; i<4; i++){
      int newX = x + shape.getX(i);
      int newY = y - shape.getY(i);
      if (newX < 0 || newY < 0 || newX >= COURT_WIDTH || newY>= COURT_HEIGHT) {
        return false; //Don't move if out of bounds
      }
      if (board[newY][newX] != 0) {
        return false; //Don't move if shape intersects with another shape
      }
    }
    //If the shape can be drawn on screen, make it the current shape
    curShape = shape;
    curX = x;
    curY = y;
    repaint();
    return true;
  }
  
  public boolean isPaused() {
    return isPaused;
  }
  
  public void pause() {
    if (!playing)         //if haven't started, do nothing
        return;

    isPaused = !isPaused;     //change status of paused/not
    if (isPaused) {           //if now paused
        timer.stop();
    } 
    
    else {                  //if now not paused
        timer.start();
    }
    
    repaint();
    // Make sure that this component has the keyboard focus
    requestFocusInWindow();
  }

	/**
	 * (Re-)set the game to its initial state.
	 */
  
	public void restart() {
    playing = true;
    hasFallen = false;
    isPaused = false;
    linesCleared = 0;
    score = 0;
    level = 1;
    board = new int [18][10];
    for (int i = 0; i< 18; i++) {
      for (int j = 0; j<10; j++) {
        board[i][j] = 0;
      }
    }
    newShape();
    timer.start();
		
		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}

	//makes a new shape for nextShape, and makes curShape the original nextShape
  private void newShape() {
    curShape.setShape(nextShape.getType());
    curShape = curShape.flip();       //so that it looks the same as in preview
    nextShape.setRandomShape();       //get new shape and put it in the middle
    curX = 5;
    curY = COURT_HEIGHT - 1 + curShape.minY();

    if (!move(curShape, curX, curY)) {    //if cannot move anymore 
      curShape.setShape(0);               //right from the start, game over
      timer.stop();
      playing = false;
      repaint();
    }
  }

  //drops the entire shape down to the very bottom
  private void dropDown() {
    int y = curY;
    while (y > 0) {
      if (!move(curShape, curX, y - 1)) {
          break;
      }
      y--;
    }
    shapeDrop();
  }

  //moves the shape down one line
  private void oneLineDown() {
    if (!move(curShape, curX, curY - 1)) {
      shapeDrop();
    }
  }
  
  //check if lines are cleared
  private void clearLines() {
    int checkCleared = 0;
    for (int i = COURT_HEIGHT - 1; i >= 0; i--) {
      boolean isFull = true;
      //if there are any empty spaces, false and stop
      for (int j = 0; j < COURT_WIDTH; j++) {
        if (board[i][j] == 0) {
          isFull = false;
          break;
        }
      }
      //if this line is full, shift entire board above the cleared line down one
      if (isFull) {
        checkCleared ++;
        for (int k = i; k < COURT_HEIGHT-1; k++) {
          for (int l = 0; l < COURT_WIDTH; l++) {
            board[k][l] = board[k+1][l];
          }
        }
      }
    }
    //after going through all lines, if any lines are cleared
    if (checkCleared > 0) {
      linesCleared += checkCleared;
      score += lineScores[checkCleared];  
      level =linesCleared/10+1;           //level up every 10 lines cleared
      timer.setDelay(550-(Math.min(level, 10)-1)*50); //max speed reached 
                                                      //at level 10
      hasFallen = true;
    }
  }

  // add shape to board at current position
  private void shapeDrop() {
    for (int i = 0; i < 4; ++i) {
      int x = curX + curShape.getX(i);
      int y = curY - curShape.getY(i);
      board[y][x] = curShape.getType();
    }

    clearLines();        //remove lines 
    repaint();            // so you see block before it disappears
    score+=17;            //score adds every time a block falls
    newShape();
  }
  
  //helper function for loading images
  private void loadImg (String filename) {
    try {
      if (filename.contains("instructions")){
        intro = ImageIO.read(new File (filename));
      }
      else {
        gameOver = ImageIO.read(new File (filename));
      }
    } catch (IOException e) {
        System.out.println("Exception: " + e.getMessage());
    }
  }

  //THE MOST IMPORTANT PAINT FUNCTION!!!
	public void paint(Graphics g) {
		super.paint(g);
		
		//Paints background color
    Color color = new Color (222,238,255);
    g.setColor(color);
    g.fillRect(0, 0, (int) getSize().getWidth(), (int) getSize().getHeight());
    
    //Draws matrix outline
    color = new Color (0,0,0);
    g.setColor(color);
    g.drawRect(0, 0, COURT_WIDTH*20, COURT_HEIGHT*20);
    
    //Drawing preview box and shape		
    g.drawRect(COURT_WIDTH*20 + 15, 0, 120, 90);
    Shape preview = nextShape;
		for (int i = 0; i < 4; i++) {
		  drawUnit(g, COURT_WIDTH*20 + 55 + preview.getX(i)*20,
		              35 - preview.getY(i)*20, preview);
		}
		
		//Drawing level, score, lines cleared, cute name image
		g.setFont(new Font("Calibri", Font.BOLD, 15));
    g.drawString("Level: " + level, COURT_WIDTH*20 + 35, 125);
    g.drawString("Score: " + score, COURT_WIDTH*20 + 35, 155);
    g.drawString("Lines: " + linesCleared, COURT_WIDTH*20 + 35, 185);
    g.drawImage(name, COURT_WIDTH*20 , 200, null);
		
		//Drawing all shapes in the matrix
		for (int i = 0; i < COURT_HEIGHT; i++) {
		  for (int j = 0; j < COURT_WIDTH; j++) {
		    int t = board[COURT_HEIGHT - i - 1][j]; //start from bottom of grid
		    if (t != 0) {
		      drawUnit(g, j * 20, i * 20, new Shape(t));
		    }
		  }
		}
		
		//Draws current shape at current position
		if (curShape.getType() != 0) {
		  for (int i = 0; i < 4; i++) {
		    int x = curX + curShape.getX(i);
		    int y = curY - curShape.getY(i);
		    drawUnit(g, x * 20, (COURT_HEIGHT - y - 1) * 20, curShape);
		  }
		}
		
    //If paused, show the instructions image
    if (isPaused){
      loadImg("tetris_instructions.png");
    }
    else if (!playing){       //if game over, show game over image
      loadImg("tetris_gameOver.png");
    }
    else {              //if neither paused or game over, take off image
      intro = null;
      gameOver = null;
    }
    g.drawImage(intro,0,0,null);
    g.drawImage(gameOver,0,0,null);

	}

	//draws single unit of given shape at given (x,y)
  private void drawUnit(Graphics g, int x, int y, Shape shape) { 
    ////corresponding color - piece
    Color colors[] = { new Color(0, 0, 0), new Color(0, 255, 255), 
          new Color(0, 255, 0), new Color(255, 0, 255), 
          new Color(155, 0, 255), new Color(255, 0, 0), 
          new Color(0, 0, 255), new Color(255, 255, 0)
      };

    //Draws square of given color, put decoration on top, 
    //and then outline in black
    Color color = colors[shape.getType()];
    g.setColor(color);
    g.fillRect(x, y, 20, 20);

    g.drawImage(block, x, y, null);
    
    g.setColor(new Color (0,0,0));
    g.drawRect(x, y, 20, 20);

  }
	
  //Sets dimensions
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH*20 + 150, COURT_HEIGHT*20 + 1);
	}     
	
	
  @Override
  public void actionPerformed(ActionEvent e) {
    if (hasFallen) {
      hasFallen = false;    //if fallen to the ground, get next piece
    }
    else {
      oneLineDown();   //else, move shape one line down
    }
  }  
}
