package lift.common;

public class LiftOnTheFloorEvent extends LiftEvent {
	private final int floor;
	public LiftOnTheFloorEvent(final int floor) {
		this.floor=floor;
	}
	/**
	 * @return the floor
	 */
	public int getFloor() {
		return floor;
	}

}
