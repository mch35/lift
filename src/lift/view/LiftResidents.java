/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lift.view;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * klasa rysujaca mieszkañców w windzie
 * @author Pawel
 */
public class LiftResidents extends JPanel{
    private final LinkedList<Resident> people;
    public static int IMAGE_WIDTH;
    public static int IMAGE_HEIGHT;
    private String imgFileName1 = "images/man1.jpg"; // relative to project root (or bin)
    private String imgFileName2 = "images/man2.jpg"; // relative to project root (or bin)
    private Image img;  // a BufferedImage object

    public LiftResidents(LinkedList<Resident> people, final int imageX, int imageY) {
        this.people = people;
        IMAGE_WIDTH = imageX;
        IMAGE_HEIGHT = imageY;
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
    
    @Override
    public void paint(Graphics g)
    {
        int tempX, tempY;
        for(int i = 0; i < people.size(); i++)
        {
            if(i%2 == 0)
            {
                tempX = 5;
                tempY = (IMAGE_HEIGHT + 15) * i/2;
            }
            else
            {
                tempX = IMAGE_WIDTH + 5;
                tempY = (IMAGE_HEIGHT + 15) * (i - 1)/2;
            }
            
            g.drawImage(img, tempX, tempY, null);
            g.drawString("ID: "+ people.get(i).getId()+" Dest: "+people.get(i).getDestFloor(), tempX+10, tempY+IMAGE_HEIGHT + 10);
        }
    }
    
}
