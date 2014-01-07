package lift.residents;


import lift.common.Direction;
import lift.common.events.GetOffEvent;
import lift.common.events.GetOnEvent;
import lift.common.events.InnerButtonEvent;
import lift.common.events.LiftIsReadyEvent;
import lift.server.Connection;



public class Lift {

	private int maxNumberOfPeople;
	private Direction direction;
	private Person[] inLift = new Person[8];
	private int numberOfPeople = 0;
	private final Connection connection;
	
	public Lift (int n, final Connection connection)
	{
		this.connection = connection;
		maxNumberOfPeople = n;
		numberOfPeople = 0;
	}
	
	
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) 
	{
		this.direction = direction;
	}
	
	/**
	 * Funkcja zapewnia odpowiednie wsiadanie i wysiadanie ludzi na konkretnym pietrze
	 * @param floorNumber
	 * @param floor
	 */
	public void liftOnTheFloor(int floorNumber, Floor floor)
	{
		removePeopleFromLift(floorNumber);
		System.out.println("Winda ma w sobie " + numberOfPeople + " ludzi");
		
		for(int i =0; i < (maxNumberOfPeople - numberOfPeople); i++)
		{
			Person passager = floor.getPassager(direction);
			if(passager == null)
				return;
			connection.send(new InnerButtonEvent(passager.getDestFloor()));
			addPersonToLift(passager);
		}
		connection.send(new LiftIsReadyEvent(floorNumber));
		refreshQueue(floor);
		
	}
	
	/**
	 * Funkcje sprawdzajace czy ponownie nie zostal wcisniety przycisk przywolania windy
	 * @param floor
	 */
	private void refreshQueue(Floor floor)
	{
		if(direction == Direction.DOWN)
			floor.refreshButtonDown();
		else
			floor.refreshButtonUp();
	}

	/**
	 * Funkcja dodaje czlowieka do windy
	 * @param passager
	 */
	private void addPersonToLift(Person passager)
	{
		for(int i =0; i< maxNumberOfPeople; i++)
		{
			if(inLift[i] == null)
			{
				connection.send(new GetOnEvent(passager.getId()));
				inLift[i] = passager;
				numberOfPeople++;
				break;
			}
		}
		
	}
	
	/**
	 * Funkcja dba o to aby kazdy kto chcial wysiasc na tym pietrze to zrobil
	 * @param currentFloor
	 */
	private void removePeopleFromLift(int currentFloor)
	{
		for(int i = 0; i < maxNumberOfPeople; i++)
		{
			if(inLift[i] != null && inLift[i].getDestFloor() == currentFloor)
			{
				connection.send(new GetOffEvent(inLift[i].getId()));
				inLift[i] = null;
				numberOfPeople--;
			}
		}
		connection.send(new LiftIsReadyEvent(currentFloor));

	}
	
}
