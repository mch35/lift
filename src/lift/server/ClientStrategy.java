package lift.server;

import java.util.HashMap;

import lift.common.events.LiftEvent;
import lift.server.Worker.LiftEventStrategy;

/**
 * Strategia obslugi klienta
 * 
 * @author Micha³ Chilczuk
 *
 */
class ClientStrategy
{
	/** strategie dla danego klienta */
	private final HashMap<Class<? extends LiftEvent>, LiftEventStrategy> strategies;
	
	public ClientStrategy()
	{
		this.strategies = new HashMap<>();
	}
	
	/** 
	 * Wykonuje dana strategie 
	 * 
	 * @param event wiadomosc dla ktorej ma byc wykonana strategia
	 * 
	 */
	public void process(final LiftEvent event)
	{
		LiftEventStrategy strategy = this.strategies.get(event.getClass());
		if(strategy != null)
		{
			strategy.execute(event);
		}
	}
	
	/**
	 * dodaje strategie dla danej wiadomosci
	 * 
	 * @param event wiadomosc dla ktorej bedzie wykonywana strategia
	 * @param strategy strtegia do wykonania dla danej wiadomosci
	 */
	public void addStrategy(final Class<? extends LiftEvent> event, final LiftEventStrategy strategy)
	{
		System.out.println("dodaje strategie dla: " + event);
		this.strategies.put(event, strategy);
	}
	
	/**
	 * Usuwa strategie dla danej wiadomosci
	 * 
	 * @param event wiadomosc dla ktorej bedzie usunieta strategia
	 * 
	 */
	public void removeStrategy(final Class<? extends LiftEvent> event)
	{
		this.strategies.remove(event);
	}
}