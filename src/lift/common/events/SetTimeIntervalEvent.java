package lift.common.events;

public class SetTimeIntervalEvent extends LiftEvent{
	
	private final int min;
	private final int max;
	
	public SetTimeIntervalEvent(final int minTime, final int maxTime)
	{
		min = minTime;
		max = maxTime;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

}
