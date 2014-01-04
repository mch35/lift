package lift.common.events;

/**
 * Zdarzenie przesylane gdy winda jest gotowa ?do czego?.
 *
 */
public class LiftIsReadyEvent extends LiftEvent
{
	private final int floor;
	
	public LiftIsReadyEvent(final int floor)
	{
		super();
		this.floor=floor;
	}

	public int getFloor()
	{
		return floor;
	}
}
