/* Sometimes, we want to draw something more complicated than a series of
   rectangles, ovals, Strings, lines, and polygons.
   
   At times like these, we can work at the pixel level! This COULD be
   accomplished by calling drawRect over and over again with 1x1 rectangles,
   but it's more efficient to just modify the pixel values of an image and
   then make one drawImage call. This has the additional advantage that we
   only have to create the image one time, so we don't have to worry as much
   about optimizing the image creation process.
 */
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

// Need these two to save images to files
import javax.imageio.ImageIO;
import java.io.File;

public class ImageCreationExample extends JPanel
{ 
  BufferedImage image;
  
  public ImageCreationExample()
  {
    int width = 800;
    int height = 600;
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    
    // Fill the whole image with white
    for(int row=0; row<height; row++)
    {
      for(int col=0; col<width; col++)
      {
        // xcoord, ycoord, color code (first two hex digits are transparency)
        image.setRGB(col, row, 0xffffffff);
        
        // More exciting background option:
        //image.setRGB(col, row, 0xffff0000 + 0x00000100*row/3 + 0x00000001*col/10);
      }
    }
    
    // Draw a function in white on top
    // i is looping through all possible x coordinates
    for(int i=0; i<width; i++)
    {
      /* The sin function ranges from -1 to 1, so adding height/2 centers the
         function vertically on the screen. The *0.02 multiplier for x is
         because the period of sin is 2pi, which is about 6.28 (so if we just
         used i, the function would be bouncing up and down and look
         rather chaotic). The height/4 scale factor is needed because a 1 pixel
         high sin graph wouldn't be very exciting!
       */
      //image.setRGB(i, (int)(height/2 + Math.sin(i*0.02)*height/4), 0xffffffff);
      
      double x = i*0.02;
      double y = (height/4)*Math.sin(x) + height/2;
      if(y >= 0 && y<height)
      {
        image.setRGB((int)i, (int)y -1, 0xffff0000);
        image.setRGB((int)i, (int)y, 0xffff0000);
        image.setRGB((int)i, (int)y +1, 0xffff0000);
      }
    }
    
    // Uncomment this part if you want to save the created image to a file
    try
    {
      ImageIO.write(image,"png",new File("out.png"));
    }
    catch(Exception e){e.printStackTrace();}
  }

  // Note how we don't need to do anything special in the paintComponent
  // method if we have already constructed the image elsewhere.
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    g.drawImage(image, 0, 0, null);
  }
}