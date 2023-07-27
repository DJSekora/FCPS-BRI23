/* Basic example of how to do animation.

   Make sure to call panel.mainLoop() in your main method!
 */
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

public class AnimationExample extends JPanel
{
  private int x;
  private int speed;
  private int delay = 10;

  public AnimationExample()
  {
    x = 50;
    speed = 5;
  }
  
  /* Update the image and redraw the screen */
  public void mainLoop()
  {
    while(true)
    {
      // Not great style to say while(true), but we'll talk about alternatives
      // later. For now, this is our main way to have a persistent program.
      while(x < 600)
      {
        // Wait an amount of time in between frames
        try
        {
          Thread.sleep(delay);
        }catch(Exception e){e.printStackTrace();}
        
        // Make a change
        x+=speed;
        
        // Redraw the screen
        repaint(); // built-in to JPanel
      }
      while(x > speed)
      {
        try{Thread.sleep(delay);}catch(Exception e){e.printStackTrace();}
        x-=speed;
        repaint(); // built-in to JPanel
      }
    }
  }
  
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    
    g.setColor(new Color(x/3, 80, 80));
    
    // Make sure something in your paintComponent method is linked to a variable!
    g.fillRect(x, x, 50, 50);
  }
}