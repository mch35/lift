package lift.common.events;

public class GetOffEvent extends LiftEvent{
	
	private final int numberInLift;
	
	public GetOffEvent(int numerInLift)
	{
		this.numberInLift = numerInLift;
	}

	public int getNumberInLift() {
		return numberInLift;
	}

}
