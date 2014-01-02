package lift.view;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;


public class Man {
   // Variables (package access)
   int x, y, width, height; 
   
   private String imgFileName = "images/man1.jpg"; // relative to project root (or bin)
   private Image img;  // a BufferedImage object
   
   private int sourceFloor;
   private int destinationFloor;
   private int id;
 
   /** Constructor to setup the GUI */
   public Man(int x, int y, int width, int height, int sourceFloor, int destinationFloor) 
   {
	   	  this.x = x;
	      this.y = y;
	      this.width = width;
	      this.height = height;
	      this.sourceFloor = sourceFloor;
	      this.destinationFloor = destinationFloor;
	      this.id = 5;
	      
	   // Load an external image via URL
	      loadImage();
   }

   private void loadImage() {
	URL imgUrl = getClass().getClassLoader().getResource(imgFileName);
	  if (imgUrl == null) {
	     System.err.println("Couldn't find file: " + imgFileName);
	  } else {
	     try {
	        img = ImageIO.read(imgUrl);
	     } catch (IOException ex) {
	        ex.printStackTrace();
	     }
	  }
}
 
   /** Paint itself (given the Graphics context) */
   public void paint(Graphics g) {
	   g.drawImage(img, x, y, null);
	   g.drawString("ID: "+getId(), x+width-20, y+height-10);
   }

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}
}