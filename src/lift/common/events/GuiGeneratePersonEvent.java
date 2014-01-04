package lift.common.events;

/**
 * Zdarzenie przesylane, gdy wcisnieto przycisk Add new Resident
 * 
 *
 */
public class GuiGeneratePersonEvent extends LiftEvent{
	
	private final int homeFloor;
	private final int destinationFloor;
	
	public GuiGeneratePersonEvent(final int homefloor, final int destinationfloor)
	{
		homeFloor = homefloor;
		destinationFloor =destinationfloor;
	}

	public int getHomeFloor() {
		return homeFloor;
	}

	public int getDestinationFloor() {
		return destinationFloor;
	}
	
	

}
