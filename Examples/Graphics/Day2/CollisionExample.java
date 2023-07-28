/* This class builds on our KeyboardExample series by adding collision!

   To help us out, we define a new class called Collisable. A Collisable is a
   rectangle, and we have a method to check if two Collisables intersect.
   This allows us to easily create a bunch of objects on screen that can be
   interacted with. This is an upgrade to our Square class.
   
   (We could also use the word "hitbox" to describe this behavior, but
    hitboxes typically are a slightly different concept in that they have
    positions relative to the objects they belong to, so we'll save that
    word for later.)
 */

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener; 

public class CollisionExample extends JPanel implements KeyListener
{
  Collisable player;
  Collisable obstacle;
  
  int speed = 5;
  
  boolean upPressed = false;
  boolean downPressed = false;
  boolean leftPressed = false;
  boolean rightPressed = false;

  public CollisionExample()
  {
    addKeyListener(this);
    setFocusable(true);
    
    player = new Collisable(300, 300, 50, 50, Color.RED);
    obstacle = new Collisable(499, 99, 200, 100, Color.BLACK);
  }
  
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    
    /* This dot will help us test out our containsPoint method. If it works
       correctly, then when we move the square over the dot it should turn red.
     */
    g.fillRect(399,399,2,2);
    
    obstacle.drawTo(g);
    
    if(player.containsPoint(400, 400))
    {
      g.setColor(Color.RED);
    }
    else if(player.intersects(obstacle))
    {
      g.setColor(Color.GREEN);
    }
    player.drawTo(g);
  }
  
  public void mainLoop()
  {
    while(true)
    { 
      if(rightPressed)
      {
        player.moveRight(speed);
        if(player.intersects(obstacle))
        {
          
        }
      }
      if(leftPressed)
      {
        player.moveLeft(speed);
      }
      if(upPressed)
      {
        player.moveUp(speed);
      }
      if(downPressed)
      {
        player.moveDown(speed);
      }
      
      try
      {
        Thread.sleep(20);
      }
      catch(Exception e){e.printStackTrace();}
      
      repaint();
    }
  }
  
  /* Required methods for KeyListener (no change from EvenBetterKeyboardExample) */
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

/* A support class to allow us to create 'Collisable' objects - objects that
   can collide with other objects
 */
class Collisable
{
  int x;
  int y;
  int width;
  int height;
  
  public Collisable(int nx, int ny, int nw, int nh, Color nc)
  {
    x = nx;
    y = ny;
    width = nw;
    height = nh;
  }
  
  public void drawTo(Graphics g)
  {
    g.fillRect(x, y, width, height);
  }
  
  /* Return true if the point is contained inside the rectangle for this
     Collisable, false otherwise.
     FILL THIS IN!
   */
  public boolean containsPoint(int pointX, int pointY)
  {
    return pointX > x &&
           pointX /* fill the rest of this in */;
  }
  
  /* Return true if the rectangles for this Collisable and the parameter other
     are intersecting, false otherwise.
     FILL THIS IN!
   */
  public boolean intersects(Collisable other)
  {
    return x + width > other.x &&
           x > /* fill the rest of this in */;
  }
  
  /* Movement methods for our square.
     We will modify this in a future file to stop our player square from being
     able to run into other squares.
   */
  public void moveRight(int amount)
  {
    x += amount;
  }
  
  public void moveLeft(int amount)
  {
    x -= amount;
  }
  
  public void moveUp(int amount)
  {
    y -= amount;
  }
  
  public void moveDown(int amount)
  {
    y += amount;
  }
}