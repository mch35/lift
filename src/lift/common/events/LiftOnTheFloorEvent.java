package lift.common.events;

/**
 * Zdarzenie przesylane gdy winda dojechala na dane pietro.
 *
 *
 */
public class LiftOnTheFloorEvent extends LiftEvent
{
	private final int floor;

	public LiftOnTheFloorEvent(final int floor)
	{
		this.floor=floor;
	}
	
	/**
	 * @return the floor
	 */
	public int getFloor()
	{
		return floor;
	}
}
