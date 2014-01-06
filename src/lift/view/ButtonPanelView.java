/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lift.view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import javax.swing.JPanel;

/**
 *
 * @author Pawel
 */
public class ButtonPanelView extends JPanel
{
    private final LinkedList<Resident> people;
    private final int numberOfFloors;

    public ButtonPanelView(LinkedList<Resident> people, int numberOfFloors) {
        this.people = people;
        this.numberOfFloors = numberOfFloors;
    }
    
    
    @Override
    public void paint(Graphics g)
    {
        int tempX, tempY;
        for(int i = 0; i < numberOfFloors; i++)
        {
            if(i%2 == 0)
            {
                tempX = 5;
                tempY = 50 * i/2 + 15;
            }
            else
            {
                tempX = 90;
                tempY = 50 * (i - 1)/2 + 15;
            }
            g.setColor(Color.BLACK);
            g.drawString("Pietro: "+i, tempX, tempY);;
            g.setColor(Color.GRAY);
            for(Resident person: people)
            {
                if(person.getDestFloor() == i)
                    g.setColor(Color.RED);
            }
            g.fillOval(tempX + 10, tempY + 5, 20, 20);
        }
        
    }
    
}
