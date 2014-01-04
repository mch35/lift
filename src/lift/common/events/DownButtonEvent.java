package lift.common.events;

/**
 * Zdarzenie przesylane, gdy nacisnieto przycisk w dol na pietrze.
 *
 *
 */
public class DownButtonEvent extends LiftEvent
{
	private final int floor;
	
	public DownButtonEvent(final int floor)
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

