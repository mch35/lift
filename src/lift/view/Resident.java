package lift.view;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;


public class Resident {
   
   /** Wspolrzedne mieszkanca stajacego w kolejce */
   int x, y;
   
   /** Aktualne wspolrzedne mieszkanca */
   int tempX, tempY;
   
   /** Wymiary mieszkanca */
   private final int width = 85;
   private final int height = 126; 
   
   /** Ilosc pieter w budynku */
   private final int floorNumber;
   
   private String imgFileName1 = "images/man1.jpg"; // relative to project root (or bin)
   private String imgFileName2 = "images/man2.jpg"; // relative to project root (or bin)
   private Image img;  // a BufferedImage object
   
   private final int homeFloor;
   private final int destFloor;
   private final int id;
   
 
   /** Konstruktor klasy {@link Resident} */
   public Resident(final int id, final int homeFloor, final int numerInQueue, 
		   final int destFloor, final int floorNumber) 
   {
	      this.id = id;
	      this.homeFloor = homeFloor;
          this.destFloor = destFloor;
          this.floorNumber = floorNumber;
	      
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
	   URL imgUrl;
	   Random r = new Random();
	   int x = r.nextInt(2);
	   if(x == 1)
	   {
		   imgUrl = getClass().getClassLoader().getResource(imgFileName1);
	   }
	   else
	   {
		   imgUrl = getClass().getClassLoader().getResource(imgFileName2);
	   }
	     
	   if (imgUrl == null) 
	   {
		   System.err.println("Couldn't find file");
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
	   g.drawString("ID: "+getId()+" Dest: "+getDestFloor(), tempX+width-20, tempY+height-10);
   }

	public int getId() {
		return this.id;
	}
	
	/** Ustawia wspolrzedna x polozenia mieszkanca stojacego w kolejce */
	public void setXCoordinate(int number)
	{
		this.x = 900-(number+1)*width;
	}
	
	/** Ustawia wspolrzedna y polozenia mieszkanca stojacego w kolejce */
	public void setYCoordinate(int homeFloor)
	{
		this.y = (this.floorNumber-homeFloor)*height;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
        
        public int getDestFloor()
	{
		return destFloor;
	}

}