package lift.common.events;

public class UpButtonEvent extends LiftEvent{
	private final int floor;
	
	public UpButtonEvent(final int floor){
		super();
		this.floor=floor;
	}

	/**
	 * @return the floorNumber
	 */
	public int getFloor() {
		return floor;
	}
	
}
