/**
 * 
 */
package lift.server;

import lift.common.LiftEvent;

/**
 * Pakiet przechowujacy wiadomosc i jego nadawce
 * 
 * @author Micha³ Chilczuk
 *
 */
class Packet
{
	/** nadawca wiadomosci */
	private final ModuleID sender;
	/** przesylana wiadomosc */
	private final LiftEvent event;
	
	/**
	 * @param sender kto przesyla
	 * @param event przesylana wiadomosc
	 */
	public Packet(final ModuleID sender, final LiftEvent event)
	{
		this.sender = sender;
		this.event = event;
	}
	
	/**
	 * Zwraca identyfikator nadawcy wiadomosci
	 * 
	 * @return identyfikator nadawcy
	 */
	public final ModuleID getSender()
	{
		return sender;
	}
	
	/**
	 * Zwraca wiadomosc pakietu
	 * 
	 * @return wiadomosc
	 */
	public final LiftEvent getEvent()
	{
		return event;
	}
}
