package lift.view;

import java.awt.Color;
import java.awt.Graphics;


public class Building {
   // Variables (package access)
   int x, y, width, height; // rectangle (for illustration)
   Color color = Color.BLACK; // color of the object
   
   int floorNumber = 4;
 
   /** Constructor to setup the GUI */
   public Building(int x, int y, int width, int height, Color color) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.color = color;
   }
 
   /** Paint itself (given the Graphics context) */
   public void paint(Graphics g) {
      g.setColor(color);
      g.drawRect(0, 0, width, height);
      
      //createFloors
      int y_position = 0;
      String string = "FLOOR ";
		for(int i=0; i<floorNumber; ++i)
		{
			g.drawRect(0, y_position, 640, 126);
			g.drawString(string+(floorNumber-i), 10, y_position+12);
			y_position += 126;
		}
   }
}