package lift.residents;

import lift.common.Direction;

public class Person 
{
	private final int startFloor;
	private final int destFloor;
	private final Direction direction;
	private int numberInLift;							//numer rozrozniajacy czlowieka w windzie
	
	public Person(int startFloor, int destFloor)
	{
		this.startFloor = startFloor;
		this.destFloor = destFloor;
		if(startFloor > destFloor)
			direction = Direction.DOWN;
		else
			direction = Direction.UP;
		

			
	}
	
	public void setNumberInLift(int number)
	{
		numberInLift = number;
	}
	
	public Direction getDirection()
	{
		return direction;
	}
	public int getStartFloor()
	{
		return startFloor;
	}
	public int getDestFloor()
	{
		return destFloor;
	}

	
}
