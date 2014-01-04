package lift.common.events;

/**
 * Zdarzenie przesylane gdy wcisnieto przycisk w gore na pietrze.
 *
 */
public class UpButtonEvent extends LiftEvent
{
	private final int floor;
	
	public UpButtonEvent(final int floor)
	{
		super();
		this.floor=floor;
	}

	/**
	 * @return the floorNumber
	 */
	public int getFloor()
	{
		return floor;
	}
}
