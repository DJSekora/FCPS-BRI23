import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener; 

public class ImageCrop extends JPanel implements KeyListener
{ 
  // We have written an object class, so it can have fields! We use one here so
  // that we only need to load the image one time.
  BufferedImage image;
  
  int x = 0;
  int y = 0;
  int right;
  int bottom;
  int speed = 5;
  int offset = 50; // offset for interface
  
  String filename;

  // Since we are now storing data, we should have a constructor to help us
  // initialize that data.
  public ImageCrop(String nameOfFile)
  {
    filename = nameOfFile;
    image = readImage(filename);
    
    right = image.getWidth();
    bottom = image.getHeight();
    
    addKeyListener(this);
    setFocusable(true);
  }

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    
    // image, x coordinate, y coordinate, null
    // (for our purposes in this course, we will always make the last argument
    //  null, but of course it has a purpose)
    g.drawImage(image, x, y + offset, right, bottom + offset, x, y, right, bottom, null);
    g.drawString("Crop speed: " + speed, 10, offset);
  }

  /* Read the image with the specified file name and return it as a BufferedImage. */
  public static BufferedImage readImage(String infile)
  {
    try
    {
      BufferedImage ret = ImageIO.read(new File(infile));
      return ret;
    }
    catch(Exception e){System.out.println(e.getMessage()); return null;}
  }
  
  public void writeImage(String outfile)
  {
    try
    {
      ImageIO.write(image.getSubimage(x, y, right-x, bottom-y),"png",new File(outfile));
    }
    catch(Exception e){e.printStackTrace();}
  }
  
  public void keyPressed(KeyEvent e)
  {
    int code = e.getKeyCode();
    if(code == KeyEvent.VK_A)
    {
      x-=speed;
    }
    else if(code == KeyEvent.VK_D)
    {
      x+=speed;
    }
    else if(code == KeyEvent.VK_W)
    {
      y-=speed;
    }
    else if(code == KeyEvent.VK_S)
    {
      y+=speed;
    }
    else if(code == KeyEvent.VK_LEFT)
    {
      right-=speed;
    }
    else if(code == KeyEvent.VK_RIGHT)
    {
      right+=speed;
    }
    else if(code == KeyEvent.VK_UP)
    {
      bottom-=speed;
    }
    else if(code == KeyEvent.VK_DOWN)
    {
      bottom+=speed;
    }
    else if(code == KeyEvent.VK_X)
    {
      speed++;
    }
    else if(code == KeyEvent.VK_Z)
    {
      speed--;
    }
    else if(code == KeyEvent.VK_ENTER)
    {
      writeImage("cropped" + filename);
    }
    repaint();
  }
  
  public void keyReleased(KeyEvent e)
  {
  }
  
  public void keyTyped(KeyEvent e){}
  
  public static void main(String[] args)
  {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ImageCrop panel = new ImageCrop("Wolf.jpg");
    frame.setSize(panel.right,panel.bottom + panel.offset);
    frame.add(panel);
    frame.setVisible(true);
  }
}