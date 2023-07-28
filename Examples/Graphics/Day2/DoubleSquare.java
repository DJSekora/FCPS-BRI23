import java.awt.Color;
import java.awt.Graphics;
public class DoubleSquare
{
  double x;
  double y;
  double size;
  Color color;
  
  // The speed will now change depending on input and be applied in a main loop
  double xSpeed = 0;
  double ySpeed = 0;
  
  double friction = 0.95; // Multiplier for speed at each tick so we don't get infinite movement
  
  public DoubleSquare(double nx, double ny, double nsize, Color ncolor)
  {
    x = nx;
    y = ny;
    size = nsize;
    color = ncolor;
  }
  
  public void drawTo(Graphics g)
  {
    g.setColor(color);
    g.fillRect((int)x, (int)y, (int)size, (int)size);
  }
  
  // Applies the speed to the position
  public void move()
  {
    moveX(xSpeed);
    moveY(ySpeed);
    
    applyFriction();
  }
  
  // Manually changes the position by the entered amount
  public void moveX(double amount)
  {
    x += amount;
  }
  
  public void moveY(double amount)
  {
    y += amount;
  }
  
  // Acceleration modifies the speed
  public void applyAccelerationX(double amount)
  {
    xSpeed += amount;
  }
  
  public void applyAccelerationY(double amount)
  {
    ySpeed += amount;
  }
  
  public void applyFriction()
  {
    xSpeed *= friction;
    ySpeed *= friction;
  }
}