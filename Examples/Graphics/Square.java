/* This class represents a square we want to move around the screen.
   We will use this in conjunction with multiple examples, including
   BetterKeyboardExample and EvenBetterKeyboardExample.
 */
import java.awt.Color;
import java.awt.Graphics;
public class Square
{
  int x;
  int y;
  int size;
  Color color;
  
  // The speed will now change depending on input and be applied in a main loop
  int xSpeed = 0;
  int ySpeed = 0;
  
  /* Our maxSpeed makes a comeback, this time as a limiter on how large the
     speed can get in each direction. We put it in the Square class instead
     of the JPanel-extending class because we have most of the movement logic
     in here.
     (If we wanted to be even more realistic, we could limit the TOTAL speed
      instead of the speed in each direction, but that would require some
      trigonometry and more code so we'll leave that for now.)
   */
  int maxSpeed = 20;
  
  public Square(int nx, int ny, int nsize, Color ncolor)
  {
    x = nx;
    y = ny;
    size = nsize;
    color = ncolor;
  }
  
  public void drawTo(Graphics g)
  {
    g.setColor(color);
    g.fillRect(x, y, size, size);
  }
  
  // Applies the speed to the position
  public void move()
  {
    moveX(xSpeed);
    moveY(ySpeed);
  }
  
  // Manually changes the position by the entered amount
  public void moveX(int amount)
  {
    x += amount;
  }
  
  public void moveY(int amount)
  {
    y += amount;
  }
  
  // Acceleration modifies the speed
  public void applyAccelerationX(int amount)
  {
    xSpeed += amount;
    
    // Make sure we don't go past our max
    if(xSpeed > maxSpeed)
    {
      xSpeed = maxSpeed;
    }
    if(xSpeed < -maxSpeed)
    {
      xSpeed = -maxSpeed;
    }
  }
  
  public void applyAccelerationY(int amount)
  {
    ySpeed += amount;
    
    if(ySpeed > maxSpeed)
    {
      ySpeed = maxSpeed;
    }
    if(ySpeed < -maxSpeed)
    {
      ySpeed = -maxSpeed;
    }
  }
}