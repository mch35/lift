package lift.view;

import java.util.LinkedList;
import lift.common.Direction;

public class LogicLift {
	
	private LogicPerson[] people = new LogicPerson[8];
	private int currentFloor;
	private Direction currentDirection;
	private final LinkedList<Resident> people2;

    public LinkedList<Resident> getPeople2() {
        return people2;
    }
	
	LogicLift()
	{
		for(int i =0;i<8;i++)
			people[i] = null;
                people2 = new LinkedList<Resident>();
	}

/**
 * Funkcja dodaje czlowieka do wirtualnej windy nadajac Mu unikalne id (unikalne wewnatrz windy)
 * @param person
 * @param id
 */
	void addToTheLift(Resident person)
	{
	//	if(people[id] != null)
	//		System.out.println("Jeden z ludzi wypadl z windy podczas jazdy");
	//	people[id] = person;
	//	person.setIdInLift(id);
                people2.add(person);
	}
	
	void getOff(int id)
	{
		//TODO: Metoda rysujaca wysiadanie z windy, ewentualnie znikanie danej osoby
		people[id] = null;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}


	public void setCurrentFloor(int currentFloor) {
		this.currentFloor = currentFloor;
	}


	public Direction getCurrentDirection() {
		return currentDirection;
	}


	public void setCurrentDirection(Direction currentDirection) {
		this.currentDirection = currentDirection;
	}

}
