package lift.view;

import java.util.LinkedList;

import lift.common.Direction;



public class LogicFloor {

	private final int floorNumber;
	private LinkedList<LogicPerson> upQueue;
	private LinkedList<LogicPerson> downQueue;
	
	public LogicFloor(final int floorNumber)
	{
		this.floorNumber = floorNumber;
	}

	public int getFloorNumber() {
		return floorNumber;
	}
	
	public void addPerson(LogicPerson newPerson)
	{
		if(newPerson.getDirection() == Direction.UP)
			upQueue.add(newPerson);
		else
			downQueue.add(newPerson);
	}
	
	/*
	 * Mam dwie koncepcje, albo narysowac po prostu tylu ludzikow ilu jest w obu kolejkach i jak przyjedzie winda, 
	 * wsadzic odpowiednia ilosc z osob najblizej stojacych i oczywisicie odjac ludzi z odpowiedniej kolejki
	 * albo rozrozniac od razu ludzi stojacych i rysowac kazdego czlowieka z osobna i jak przyjedzie winda
	 * to juz tego konkretnego czlowieka przesuwac do windy
	 */
	public void drawFloor()
	{
		int i = 0;
		for(LogicPerson x: upQueue)
		{
			//kazdy czlowiek w kolejce bedzie rysowany jeden za drugim w odleglosci, np , 5
			x.drawPerson(i+=5);
		}
		for(LogicPerson x: downQueue)
		{
			//ludzie z tej kolejki beda rysowani za ludzmi z tamtej
			x.drawPerson(i+=5);
		}
	}
	
	
	/**
	 * Funkcja wsadzajaca czlowieka z odpowiedniej kolejki do windy
	 * @param direction
	 * @param id
	 * @param lift
	 */
	public void getOn(Direction direction, int id, LogicLift lift)
	{
		if(direction==Direction.DOWN)
			lift.addToTheLift(downQueue.removeFirst(),id);
		else if(direction == Direction.UP)
			lift.addToTheLift(upQueue.removeFirst(),id);
		
	
	}
	
	/*
	 * Tutaj jeszcze cos po pierwsze do narysowania tego a po drugie, chyba wazniejsze, jakas logika 
	 * obslugi ludzi w windzie. Eventy sa przystosowane do tego aby po wejsciu do windy nadac czlowiekowi
	 * unikalne id wewnatrz windy bo myslalem ze nie bedzie potrzebne id takie ogolniejsze, teraz juz sam nie wiem
	 * Wojtku proponowal zeby od poczatku do konca czlowiek mial swoje unikalne id. Event oczywisice mozna zmienic.
	 * Mi chodzilo o to zeby GUI nie mialo w sobie tez calej logiki ale nie wiem czy to jest do unikniecia
	 */

	
}
