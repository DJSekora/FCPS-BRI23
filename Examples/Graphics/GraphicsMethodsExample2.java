/* This class contains more examples of basic functions for drawing in Swing
   See the full spec at the Oracle documentation website for even more!
   (Examples include drawArc and fillArc, for drawing only part of an oval)
 */
import javax.swing.JPanel;
import java.awt.Graphics;

public class GraphicsMethodsExample2 extends JPanel
{ 
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    
    // We can draw a String like this. But, note that Strings are WEIRD because
    // the coordinate specifies the lower left corner not the upper left corner
    // (ask me if you want help with text formatting)
    g.drawString("CTYJOY", 50, 50);
    g.drawRect(50,50,80,20);
    
    // If we want to draw a polygon, we can use lines one by one:
    g.drawLine(150, 50, 250, 50);
    g.drawLine(200, 130, 250, 50);
    g.drawLine(150, 50, 200, 130);
    
    /* But, this can get tedious, and if we want to fill the polygon we'd have
       to make our code a lot more sophisticated to determine what the "inside"
       is. Luckily, Java has a built-in way to do this. First, you specify
       arrays containing the x coordinates and the y coordinates of the
       vertices of the polygon:
     */
    int[] xcoords = new int[]{350, 325, 375, 425, 400};
    int[] ycoords = new int[]{150, 90,  50,  90,  150};
    
    // Then, pass the vertices into the drawPolygon or fillPolygon method.
    // The final argument is the number of vertices (useful if you have a long
    // array you just want to take the first portion of):
    for(int i=0; i<=5; i++)
    {
      g.fillPolygon(xcoords, ycoords, i);
      for(int j=0; j<ycoords.length; j++)
      {
        ycoords[j] += 100;
      }
    }
  }
}