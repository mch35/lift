package lift.view;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;


public class Resident {
   
   /** Aktualne wspolrzedne mieszkanca */
   int x, y;
   /** Wymiary mieszkanca */
   int width, height; 
   
   private String imgFileName = "images/man1.jpg"; // relative to project root (or bin)
   private Image img;  // a BufferedImage object
   
 
   /** Konstruktor klasy {@link Resident} */
   // pewnie w konstruktorze trzeba bedzie dodac inne parametry
   public Resident(int x, int y, int width, int height) 
   {
	   	  this.x = x;
	      this.y = y;
	      this.width = width;
	      this.height = height;
	      
	      
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
   public void paint(Graphics g)
   {
	   g.drawImage(img, x, y, null);
	   //g.drawString("ID: "+getId(), x+width-20, y+height-10);
   }

//	public int getId() {
//		return id;
//	}
//	
//	public void setId(int id) {
//		this.id = id;
//	}
}