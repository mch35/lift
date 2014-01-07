package lift.view;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

import lift.common.Direction;
import lift.view.Consts;


public class LogicFloor
{

	private final int floorNumber;
	private final int numberOfFloors;
	private final ArrayList<Resident> people;

        public ArrayList<Resident> getPeople() {
            return people;
        }
        boolean up, down, lightUp, lightDown;
	
	public LogicFloor(final int floorNumber, final int numberOfFloors)
	{
		this.floorNumber = floorNumber;
		this.numberOfFloors=numberOfFloors;
		people = new ArrayList<Resident>();
                lightUp = lightDown = up = down = false;
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
	{ // TODO: na sztywno jest ilosc pieter wpisana !!!
		Resident newResident = new Resident(id,homeFloor,people.size()+1, destFloor, numberOfFloors);
		people.add(newResident);
		return newResident;
	}
	
	
	
	
	public void getOn(Resident res)
	{

		people.remove(res);
	
	}
	
	public void getOff(Resident res)
	{

		people.add(res);
	
	}
	
   public void setDown(boolean down) {
       
       this.down = down;
   }
    
   public void setUp(boolean up) {
       
       this.up = up;
   }
   
   public void setLightDown(boolean down) {
       
       this.lightDown = down;
   }
    
   public void setLightUp(boolean up) {
       
       this.lightUp = up;
   }
   
   public void paint(Graphics g) {
      g.setColor(Color.BLACK);
      int[] x = {924,932,940};
      int[] y = {126*floorNumber + 55, 126*floorNumber + 35, 126*floorNumber + 55};
      int[] yy={126*floorNumber + 60, 126*floorNumber + 80, 126*floorNumber + 60};
      if(floorNumber!=0){
      	g.drawString("Up", 770, 126*floorNumber + 55);
      }
      if(floorNumber!=numberOfFloors-1){
      	g.drawString("Dw", 770, 126*floorNumber + 80);
      }
      	if(up == false)
      {
          g.setColor(Color.GRAY);
      }
      else
      {
          g.setColor(Color.RED);
      }
      if(floorNumber!=0){
      		g.fillOval(790, 126*floorNumber + 40, 20, 20);
      }
      if(down == false)
      {
          g.setColor(Color.GRAY);
      }
      else
      {
          g.setColor(Color.RED);
      }
      if(floorNumber!=numberOfFloors-1){
      	g.fillOval(790, 126*floorNumber + 65, 20, 20);
      }
      
    	if(lightUp == false)
    {
        g.setColor(Color.GRAY);
    }
    else
    {
        g.setColor(Color.GREEN);
    }
    if(floorNumber!=0){
  	  g.fillPolygon(x, y, 3 );
    }
      
      if(lightDown == false)
      {
          g.setColor(Color.GRAY);
      }
      else
      {
          g.setColor(Color.GREEN);
      }
      if(floorNumber!=numberOfFloors-1){
    	  g.fillPolygon(x, yy, 3 );
      }
   }
	
	/*
	 * Tutaj jeszcze cos po pierwsze do narysowania tego a po drugie, chyba wazniejsze, jakas logika 
	 * obslugi ludzi w windzie. Eventy sa przystosowane do tego aby po wejsciu do windy nadac czlowiekowi
	 * unikalne id wewnatrz windy bo myslalem ze nie bedzie potrzebne id takie ogolniejsze, teraz juz sam nie wiem
	 * Wojtku proponowal zeby od poczatku do konca czlowiek mial swoje unikalne id. Event oczywisice mozna zmienic.
	 * Mi chodzilo o to zeby GUI nie mialo w sobie tez calej logiki ale nie wiem czy to jest do unikniecia
	 */

//   public void paintLights(Graphics g) {
//	      g.setColor(Color.BLACK);
//	      int[] x = {924,932,940};
//	      int[] y = {126*floorNumber + 55, 126*floorNumber + 35, 126*floorNumber + 55};
//	      int[] yy={126*floorNumber + 35, 126*floorNumber + 35, 126*floorNumber + 35};
//	      if(floorNumber!=0){
//	      	g.fillPolygon(x, yy, 3 );
//	      	//g.fillPolygon(new int[]{924,932,940}, {126*floorNumber + 55, 126*floorNumber + 35, 126*floorNumber + 55}, 3);
//	      }
//	      if(floorNumber!=numberOfFloors-1){
//	    	  g.fillPolygon(x, y, 3 );
//	      }
//	      	if(up == false)
//	      {
//	          g.setColor(Color.GRAY);
//	      }
//	      else
//	      {
//	          g.setColor(Color.RED);
//	      }
//   }
}
