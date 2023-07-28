import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.Random;

public class AimTrainer extends JPanel implements MouseListener
{
  Target target;
  
  int score = 0;
  int highscore = 0;
  
  Random rng;

  public AimTrainer()
  {
    target = new Target(100, 500, 50);
    rng = new Random();
    addMouseListener(this);
    setFocusable(true);
  }
  
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    
    g.setFont(new Font("Arial", Font.PLAIN, 72));
    drawFancyString(g, "AIMTRAINER3000", 85, 100, new Color(0xffff));

    target.drawTo(g);
    
    g.setColor(Color.BLACK);
    g.setFont(new Font("Helvetica", Font.PLAIN, 36));
    g.drawString("Score: " + score, 600, 200);
    
    g.drawString("Highest: " + highscore, 600, 400);
  }
  
  public void drawFancyString(Graphics g, String text, int x, int y, Color c)
  {
    g.setColor(Color.BLACK);
    for(int i=0; i<3; i+=2)
    {
      for(int j=0; j<3; j+=2)
      {
        g.drawString(text, x + i, y + j);
      }
    }
    g.setColor(c);
    g.drawString(text, x + 1, y + 1);
  }
  
  public void mousePressed(MouseEvent e)
  {
    int button = e.getButton();
    if(button == MouseEvent.BUTTON1)
    {
      if( target.clicked(e.getX(),e.getY()) )
      {
        score++;
        highscore = Math.max(score, highscore);
        target.respawn(rng);
      }
      else
      {
        score = 0;
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

class Target
{
  private int x;
  private int y;
  private int diameter;
  private int radius;
  private Color color;
  
  public Target(int nx, int ny, int nd)
  {
    x = nx;
    y = ny;
    diameter = nd;
    radius = diameter/2;
    color = Color.BLACK;
  }
  
  public void drawTo(Graphics g)
  {
    g.setColor(color);
    g.fillOval(x, y, diameter, diameter);
  }
  
  public int centerX()
  {
    return x + radius;
  }
  
  public int centerY()
  {
    return y + radius;
  }
  
  public boolean clicked(int x, int y)
  {
    int xdif = x - centerX();
    int ydif = y - centerY();
    return (xdif*xdif + ydif*ydif) <= radius*radius; // x^2 + y^2 <= r^2
  }
  
  public void respawn(Random rng)
  {
    x = rng.nextInt(500);
    y = rng.nextInt(450) + 100;
    
    radius = rng.nextInt(15) + 20;
    diameter = radius*2;
    color = new Color(rng.nextInt(200) << 16 + rng.nextInt(200) << 8 + rng.nextInt(200));
  }
}