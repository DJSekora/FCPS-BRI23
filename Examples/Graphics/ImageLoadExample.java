/* This class contains an example of how to load an image from a file in Java */
import javax.swing.JPanel;
import java.awt.Graphics;

// New imports for image reading
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageLoadExample extends JPanel
{ 
  // We have written an object class, so it can have fields! We use one here so
  // that we only need to load the image one time.
  BufferedImage image;

  // Since we are now storing data, we should have a constructor to help us
  // initialize that data.
  public ImageLoadExample(String filename)
  {
    image = readImage(filename);
  }

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    
    // image, x coordinate, y coordinate, null
    // (for our purposes in this course, we will always make the last argument
    //  null, but of course it has a purpose)
    g.drawImage(image, 0, 0, null);
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
}