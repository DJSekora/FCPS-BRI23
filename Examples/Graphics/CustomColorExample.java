/* This class contains examples of how to create and use custom colors in Java */
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

public class CustomColorExample extends JPanel
{ 
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    
    // Here are two ways to make custom colors:
    // Option 1: with an integer color code (most conveniently written in hex)
    // RGB format - RED GREEN BLUE
    Color color1 = new Color(0xff80a0);
                           
    
    // Option 2: with the colors specified individually as integers
    // (you can use hex here too but it's less helpful)
    Color color2 = new Color(70, 20, 180);
    
    g.setColor(color1);
    g.fillRect(200,200,200,200);
    
    g.setColor(color2);
    g.fillRect(400,200,200,200);
    
    /* There's actually an optional fourth field - transparency! */
    g.setColor(new Color(0, 255, 0, 128));

    g.fillRect(300,300,200,200);
  }
}