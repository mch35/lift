package lift.view;
import java.awt.*;


public class ElevatorBox {
   // Variables (package access)
   int x, y, width, height; // rectangle (for illustration)
   Color color = Color.BLACK; // color of the object
 
   /** Constructor to setup the GUI */
   public ElevatorBox(int x, int y, int width, int height, Color color) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.color = color;
   }
 
   /** Paint itself (given the Graphics context) */
   public void paint(Graphics g) {
      g.setColor(color);
      g.fillRect(x, y, width, height);
   }
   
   public void openTheDoor() {
	   
	   
   }
}