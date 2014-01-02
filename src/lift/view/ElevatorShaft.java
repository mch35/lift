package lift.view;
import java.awt.*;


public class ElevatorShaft {
   // Variables (package access)
   int x, y, width, height; // rectangle (for illustration)
   Color color = Color.BLACK; // color of the object
 
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