package lift.common.events;

/**
 * Zdarzenie przesylane gdy winda zatrzymala sie na danym pietrze.
 *
 */
public class LiftStopEvent extends LiftEvent
{
	private final int floor;

	public LiftStopEvent(final int floor)
	{
		this.floor = floor;
	}
	
	/**
	 * @return the floor
	 */
	public int getFloor()
	{
		return floor;
	}
}
