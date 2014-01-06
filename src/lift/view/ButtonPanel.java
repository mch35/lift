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
public class ButtonPanel
{
   private LogicLift lift;
   JFrame pole;
   public static final Color CANVAS_BG_COLOR = new Color(183, 221, 230);
   private ButtonPanelView buttonPanelView;
   
   public ButtonPanel(LogicLift li, int numberOfFloors)
   {
       lift = li;
       buttonPanelView = new ButtonPanelView( lift.getPeople2(), numberOfFloors);
       pole = new JFrame("Panel");
       pole.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       pole.add(buttonPanelView);
       pole.pack();
       pole.setSize(150, numberOfFloors * 37);
       pole.setBackground(CANVAS_BG_COLOR);
       pole.setLocation(1250, 10);
       pole.setResizable(false);
       pole.setVisible(true);
   }

}
