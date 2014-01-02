package lift.common;

public class LiftOnTheFloor extends LiftEvent
{
	private final int floorNumber;
	
	public LiftOnTheFloor(int floorNumber)
	{
		this.floorNumber = floorNumber;
	}

	public int getFloorNumber() {
		return floorNumber;
	}


	
	
}
