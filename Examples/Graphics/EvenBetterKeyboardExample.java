/* This class contains even more improvements over BetterKeyboardExample.java.

   Requires Square.java (or at least Square.class), provided to you, to work.

   We once again change the way input works, to give us more control over how
   we respond to those inputs within the main loop.
   
   To see a big difference, try doing the following simple experiment with both
   this program and BetterKeyboardExample.java:
     Hold down the RIGHT arrow key on the keyboard.
     Keeping the RIGHT key held down, keep pressing and releasing the LEFT
     arrow key and observe the behavior of the square.
     Which program does a better job of producing the expected results?
   
   See AccelerationExample.java for a few small changes to this paradigm that
   allow us to model physics at a more accurate level (introducing
   acceleration)
   
   After that, take a look at CollisionExample to see how we can add collision
   with walls and onscreen entities.
 */

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener; 

public class EvenBetterKeyboardExample extends JPanel implements KeyListener
{
  static final int MSPF = 20; // Amount of time each frame should take

  Square ourSquare;
  
  // Fixed speed for now
  int maxSpeed = 5;
  
  /* These variables will keep track of whether the up/down/left/right keys
     are currently being pressed. This is more useful than other approaches
     because your computer will typically cut off the signal from one key when
     another key is pressed.
   */
  boolean upPressed = false;
  boolean downPressed = false;
  boolean leftPressed = false;
  boolean rightPressed = false;

  public EvenBetterKeyboardExample()
  {
    ourSquare = new Square(50, 50, 50, new Color(0xff00b0));
  
    addKeyListener(this);
    setFocusable(true);
  }
  
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    ourSquare.drawTo(g);
  }
  
  public void mainLoop()
  {
    long nextFrame = System.currentTimeMillis();
    while(true)
    { 
      /* We have moved the logic to decide how to respond to key presses into
         the main loop of our program. This creates some small improvments
         right away (such as freezing the square when both left and right are
         pressed, which is probably what you would expect to happen), and also
         allows us more flexibility for future modifications.
       */
      if(rightPressed)
      {
        ourSquare.moveX(maxSpeed);
      }
      if(leftPressed)
      {
        ourSquare.moveX(-maxSpeed);
      }
      if(upPressed)
      {
        ourSquare.moveY(-maxSpeed);
      }
      if(downPressed)
      {
        ourSquare.moveY(maxSpeed);
      }
      
      nextFrame += MSPF;
      
      try
      {
        Thread.sleep(nextFrame - System.currentTimeMillis());
      }
      catch(Exception e){e.printStackTrace();}
      
      repaint();
    }
  }
  
  /* Required methods for KeyListener */
  public void keyPressed(KeyEvent e)
  {
    int code = e.getKeyCode();
    
    /* Notice how we are now just setting these boolean variables to true.
       This allows us to save the information about what keys are pressed.
       In this file, this produces a similar outcome to what we got in
       BetterKeyboardExample.java. But, we now gain the ability to handle both
       rightPressed and leftPressed at the same time!
       
       We can also change what keys count as "right", "left", etc. without
       having to modify most of our code - we just pick a different KeyEvent
       preset here and we're done.
     */
    if(code == KeyEvent.VK_RIGHT)
    {
      rightPressed = true;
    }
    else if(code == KeyEvent.VK_LEFT)
    {
      leftPressed = true;
    }
    else if(code == KeyEvent.VK_UP)
    {
      upPressed = true;
    }
    else if(code == KeyEvent.VK_DOWN)
    {
      downPressed = true;
    }
  }
  
  /* The keyReleased event is once again important, this time to turn the
     appropriate variable to false when the key is released. Logically, if
     a key is released, then it isn't being pressed anymore!
   */
  public void keyReleased(KeyEvent e)
  {
    int code = e.getKeyCode();
    
    if(code == KeyEvent.VK_RIGHT)
    {
      rightPressed = false;
    }
    else if(code == KeyEvent.VK_LEFT)
    {
      leftPressed = false;
    }
    else if(code == KeyEvent.VK_UP)
    {
      upPressed = false;
    }
    else if(code == KeyEvent.VK_DOWN)
    {
      downPressed = false;
    }
  }
  public void keyTyped(KeyEvent e)
  {
  }
}