/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lift.view;

import java.awt.*;
import javax.swing.JFrame;

/**
 *
 * @author Pawel
 */
public class LiftInsideSimulation extends JFrame implements Runnable
{
   public static int IMAGE_WIDTH = 85;
   public static int IMAGE_HEIGHT = 126;
   private LogicLift lift;
   JFrame pole;
   public static final Color CANVAS_BG_COLOR = new Color(183, 221, 230);
   private LiftResidents liftResidents;
   
   public LiftInsideSimulation(final int imageX, int imageY, LogicLift li)
   {
       IMAGE_WIDTH = imageX;
       IMAGE_HEIGHT = imageY;
       lift = li;
       liftResidents = new LiftResidents(lift.getPeople2(), IMAGE_WIDTH, IMAGE_HEIGHT);
       pole = new JFrame("");
       pole.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       pole.add(liftResidents);
       pole.pack();
       pole.setSize(IMAGE_WIDTH*2+10, (IMAGE_HEIGHT+30)*4+10);
       pole.setBackground(CANVAS_BG_COLOR);
       pole.setResizable(false);
       pole.setVisible(true);
   }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
