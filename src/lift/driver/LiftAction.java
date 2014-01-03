package lift.driver;

import lift.common.events.LiftEvent;

public interface LiftAction {
	public abstract void execute(LiftEvent e);
}
