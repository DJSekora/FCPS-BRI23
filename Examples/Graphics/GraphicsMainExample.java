/* This is a basic main method to be used with graphical applications.
   It can technically go in the same class as the Panel class, but we've
   separated it here for clarity.
 */
import javax.swing.JFrame;

public class GraphicsMainExample
{
  public static void main(String[] args)
  { 
    // Initialize the window
    JFrame frame = new JFrame();

    // Set the width and height of the window in pixels
    frame.setSize(800,600);
    
    // Make the program end when the window is closed
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // Initialize the drawing canvas (replace BGPExample with the name of any
    // class that extends JPanel)
    BGPExample panel = new BGPExample();
    
    // Add the drawing canvas to the window
    frame.add(panel);
    
    // Show the window when we are done with all of our initialization
    frame.setVisible(true);
  }
}
