package lift.common;

/**
 * Event mowiacy o tym ze ktos wysiada z windy
 * Osoba wysiadajaca jest rozroznialna poprzez numer w windzie
 * @author Tomek
 *
 */
public class GetOffEvent extends LiftEvent{
	
	private final int numberInLift;
	
	public GetOffEvent(int numberInLift)
	{
		this.numberInLift = numberInLift;
	}

	public int getNumberInLift() {
		return numberInLift;
	}




}
