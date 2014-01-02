/**
 * 
 */
package lift.server;

import lift.common.LiftEvent;

/**
 * Nasluchuje na konkretnym polaczeniu i przesyla wiadomosci do watku obslugujacego.
 * 
 * @author Micha³ Chilczuk
 *
 */
class Listener implements Runnable
{
	/** Kanal na ktorym ma sluchac listener */
	protected final Channel<LiftEvent> forListening;	
	/** Kanal do ktorego ma wysylac wiadomosci */
	protected final Channel<Packet> forSending;
	/** Identyfikator modulu ktorego slucha */
	protected final ModuleID id;		
	
	public Listener(final ModuleID id, final Channel<LiftEvent> forListening, final Channel<Packet> forSending)
	{
		this.id = id;
		this.forListening = forListening;
		this.forSending = forSending;
	}

	/**
	 * Watek ktory pobiera wiadomosci i podaje je dalej
	 * 
	 */
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
}
