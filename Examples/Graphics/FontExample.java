/* This class contains an example of how to create a Font object and make your
   drawString calls use that Font.
 */
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;

public class FontExample extends JPanel
{ 
  // We'll use a field to represent the font rather than a variable inside
  // of the paintComponent method. This means we don't have to remake the Font
  // every time we redraw the screen (a very small optimization)
  Font font;
  
  public FontExample()
  {
    // Syntax is font name, font style, and font size.
    // (PLAIN, ITALICS, and BOLD are available for font style, see Oracle
    //  documentation for more details,)
    font = new Font("Arial", Font.PLAIN, 48);
  }

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    
    // We have to set the Graphics object to use our Font for it to show up
    // on the screen. You can use different fonts for different Strings in the
    // same image.
    g.setFont(font);
    g.setColor(new Color(0xb16725));
    g.drawString("This is in Arial 48pt", 100, 50);
  }
}