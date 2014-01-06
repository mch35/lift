package lift.view;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

import lift.common.Direction;



public class LogicFloor
{

	private final int floorNumber;
	private final ArrayList<Resident> people;
        boolean up, down;
	
	public LogicFloor(final int floorNumber)
	{
		this.floorNumber = floorNumber;
		people = new ArrayList<Resident>();
                up = down = false;
	}

	public int getFloorNumber()
	{
		return floorNumber;
	}
	
	/**
	 * Funkcja dodajaca czlowieka pietru. Zakladam ludzkie podejscie do kolejnsci w kolejce, nie ma osoby
	 * "zerowej" w kolejce - pierwsza osoba jest pierwsza w kolejce
	 * @param id
	 * @param homeFloor
	 */
	public Resident addPerson(final int id, final int homeFloor, final int destFloor)
	{
		Resident newResident = new Resident(id,homeFloor,people.size()+1, destFloor, this.floorNumber);
		people.add(newResident);
		return newResident;
	}
	
	
	
	
	public void getOn(Resident res)
	{

		people.remove(res);
	
	}
   public void setDown(boolean down) {
       
       this.down = down;
   }
    
   public void setUp(boolean up) {
       
       this.up = up;
   }
   
   public void paint(Graphics g) {
      g.setColor(Color.BLACK);
      g.drawString("Up", 905, 126*floorNumber + 55);
      g.drawString("Dw", 905, 126*floorNumber + 80);
      if(up == false)
      {
          g.setColor(Color.GRAY);
      }
      else
      {
          g.setColor(Color.RED);
      }
      g.fillOval(925, 126*floorNumber + 40, 20, 20);
      if(down == false)
      {
          g.setColor(Color.GRAY);
      }
      else
      {
          g.setColor(Color.RED);
      }
      g.fillOval(925, 126*floorNumber + 65, 20, 20);
   }
	
	/*
	 * Tutaj jeszcze cos po pierwsze do narysowania tego a po drugie, chyba wazniejsze, jakas logika 
	 * obslugi ludzi w windzie. Eventy sa przystosowane do tego aby po wejsciu do windy nadac czlowiekowi
	 * unikalne id wewnatrz windy bo myslalem ze nie bedzie potrzebne id takie ogolniejsze, teraz juz sam nie wiem
	 * Wojtku proponowal zeby od poczatku do konca czlowiek mial swoje unikalne id. Event oczywisice mozna zmienic.
	 * Mi chodzilo o to zeby GUI nie mialo w sobie tez calej logiki ale nie wiem czy to jest do unikniecia
	 */

	
}
