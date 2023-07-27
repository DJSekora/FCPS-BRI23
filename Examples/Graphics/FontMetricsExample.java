/* This class contains examples of how to get the actual display size of a
   String rendered in a font, and how to use that size to center the Font or
   make other decisions about the image.
 */
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;

public class FontMetricsExample extends JPanel
{ 
  Font font;
  
  public FontMetricsExample()
  {
    // Showing off bitwise OR for font style
    font = new Font("Arial", Font.BOLD | Font.ITALIC, 32);
  }

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    
    g.setFont(font);
    
    // This is a custom method we have written below!
    drawBoxedText(g, "sample text here", 100, 50, 0);
    drawBoxedText(g, "sample text here", 400, 100, 20);
    
    g.drawOval(300, 300, 200, 200);
    drawCenteredText(g, "Centered!", 400, 400);
  }
  
  /* This is a "helper method". We often create these when we have small tasks
     that take a few lines of repetitive code and can be easily parameterized
     (the range of different outcomes we want can be described by a handful of
      variables).
      
     In this case, this is a method to draw some text with a box around it. The
     box is separated from the text by at least 'border' pixels on all sides.
     
     Note that the x and y coordinates given are for the bounding box, not for
     the String itself.
     
     Note also that we have to include the Graphics object here, otherwise we
     wouldn't be able to call on the methods we need to do the actual drawing!
   */
  public void drawBoxedText(Graphics g, String text, int x, int y, int border)
  { 
    Font f = g.getFont(); // get the actual font currently being used
    
    // The FontMetrics class gives us all sorts of information about how the
    // Font will be rendered in the current environment.
    // (getFontMetrics is a method provided by JPanel).
    FontMetrics metrics = getFontMetrics(f);
    
    // This gives the width in pixels of the specified String
    // Note how we add 2*border because the border is on the left and right
    int width = metrics.stringWidth(text) + 2*border;
    
    /* This gives the max height of any String in this font. For some reason
       probably having to do with Unicode support (e.g. accent marks), the
       height given by this method is generally too large for English text.
       So, I've found that multiplying by 4/5 often gives a height that is
       "just right". (Results may vary by font and character set.)
       
       (You can alternatively use getHeight, in which case multiplying by 2/3
        might work better.)
    */
    int height = metrics.getAscent()*4/5 + 2*border;
    
    // Finally we can draw the actual String!
    // Note how we have to add the height, since drawString y coordinate is
    // the bottom.
    g.drawString(text, x + border, y + height - border);
    
    // Note that we have to subtract the height here because the y coordinate
    // for drawString is the bottom not the top!
    g.drawRect(x, y, width, height);
  }
  
  /* The above method figures out how large the String is, then sizes the box
     to match. This is a slightly different approach: given a fixed point in
     the image, adjust the position of the String based on its size to ensure
     it is centered on that point.
   */
  public void drawCenteredText(Graphics g, String text, int xCenter, int yCenter)
  { 
    Font f = g.getFont();
    FontMetrics metrics = getFontMetrics(f);
    int width = metrics.stringWidth(text);
    int height = metrics.getAscent()*4/5;
    
    g.drawString(text, xCenter-width/2, yCenter+height/2);
  }
}