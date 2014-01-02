package lift.common;

public class LiftIsReadyEvent extends LiftEvent {

	private final int floor;
	
	public LiftIsReadyEvent(final int floor) {
		super();
		this.floor=floor;
	}

	public int getFloor(){
		return floor;
	}
}
