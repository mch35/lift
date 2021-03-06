package lift.driver;

import lift.common.events.LiftEvent;

/** An interface which has abstract method to execute proper 
 * action after receiving particular event */

public interface LiftAction {
	public abstract void execute(LiftEvent e);
}
