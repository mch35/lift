package lift.common.events;

/**
 * Zdarzenie przesylane gdy wysiedli ludzie z windy.
 *
 *
 */
public class GetOffEvent extends LiftEvent
{	
	private final int numberInLift;
	
	public GetOffEvent(int numerInLift)
	{
		this.numberInLift = numerInLift;
	}

	public int getNumberInLift()
	{
		return numberInLift;
	}
}
