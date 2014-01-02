package lift.common.events;

public class GetOnEvent extends LiftEvent{
	
	private final int numberInLift;
	
	public GetOnEvent(int number)
	{
		numberInLift = number;
	}

	public int getNumberInLift() {
		return numberInLift;
	}

}
