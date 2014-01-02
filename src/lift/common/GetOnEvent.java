package lift.common;

/**
 * Event mowiacy o tym ze jakas osoba wsiadla do windy, bedzie to zawsze pierwsza osoba z kolejki 
 * w danym kierunku na danym pietrze. Nadaje sie jej numer w windzie aby GUI moglo przy wysiadaniu odroznic
 * go od innych pasazerow
 * @author Tomek
 *
 */
public class GetOnEvent extends LiftEvent{
	
	private final int numberInLift;
	
	public GetOnEvent(int numberInLift)
	{
		this.numberInLift = numberInLift;
	}

	public int getNumberInLift() {
		return numberInLift;
	}


	

	

}
