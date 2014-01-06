package lift.view;
import java.awt.*;
import java.util.LinkedList;



public class ElevatorBox {
   // Variables (package access)
   int x, y, width, height; // rectangle (for illustration)
   Color color = Color.BLACK; // color of the object
   private final LinkedList<Resident> people;

    public LinkedList<Resident> getPeople() {
        return people;
    }
 
   /** Constructor to setup the GUI */
   public ElevatorBox(int x, int y, int width, int height, Color color) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.color = color;
      people = new LinkedList<Resident>();
   }
   
   public Resident addPerson(final int id, final int homeFloor, final int destFloor)
    {
            Resident newResident = new Resident(id,homeFloor,people.size()+1, destFloor, 0);
            people.add(newResident);
            return newResident;
    }
   
   public void getOff(Resident res)
    {

            people.remove(res);

    }
   
   /** Paint itself (given the Graphics context) */
   public void paint(Graphics g) {
      g.setColor(color);
      g.fillRect(x, y, width, height);
   }
   
   public void openTheDoor() {
	   
	   
   }
}