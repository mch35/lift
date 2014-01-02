package lift.common.events;

public class InnerButtonEvent extends LiftEvent {

	private final int floor;
	public InnerButtonEvent(final int floor) {
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
