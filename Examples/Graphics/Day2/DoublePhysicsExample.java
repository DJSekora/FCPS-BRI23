/* This class makes some small modifications to EvenBetterKeyboardExample to
   add acceleration. Now, the controls don't increase or decrease the position
   of the square directly - instead, they increase or decrease the velocity of
   the square in that direction, and then the velocity at each frame is used
   to calculate the position.
 */

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener; 

public class DoublePhysicsExample extends JPanel implements KeyListener
{
  DoubleSquare ourSquare;
  
  // This variable controls the rate of change of the speed at each frame.
  // If we want more fine-grained control, we can either use doubles or use
  // speeds at a magnified scale (e.g. multiply maxSpeed by 10, then divide by
  // 10 whenever you use a speed to modify a velocity)
  int acceleration = 1;
  
  boolean upPressed = false;
  boolean downPressed = false;
  boolean leftPressed = false;
  boolean rightPressed = false;

  public DoublePhysicsExample()
  {
    ourSquare = new DoubleSquare(50, 50, 50, new Color(0x7000ff));
  
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
    while(true)
    {
      try
      {
        Thread.sleep(20);
      }
      catch(Exception e){e.printStackTrace();}
      
      /* We use the acceleration to increase or decrease the speed depending on
         which button is pressed. The logic is handled in Square.java
       */
      if(rightPressed)
      {
        ourSquare.applyAccelerationX(acceleration);
      }
      if(leftPressed)
      {
        ourSquare.applyAccelerationX(-acceleration);
      }
      if(upPressed)
      {
        ourSquare.applyAccelerationY(-acceleration);
      }
      if(downPressed)
      {
        ourSquare.applyAccelerationY(acceleration);
      }
      
      /* Finally, we apply the xSpeed and ySpeed we calculated to the position
         variables, which will update the position of the square on the screen.
         We already handled this in Square with the move method.
       */
      ourSquare.move();
      
      repaint();
    }
  }
  
  /* Required methods for KeyListener.
     (These are unchanged from EvenBetterKeyboardExample, which is part of why
      we set it up in this way!)
   */
  public void keyPressed(KeyEvent e)
  {
    int code = e.getKeyCode();
    
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