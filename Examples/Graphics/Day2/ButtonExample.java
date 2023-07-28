/* Demonstrates how to detect a click within a rectangular region (essentially
   a button).
   
   In practical applications, you're probably better off using Java's built-in
   GUI system via JButton etc.. But, the ideas here roughly indicate what the
   Java developers did to make that system!
 */

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ButtonExample extends JPanel implements MouseListener
{
  Button myButton; //NEW

  public ButtonExample()
  {
    // left, top, right, bottom
    myButton = new Button(100, 300, 200, 400); //NEW
  
    // MouseListener instead of KeyListener
    addMouseListener(this);
    setFocusable(true);
  }
  
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    myButton.drawTo(g); //NEW
  }
  
  /* Required methods for MouseListener */
  public void mousePressed(MouseEvent e)
  {
    // getButton tells us which button was clicked.
    int button = e.getButton();
    
    // BUTTON1 = left click
    if(button == MouseEvent.BUTTON1)
    {
      int x = e.getX();
      int y = e.getY();
      if( myButton.clicked(x,y) )
      {
        System.out.println("Button was clicked!");
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

class Button
{
  public int left;
  public int right;
  public int top;
  public int bottom;
  
  public Button(int l, int t, int r, int b)
  {
    left = l;
    top = t;
    right = r;
    bottom = b;
  }
  
  public void drawTo(Graphics g)
  {
    g.fillRect(left, top, right-left, bottom-top);
  }
  
  public boolean clicked(int x, int y)
  {
    return x > left && x < right && y > top && y < bottom;
  }
}