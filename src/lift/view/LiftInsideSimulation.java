/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lift.view;

import java.awt.*;
import javax.swing.JFrame;

/**
 * symulacja okna z mieszkañcami znajdujacymi sie w windzie
 * @author Pawel
 */
public class LiftInsideSimulation
{
   public static int IMAGE_WIDTH;
   public static int IMAGE_HEIGHT;
   private LogicLift lift;
   JFrame pole;
   public static final Color CANVAS_BG_COLOR = Color.LIGHT_GRAY;
   private LiftResidents liftResidents;
   
   public LiftInsideSimulation(final int imageX, int imageY, LogicLift li)
   {
       IMAGE_WIDTH = imageX;
       IMAGE_HEIGHT = imageY;
       lift = li;
       liftResidents = new LiftResidents(lift.getPeople2(), IMAGE_WIDTH, IMAGE_HEIGHT);
       pole = new JFrame("Inside");
       pole.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       pole.add(liftResidents);
       pole.pack();
       pole.setSize(IMAGE_WIDTH*2+15, (IMAGE_HEIGHT+30)*4+10);
       pole.setBackground(CANVAS_BG_COLOR);
       pole.setLocation(1000, 10);
       pole.setResizable(false);
       pole.setVisible(true);
   }
   
    

}
