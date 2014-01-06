package lift.view;
import java.awt.*;


public class ElevatorShaft {
  
   int x, y, width, height; 
   Color color;
 
   /** Constructor to setup the GUI */
   public ElevatorShaft(int x, int y, int width, int height, Color color) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.color = color;
   }
 
   /** Paint itself (given the Graphics context) */
   public void paint(Graphics g) {
      g.setColor(color);
      g.drawRect(x, y, width, height);
   }
}