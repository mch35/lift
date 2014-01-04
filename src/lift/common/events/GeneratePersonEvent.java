package lift.common.events;

import lift.common.Direction;

/**
 * Zdarzenie przesylane, gdy pojawil sie nowy czlowiek.
 *
 */
public class GeneratePersonEvent extends LiftEvent
{
	private final int homeFloor;
	private final Direction direction;
	
	public GeneratePersonEvent(int startFloor, Direction direction)
	{
		this.direction = direction;
		homeFloor = startFloor;
	}

	public int getHomeFloor()
	{
		return homeFloor;
	}

	public Direction getDirection()
	{
		return direction;
	}
}
