package lift.common.events;

/**
 * Zdarzenie przesylane gdy wsiadl czlowiek do windy.
 *
 *
 */
public class GetOnEvent extends LiftEvent
{	
	private final int id;
	
	public GetOnEvent(int number)
	{
		id = number;
	}

	public int getid()
	{
		return id;
	}
}
