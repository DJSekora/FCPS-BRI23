/* This class shows how to make a basic image editing program.

   It's currently missing quite a few features, so see how many you can add:
   
   * Ability to change the color (with either keyboard commands,
     onscreen buttons, or typed commands in the console)
   * Ability to save your creation to a file
   * Functions to perform basic operations such as flipping the image upside
     down or inverting the color palette
   * A "replace all" feature that replaces all of one color with another color
   * 
 */

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BasicImageEditor extends JPanel implements MouseListener
{
  int rows = 5;
  int cols = 6;
  int boxSize = 50;
  
  // for convenience
  int height = rows*boxSize;
  int width = cols*boxSize;
  
  Color[][] picture;
  
  Color brushColor = Color.RED;

  public BasicImageEditor()
  {
    // Initialize the picture to be blank (all white)
    picture = new Color[rows][cols];
    for(int row = 0; row < rows; row++)
    {
      for(int col = 0; col < cols; col++)
      {
        picture[row][col] = Color.WHITE;
      }
    }
  
    addMouseListener(this);
    setFocusable(true);
  }
  
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    
    // Draw the horizontal lines
    for(int row = 0; row <= rows; row++)
    {
      g.drawLine(0, row*boxSize, width, row*boxSize);
    }
    
    // Draw the vertical lines
    for(int col = 0; col <= cols; col++)
    {
      g.drawLine( col*boxSize, 0, col*boxSize, height);
    }
    
    // Fill in all the grid squares
    for(int row = 0; row < rows; row++)
    {
      for(int col = 0; col < cols; col++)
      {
        g.setColor(picture[row][col]);
        g.fillRect(col*boxSize+1, row*boxSize+1, boxSize-1, boxSize-1);
      }
    }
  }
  
  /* Required methods for MouseListener */
  public void mousePressed(MouseEvent e)
  {
    int button = e.getButton();
    if(button == MouseEvent.BUTTON1)
    {
      if(e.getX() >= 0 && e.getX() < width && e.getY() >= 0 && e.getY() < height)
      {
        int col = e.getX()/boxSize;
        int row = e.getY()/boxSize;
        
        picture[row][col] = brushColor;
      }
    }
    repaint();
  }
  public void mouseReleased(MouseEvent e)
  {
  }
  public void mouseClicked(MouseEvent e)
  {
  }  
  public void mouseEntered(MouseEvent e)
  { 
  } 
  public void mouseExited(MouseEvent e)
  { 
  } 
}