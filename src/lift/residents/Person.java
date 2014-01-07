package lift.residents;

import lift.common.Direction;

public class Person 
{
	private final int startFloor;
	private final int destFloor;
	private final Direction direction;
	private final int id;							//numer rozrozniajacy czlowieka w windzie
	

	
	public Person(final int startFloor,final int destFloor, final int id)
	{
		this.startFloor = startFloor;
		this.destFloor = destFloor;
		if(startFloor > destFloor)
			direction = Direction.DOWN;
		else
			direction = Direction.UP;
		
		this.id = id;
		

			
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

	public int getId() {
		return id;
	}

	
}
