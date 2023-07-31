/* This class contains an example of how to rotate an image in Java
   using just what we've learned + a little bit of trigonometry.
   
   To see it, make an image called myimg.png and put it in the same
   folder as this.
   
   There's a main method at the bottom so you don't need to use
   GraphicsMainExample
   
   You can copy the rotateImage method and use it freely, no new imports
   required.

   It's a bit slow, so if you need a faster method you should
   probably use a library. You can Google something like
   "Java Graphics2D rotate image" to see examples of doing it this way
   
   Based on the ImageLoadExample
 */
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;

// Imports for image reading
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageRotateExample extends JPanel
{ 
  BufferedImage image;
  BufferedImage rotated;
  
  public ImageRotateExample(String filename)
  {
    image = readImage(filename);
    rotated = rotateImage(image, 45); // change the number to rotate by a different amount
  }

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    
    g.drawImage(image, 0, 100, null);
    g.drawImage(rotated, 400, 100, null);
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
  
  // input: original image, angle in degrees
  // returns: rotated image
  // don't worry about the details if too complicated, you can just use it
  public static BufferedImage rotateImage(BufferedImage original, double angle)
  {
    // define some variables we will be using a lot to simplify the math
    double PI2 = Math.PI*2;
    double sin = Math.abs(Math.sin(angle));
    double cos = Math.abs(Math.cos(angle));
    int ow = original.getWidth();
    int oh = original.getHeight();
    int w = (int)(ow*cos + oh*sin);
    int h = (int)(ow*sin + oh*cos);
    double xc = w/2.0;
    double yc = h/2.0;
    double oxc = ow/2.0;
    double oyc = oh/2.0;
    
    // Make the new image for the rotated version
    BufferedImage ret = new BufferedImage(w, h, original.getType()==0 ? BufferedImage.TYPE_INT_ARGB : original.getType());
    
    double xd, yd, r, ang;
    int sx, sy;
    
    // Loop over the rows and columns of the new image we're building
    for(int y = 0; y < h; y++)
    {
      yd = y - yc;
      for(int x = 0; x < w; x++)
      {
        xd = x - xc;
        r = Math.sqrt(xd*xd + yd*yd);
        ang = Math.acos(xd/r);
        if(yd < 0)
        {
          ang = PI2 - ang;
        }
        
        ang -= angle;
        
        sx = (int)Math.round(r*Math.cos(ang) + oxc);
        sy = (int)Math.round(r*Math.sin(ang) + oyc);
        
        if(sx >=0 && sx < ow && sy >= 0 && sy < oh)
        {
          ret.setRGB(x, y, original.getRGB(sx, sy));
        }
        else
        {
          ret.setRGB(x,y,0);
        }
      }
    }
    
    return ret;
  }
  
  public static void main(String[] args)
  { 
    JFrame frame = new JFrame();
    frame.setSize(800,600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ImageRotateExample panel = new ImageRotateExample("myimg.png");
    frame.add(panel);
    frame.setVisible(true); 
  }
}