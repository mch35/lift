package lift.common.events;

import lift.common.Direction;

/**
 * Zdarzenie przesylane, gdy winda zmienia kierunek ruchu.
 * W tym przypadku juz nie jest dodatkowo przesylany LiftStopEvent
 * zmiana kierunku ruchu nastepuje po zatrzymaniu
 */
public class ChangeDirectionEvent extends LiftEvent
{
	private final Direction newDirection;
	private final int floor;
	
	public ChangeDirectionEvent(final Direction newDirection, final int floor)
	{
		this.newDirection=newDirection;
		this.floor=floor;
	}
	
	public Direction getNewDirection()
	{
		return newDirection;
	}
	
	public int getFloor()
	{
		return floor;
	}
}
