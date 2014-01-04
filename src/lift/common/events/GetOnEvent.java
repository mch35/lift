package lift.common.events;

/**
 * Zdarzenie przesylane gdy wsiedli ludzie do windy.
 *
 */
public class GetOnEvent extends LiftEvent
{	
	private final int numberInLift;
	
	public GetOnEvent(int number)
	{
		numberInLift = number;
	}

	public int getNumberInLift()
	{
		return numberInLift;
	}
}
