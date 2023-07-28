import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener; 

public class ImageResize extends JPanel implements KeyListener
{ 
  // We have written an object class, so it can have fields! We use one here so
  // that we only need to load the image one time.
  BufferedImage image;
  
  BufferedImage original;
  
  int wid;
  int hei;
  
  int scale = 4;
  
  String filename;

  // Since we are now storing data, we should have a constructor to help us
  // initialize that data.
  public ImageResize(String nameOfFile)
  {
    filename = nameOfFile;
    original = readImage(filename);
    wid = original.getWidth();
    hei = original.getHeight();
    
    resizeImage();
    
    addKeyListener(this);
    setFocusable(true);
  }

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    g.drawImage(image, 0, 0, null);
    g.drawString("Scale: " + scale, 10, 50);
  }
  
  public void resizeImage()
  {
    image = new BufferedImage(scale*wid, scale*hei, BufferedImage.TYPE_INT_ARGB);
    
    for(int y = 0; y < hei; y++)
    {
      for(int x = 0; x < wid; x++)
      {
        for(int i=0; i<scale; i++)
        {
          for(int j=0; j<scale; j++)
          {
            image.setRGB(x*scale + i,y*scale + j,original.getRGB(x,y));
          }
        }
      }
    }
    repaint();
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
      ImageIO.write(image,"png",new File(outfile));
    }
    catch(Exception e){e.printStackTrace();}
  }
  
  public void keyPressed(KeyEvent e)
  {
    int code = e.getKeyCode();
    
    if(code == KeyEvent.VK_X)
    {
      scale++;
      resizeImage();
    }
    else if(code == KeyEvent.VK_Z)
    {
      scale--;
      resizeImage();
    }
    else if(code == KeyEvent.VK_ENTER)
    {
      writeImage("resized" + filename);
    }
  }
  
  public void keyReleased(KeyEvent e)
  {
  }
  
  public void keyTyped(KeyEvent e){}
  
  public static void main(String[] args)
  {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ImageResize panel = new ImageResize("Wolf.jpg");
    frame.setSize(1200, 800);
    frame.add(panel);
    frame.setVisible(true);
  }
}