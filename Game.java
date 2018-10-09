/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	public void run() {
		// Top-level frame in which game components live
	  final JFrame frame = new JFrame("TETRIS (FROZEN ED.)");
		frame.setLocation(600, 100);
    frame.setResizable(false);
    
		// Main playing area
		final GameCourt court = new GameCourt(this);
		frame.add(court, BorderLayout.CENTER);

		// Setting up control_panel with color
		final JPanel control_panel = new JPanel();
    Color color = new Color (222,238,255);    
		control_panel.setBackground(color);
		frame.add(control_panel, BorderLayout.NORTH);
		
		//Restart button
    final JButton restart = new JButton("Restart");
    restart.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println("RESTART DAMMIT");
        court.restart();
      }
    }); 
    control_panel.add(restart);
 
    // Pause button
    final JButton pause = new JButton("Pause");
    pause.addActionListener(new ActionListener() {
      @SuppressWarnings("deprecation")
      public void actionPerformed(ActionEvent e) {
        //if already paused, change label to Resume
        if(!court.isPaused()){
          pause.setLabel("Resume");
          court.pause();
        }
        //if not, change label back to Pause
        else{
          pause.setLabel("Pause");
          court.pause();
        }
      }
    });
    control_panel.add(pause);

		// Put the frame on the screen
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// Start game
		court.restart();
	}

	/*
	 * Main method run to start and run the game Initializes the GUI elements
	 * specified in Game and runs it IMPORTANT: Do NOT delete! You MUST include
	 * this in the final submission of your game.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
}
