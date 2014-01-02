package lift.common;

import lift.common.Direction;

public class ChangeDirectionEvent extends LiftEvent {
	private final Direction newDirection;
	public ChangeDirectionEvent(final Direction newDirection) {
		this.newDirection=newDirection;
	}
	public Direction getNewDirection() {
		return newDirection;
	}

}
