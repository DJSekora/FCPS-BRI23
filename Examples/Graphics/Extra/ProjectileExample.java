/* Based on EvenBetterKeyboardExample
   
   This example illustrates one way to manage a projectile system.
   It also shows a way to create sprites, which consist of an image
   along with the usual properties we've been keeping track of in
   classes like Square and Collisable.
 */
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// File loading stuff
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

// Using ArrayList to hold our projectiles
import java.util.ArrayList;

// Putting the main method in this file means that we need JFrame too
import javax.swing.JFrame;

import java.util.Random;

public class ProjectileExample extends JPanel implements KeyListener
{
  static final int MSPF = 20; // Amount of time each frame should take

  MovingSprite player;
  ArrayList<MovingSprite> projectiles;
  Collider target;
  
  BufferedImage projectileImage;
  
  // Max speeds for player and projectiles
  int playerSpeed = 5;
  int projectileSpeed = 8;
  
  // Amount of frames to wait before allowed to shoot next bullet
  int cooldown = 20;
  int cooldownRemaining = 0;
  
  int score = 0;
  Random rng = new Random();
  
  boolean upPressed = false;
  boolean downPressed = false;
  boolean leftPressed = false;
  boolean rightPressed = false;
  
  // Works just like the above 4 variables. In the main loop, if shooting
  // is true then we should spawn a new projectile.
  boolean shootPressed = false;
  
  int leftBorder = 0;
  int rightBorder = 2000;
  int topBorder = 0;
  int bottomBorder = 2000;

  public ProjectileExample()
  {
    BufferedImage playerImage = readImage("smile.png");
    projectileImage = readImage("bullet.png");
    BufferedImage targetImage = readImage("target.png");
    
    player = new MovingSprite(400,300,playerImage);
    projectiles = new ArrayList<MovingSprite>();
    target = new MovingSprite(300,100,targetImage);
  
    addKeyListener(this);
    setFocusable(true);
  }
  
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    player.drawTo(g);
    target.drawTo(g);
    for(MovingSprite s:projectiles)
    {
      s.drawTo(g);
    }
    
    g.drawString("Score: " + score, 500, 100);
  }
  
  public void mainLoop()
  {
    long nextFrame = System.currentTimeMillis();
    while(true)
    { 
      if(rightPressed)
      {
        player.moveX(playerSpeed);
      }
      if(leftPressed)
      {
        player.moveX(-playerSpeed);
      }
      if(upPressed)
      {
        player.moveY(-playerSpeed);
      }
      if(downPressed)
      {
        player.moveY(playerSpeed);
      }
      
      // Always move all the bullets, dispose on leaving the screen
      MovingSprite temp;
      for(int i=0; i<projectiles.size(); i++)
      {
        temp = projectiles.get(i);
        temp.move();
        
        // If our bullet hits, remove it from the list
        if(temp.intersects(target))
        {
          projectiles.remove(i);
          // decrease i because we don't want to skip the next bullet
          i--;
          score++;
          
          // randomize target position for fun
          target.x = rng.nextInt(400) + 100;
        }
        
        // If the bullet is offscreen, remove it from the list to free space
        else if(offscreen(temp))
        {
          projectiles.remove(i);
          i--;
        }
      }
      
      // Shoot if space is held and it's off cooldown
      if(shootPressed && cooldownRemaining == 0)
      {
        spawnProjectile();
      }
      
      // refresh player shooting cooldown
      if(cooldownRemaining > 0)
      {
        cooldownRemaining--;
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
  
  public boolean offscreen(Collider c)
  {
    return (c.x + c.width < leftBorder) || (c.x > rightBorder) || (c.y + c.height < topBorder) || (c.y > bottomBorder);
  }
  
  public void spawnProjectile()
  {
    if(cooldownRemaining == 0)
    {
      MovingSprite temp = new MovingSprite(player.x, player.y, projectileImage);
      temp.ySpeed = -projectileSpeed;
      projectiles.add(temp);
      cooldownRemaining = cooldown;
    }
  }
  
  /* Required methods for KeyListener */
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
    
    /* We could spawn the bullet here, but then you couldn't hold down the
       button and get a constant stream of bullets.
       
       Don't forget to also disable shootPressed in the keyReleased method!
     */
    else if(code == KeyEvent.VK_SPACE)
    {
      //spawnProjectile();
      shootPressed = true;
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
    else if(code == KeyEvent.VK_SPACE)
    {
      shootPressed = false;
    }
  }
  public void keyTyped(KeyEvent e)
  {
  }
  
  public static BufferedImage readImage(String infile)
  {
    try
    {
      BufferedImage ret = ImageIO.read(new File(infile));
      return ret;
    }
    catch(Exception e){System.out.println(e.getMessage()); return null;}
  }
  
  // We can put the main method in here if we want
  public static void main(String[] args)
  { 
    JFrame frame = new JFrame();
    frame.setSize(800,600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ProjectileExample panel = new ProjectileExample();

    frame.add(panel);
    
    // Show the window when we are done with all of our initialization
    frame.setVisible(true);
    
    panel.mainLoop();
  }
}

class Collider
{
  int x;
  int y;
  int width;
  int height;
  
  /* Some people prefer this convention for entering data in a constructor.
     Using the same variable names makes it easy to tell what should go where.
     But, the names conflict, and Java defaults to the narrowest scope.
     So, we have to write this.x if we want the instance variable.
   */
  public Collider(int x, int y, int width, int height) // the input parameters
  {
    this.x = x; // set the instance variable x equal to the input parameter x
    this.y = y;
    this.width = width;
    this.height = height;
  }
  
  // Instructions for 
  // default is that the collider is invisible
  // useful for invisible walls
  // override this in extending classes
  public void drawTo(Graphics g)
  {
    // leave blank
  }
  
  // Defining feature of a Collider is that it has collision!
  // So we get our collision equations
  public boolean intersects(Collider other)
  {
    return x + width > other.x &&
           other.x + other.width > x &&
           y + width > other.y &&
           other.y + other.height > y;
  }
}

class MovingSprite extends Collider
{
  BufferedImage image;
  int xSpeed = 0;
  int ySpeed = 0;
  public MovingSprite(int x, int y, BufferedImage image)
  {
    // calls the Collider constructor
    super(x,y,image.getWidth(),image.getHeight());
    this.image = image;
  }
  
  public void drawTo(Graphics g)
  {
    g.drawImage(image, x, y, null);
  }
  
  public void moveX(int amt)
  {
    x += amt;
  }
  public void moveY(int amt)
  {
    y += amt;
  }
  
  public void move()
  {
    moveX(xSpeed);
    moveY(ySpeed);
  }
}
