/* This class contains an example of how to attach keyboard input to a
   window.
   
   We will need a new keyword: implements. The details of it aren't super
   important for our purposes, but for those interested I have included a
   comment at the end of this file with some explanation.
   
   Documentation for the different key codes can be found in the Oracle docs
   page for KeyEvent.
 */

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener; 

public class KeyboardExample extends JPanel implements KeyListener
{
  // These four variables describe the properties of our rectangle.
  int sx = 200;
  int sy = 200;
  int sw = 50;
  int sh = 50;

  // How many pixels to move per key press registered
  int speed = 5;

  public KeyboardExample()
  {
    // Activates keyboard input (links the KeyListener to the JPanel, see the
    // comment at the end of this file for more in-depth information)
    addKeyListener(this);
    
    // Allows the JPanel to be the 'focus' (the window to which keyboard and
    // mouse events are routed by the operating system)
    setFocusable(true);
  }
  
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    g.fillRect(sx, sy, sw, sh);
  }
  
  /* Required methods for KeyListener */
  
  /* This one says what to do when the key is 'pressed'. This method triggers
     once when you first depress the key, and then repeatedly as you continue
     to hold the key down. The rate is determined by your system - different
     computers may have different rates.
   */
  public void keyPressed(KeyEvent e)
  {
    /* Each key is assigned a unique integer code. Luckily, you don't have to
       know the codes - they are stored as constants (public static final
       variables) in the KeyEvent class. For a full list of keys supported,
       you can consult the oracle documentation for the KeyEvent class.
       We will be using the arrow keys for our examples, but here is a small
       list of other useful ones:
        VK_A, VK_B, VK_C, ..., VK_Z for the letter keys
        VK_0, VK_1, VK_2, ..., VK_9 for the number keys
        VK_SPACE for the space bar
     */
    int code = e.getKeyCode();
    
    /* Here, we check to see which code we got. In this example, we will
       update the position of the square appropriately if one of the arrow
       keys (KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, etc.) is pressed.
       Note how we change between sx and sy, and += and -=, depending on
       which key is pressed.
     */
    if(code == KeyEvent.VK_D)
    {
      sx += speed;
    }
    else if(code == KeyEvent.VK_A)
    {
      sx -= speed;
    }
    else if(code == KeyEvent.VK_W)
    {
      sy -= speed;
    }
    else if(code == KeyEvent.VK_S)
    {
      sy += speed;
    }
    repaint();
  }
  
  /* What to do when a key is released. For this example program, we aren't
     putting anything in here, although I recommend that you experiment with
     it - try moving all of the code from the keyPressed method in here
     instead and see how differently your program controls.
   */
  public void keyReleased(KeyEvent e)
  {
  }
  
  /* What to do when a key is typed - this means when your operating system
     determines that a Unicode character has been entered. We use a different
     method here - getKeyChar() instead of getKeyCode().
     For example, if the A key is pressed, without the shift key being held
     down or the caps lock being on, then e.getKeyChar() would produce the
     character 'a'. If the 7 key is pressed with the shift key held, then
     e.getKeyChar() would produce the character '&'.
     
     Generally, unless you want the user to be able to type text, this method
     is not as useful as the other two, and so we will rarely if ever use it
     in this example series.
   */
  public void keyTyped(KeyEvent e)
  {
    /*char c = e.getKeyChar();
    if(c == 'a')
    {
      sx -= speed;
    }
    repaint();*/
  }
}
/* Explanation of the implements keyword:
     If A implements B, then A must contain all of the methods specified in B.
       (Here, A is a class, and B is a list of method prototypes called an
        'interface'. A 'method prototype' is the first line of the method
        defining its name, inputs, and outputs, among other things. Example:
          public static void main(String[] args) is the prototype for main.
     
     In exchange, we get to use A as if it were of type B. A class can only
     extend one class, but it can implement any number of interfaces. This is
     useful because it allows us to fit an object with custom behavior into an
     existing framework of method calls.
     
     In this particular case, a KeyListener is an object that looks for and
     responds to key actions (key presses, releases, and types). We could
     create a separate object to serve this purpose, but here we've actually
     done a bit of a 'hacky' solution to use the KeyboardExample class as its
     own KeyListener. This is why we get the following weird line:
     
       addKeyListener(this);
     
     This line calls the addKeyListener method from the JPanel class, which
     takes in a KeyListener object and routes keyboard input to that object
     whenever a key action occurs while this JPanel is active. The word
     'this' refers to the current object, since we are using it as its own
     KeyListener.
 */