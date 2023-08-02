// Basic imports for graphics
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;

// Mouse event management
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// Mouse location stuff (for click and drag)
import java.awt.Point;
import java.awt.MouseInfo;
import java.awt.PointerInfo;

public class DrawingExample extends JPanel implements MouseListener
{
  int color = 0xff000000;
  BufferedImage picture;
  boolean clicking = false;
  
  public DrawingExample()
  {
    picture = new BufferedImage(400,400, BufferedImage.TYPE_INT_ARGB);
  
    addMouseListener(this);
    setFocusable(true);
  }
    
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    g.drawImage(picture,0,0,null);
  }
  
  public void mainLoop()
  {
    while(true)
    {
      if(clicking)
      {
        Point cp = MouseInfo.getPointerInfo().getLocation();
        Point offset = getLocationOnScreen();
        
        int cx = cp.x - offset.x;
        int cy = cp.y - offset.y;
        if(cx > 0 && cx < picture.getWidth() && cy > 0 && cy < picture.getHeight())
        {
          picture.setRGB(cx, cy, color);
        }
      }
      try
      {
        Thread.sleep(20);
      }catch(Exception e){e.printStackTrace();}
      repaint();
    }
  }
  
  
  /* Required methods for MouseListener */
  public void mousePressed(MouseEvent e)
  {
    int button = e.getButton();
    if(button == MouseEvent.BUTTON1)
    {
      clicking = true;
    }
  }
  public void mouseReleased(MouseEvent e)
  {
    int button = e.getButton();
    if(button == MouseEvent.BUTTON1)
    {
      clicking = false;
    }
  }
  public void mouseClicked(MouseEvent e){}  
  public void mouseEntered(MouseEvent e){} 
  public void mouseExited(MouseEvent e){} 
  
  public static void main(String[] args)
  {
    JFrame frame = new JFrame();
    frame.setSize(800,600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    DrawingExample panel = new DrawingExample();
    frame.add(panel);
    frame.setVisible(true);
    
    panel.mainLoop();
  }
}