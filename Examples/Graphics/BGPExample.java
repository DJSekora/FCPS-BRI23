/* This class contains an example of how to set up a basic graphical window
   on which images can be drawn. I recommend that you create a copy of this
   for yourself with most of the comments removed to use as a template.
   
   Main method can be found in GraphicsMainExample.java
   
   Analogy:
    JFrame:   the easel
    JPanel:   the canvas
    Graphics: the paintbrush
    Color:    the paint
 */
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

public class BGPExample extends JPanel
{
  // Constructor does nothing for now, we can add in functionality later.
  public BGPExample()
  {
  }
  
  /* This method is where you put everything to be drawn to the screen.
     You don't ever actually need to call this method directly - that is
     handled for you by the JPanel (first upon initialization, and later
     whenever repaint() is invoked).
   */
  public void paintComponent(Graphics g)
  {
    /* This class extends JPanel, which has a method called paintComponent
       already. We want to run the code from that method first to initialize
       the drawing process - this is precisely what the 'super' keyword does!
     */
    super.paintComponent(g);
    
    // This changes the color for the duration of this method.
    g.setColor(Color.RED);
    
    // Draw the outline of a rectangle to the screen at position x=50, y=100
    // with width 150 and height 200. All positions and sizes are measured in
    // number of pixels. Position is for upper left corner.
    g.drawRect(50,100,150,200);
  }
}