package lift.residents;

import java.util.LinkedList;

import lift.common.Direction;
import lift.common.events.DownButtonEvent;
import lift.common.events.GeneratePersonEvent;
import lift.common.events.UpButtonEvent;
import lift.server.Connection;



public class Floor {
	
	private final int number;
	private Boolean upButton;
	private Boolean downButton;
	private LinkedList<Person> queueUp;
	private LinkedList<Person> queueDown;

	/** Polaczenie z serwerem */
	private final Connection connection;
	
	public Floor(int floorNumber, final Connection connection)
	{
		this.connection = connection;
		setUpButton(new Boolean(false));
		setDownButton(new Boolean(false));
		queueUp = new LinkedList<Person>();
		queueDown = new LinkedList<Person>();
		this.number = floorNumber;
	}
	
	/**
	 * Sprawdza czy trzeba wcisnac przycisk przywolujacy winde jadaca w gore i ewentualnie "wciska" go
	 */
	public void refreshButtonUp()
	{
		if(!queueUp.isEmpty())
			connection.send(new UpButtonEvent(number));
			
	}
	
	/**
	 * Sprawdza czy trzeba wcisnac przycisk przywolujacy winde jadaca w dol i ewentualnie "wciska" go
	 */
	public void refreshButtonDown()
	{
		if(!queueDown.isEmpty())
			connection.send( new DownButtonEvent(number));
	}
	/**
	 * Funkcja zwracajaca czlowieka z kolejki ludzi jadacych w odpowiednim kierunku
	 * @param direction - kierunek jazdy winda
	 * @return Pasazer ktory czeka w odpowiedniej kolejce
	 */
	public Person getPassager(Direction direction)
	{
		if(queueDown.size() > 0)
		{
			if(direction == Direction.DOWN)
				return queueDown.removeFirst();
		}
		if(!queueUp.isEmpty() && direction == Direction.UP)
			return queueUp.removeFirst();
		
		return null;
	}
	/*@Override
	public String toString()
	{
		//String kaszanka = new String("Pietro " + this.number + "\n");
		//kaszanka += "Liczba osob w kolejce do gory: "	+ queueUp.size() + "\n";
		//kaszanka += "Liczba osob w kolejce do dolu: "	+ queueDown.size() + "\n";
		//kaszanka += "\n";
		return kaszanka;
	}*/
	
	/**
	 * Dodaje czlowieka do odpowiedniej kolejki na danym pietrze oraz dba o ewentualne
	 * wcisniecie guzika przywolania windy
	 * @param person
	 */
	public void addPerson(Person person)
	{
		connection.send(new GeneratePersonEvent(person.getStartFloor(), person.getId(), person.getDestFloor()));
		if(person.getDirection() == Direction.DOWN)
		{
			queueDown.addLast(person);
			
			
				connection.send(new DownButtonEvent(getFloorNumber()));
				setDownButton(true);
			
		}
		else
		{
			queueUp.addLast(person);
			
				connection.send(new UpButtonEvent(getFloorNumber()));
				setUpButton(true);
			
		}
	}

	/**
	 * @return Czy przycisk przywolania windy  (jadacej w gore) na tym pietrze jest wcisniety
	 */
	public Boolean getUpButton() {
		return upButton;
	}
	
	/**
	 * Ustawia przycisk przywolania windy jadacej w gore na odpowiednia wartosc (bool)
	 * @param upButton
	 */
	private void setUpButton(Boolean upButton) {
		this.upButton = upButton;
	}

	/**
	 * @return Czy przycisk przywolania windy  (jadacej w dol) na tym pietrze jest wcisniety
	 */
	public Boolean getDownButton() {
		return downButton;
	}

	/**
	 * Ustawia przycisk przywolania windy jadacej w dol na odpowiednia wartosc (bool)
	 * @param downButton
	 */
	private void setDownButton(Boolean downButton) {
		this.downButton = downButton;
	}
	
	/**
	 * 
	 * @return numer aktualnego pietra
	 */
	public int getFloorNumber()
	{
		return number;
	}
	

}
