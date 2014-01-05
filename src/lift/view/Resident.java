package lift.view;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;


public class Resident {
   
   /** Wspolrzedne mieszkanca stajacego w kolejce */
   int x, y;
   
   /** Aktualne wspolrzedne mieszkanca */
   int tempX, tempY;
   
   /** Wymiary mieszkanca */
   private final int width = 85;
   private final int height = 126; 
   
   private String imgFileName = "images/man1.jpg"; // relative to project root (or bin)
   private Image img;  // a BufferedImage object
   
   private final int homeFloor;
   private final int id;
   
 
   /** Konstruktor klasy {@link Resident} */
   public Resident(final int id, final int homeFloor, final int numerInQueue) 
   {
	      this.id = id;
	      this.homeFloor = homeFloor;
	      
	      setYCoordinate(homeFloor);
	      this.y = getY();
	      
	      setXCoordinate(numerInQueue);
	      this.x = getX();
	      
	      this.tempX = 5;
	      this.tempY = getY();
	      
	      loadImage();
   }

   private void loadImage() 
   {
	   URL imgUrl = getClass().getClassLoader().getResource(imgFileName);
	   if (imgUrl == null) 
	   {
		   System.err.println("Couldn't find file: " + imgFileName);
	   } 
	   else
	   {
	     try 
	     {
	        img = ImageIO.read(imgUrl);
	     } 
	     catch (IOException ex)
	     {
	        ex.printStackTrace();
	     }
	   }
   }
 
   /** Paint itself (given the Graphics context) */
   public void paint(Graphics g)
   {
	   g.drawImage(img, tempX, tempY, null);
	   g.drawString("ID: "+getId(), tempX+width-20, tempY+height-10);
   }

	public int getId() {
		return this.id;
	}
	
	public void setXCoordinate(int number)
	{
		this.x = 900-(number+1)*width;
	}
	
	public void setYCoordinate(int homeFloor)
	{
		this.y = (4-homeFloor)*height;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}

}