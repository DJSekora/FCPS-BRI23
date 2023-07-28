/* We can interpret our screen as a side-view rather than a top-down view of
   a scene. 
 */

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener; 

public class GravityExample extends JPanel implements KeyListener
{
  Square ourSquare;
  
  int acceleration = 1;
  int jumpSpeed = 20;
  int gravity = 1;
  int floor = 500; // y coordinate to stop falling at

  boolean leftPressed = false;
  boolean rightPressed = false;

  public GravityExample()
  {
    ourSquare = new Square(200, 200, 50, new Color(0x40ff80));
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

      if(rightPressed)
      {
        ourSquare.applyAccelerationX(acceleration);
      }
      if(leftPressed)
      {
        ourSquare.applyAccelerationX(-acceleration);
      }
      
      ourSquare.applyAccelerationY(gravity);
      
      ourSquare.move();
      
      if(ourSquare.y + ourSquare.size >= floor)
      {
        ourSquare.y = floor - ourSquare.size;
        ourSquare.ySpeed = 0;
      }
      
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
    else if(code == KeyEvent.VK_SPACE)
    {
      ourSquare.applyAccelerationY(-jumpSpeed);
    }
  }
  public void keyTyped(KeyEvent e)
  {
  }
}