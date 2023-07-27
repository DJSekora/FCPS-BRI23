/* This class contains examples of basic functions for drawing in Swing */
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

public class GraphicsMethodsExample extends JPanel
{ 
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    
    // fillOval, fillRect, drawOval, and drawRect have the same syntax.
    g.fillOval(50, 50, 50, 50);
    g.setColor(Color.BLUE);
    g.fillRect(150, 50, 50, 50);
    g.drawOval(250, 50, 50, 50);
    g.setColor(Color.GREEN);
    g.drawRect(350, 50, 50, 50);
    
    
    /* Note that the "upper left corner" for the oval is outside the oval
       itself - it uses the y coordinate of the top of the oval and the x
       coordinate of the leftmost part of the oval.
       (If this is confusing, imagine drawing an invisible rectangle of the
        given size and then fitting the biggest possible oval inside)
     */
    g.setColor(Color.BLACK);
    g.fillRect(50, 300, 200, 200);
    
    // Drawing order matters - note how the oval blocks the square
    g.setColor(Color.RED);
    g.fillOval(50, 300, 200, 200);
    
    // We can put loops in!
    g.setColor(Color.BLACK);
    for(int i=0; i<8; i++)
    {
      g.fillRect(400, 100 + 100*i, 50, 50);
    }
  }
}