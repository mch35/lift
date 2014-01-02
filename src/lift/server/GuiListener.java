/**
 * 
 */
package lift.server;

import lift.common.LiftEvent;
import lift.common.SimulationStopEvent;
import lift.server.Worker.LiftEventStrategy;

/**
 * @author Micha³
 *
 */
public class GuiListener extends Listener
{
	private final Object simulationMode; 
	/**
	 * @param id
	 * @param forListening
	 * @param forSending
	 */
	public GuiListener(ModuleID id, Channel<LiftEvent> forListening, Channel<Packet> forSending)
	{
		super(id, forListening, forSending);
		
		simulationMode = new Object();
	}

	@Override
	public void run()
	{
		Boolean isLiving = true;
		
		while(isLiving)
		{
			LiftEvent event = null;
			event = forListening.get();			
			forSending.add(new Packet(id, event));			
		}
	}
	
	abstract class GuiEventStrategy
	{
		public abstract void execute(final LiftEvent event);
	}
	
	class SimulationStopStrategy extends GuiEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{
			
		}
	}
	
	class SimulationStartStrategy extends GuiEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{
		}
	}
	
	class StepSimulationStrategy extends GuiEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{
		}
	}
}
