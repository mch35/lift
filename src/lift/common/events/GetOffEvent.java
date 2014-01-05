package lift.common.events;

/**
 * Zdarzenie przesylane gdy wysiadl czlowiek z windy.
 *
 *
 */
public class GetOffEvent extends LiftEvent
{	
	private final int id;
	
	public GetOffEvent(int numerInLift)
	{
		this.id = numerInLift;
	}

	public int getid()
	{
		return id;
	}
}
