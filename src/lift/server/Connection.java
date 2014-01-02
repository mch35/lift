/**
 * 
 */
package lift.server;

import lift.common.LiftEvent;

/**
 * Reprezentuje polaczenie miedzy danym modulem, a serwerem.
 * 
 * @author Micha� Chilczuk  
 *
 */
public class Connection
{
	/** Kolejka do ktorej beda wysylane wiadomosci do serwera */
	private final Channel<LiftEvent> forSending;
	/** Kolejka do ktorej beda wysylane wiadomosci od serwera */
	private final Channel<LiftEvent> forRecieving;
	/** Status polaczenia */
	private final Status status;
	
	/** 
	 * @param forSending kanal do ktorego beda wysylane wiadomosci do serwera
	 * @param forRecieving kanal do ktorego beda wysylane wiadomosci od serwera
	 */
	Connection(final Channel<LiftEvent> forSending, final Channel<LiftEvent> forRecieving)
	{
		this.forSending = forSending;
		this.forRecieving = forRecieving;
		this.status = Status.OK;
	}
	
	/**
	 * Wysyla wiadomosc do serwera.
	 * 
	 * @param event wysylana wiadomosc 
	 * 
	 * @return Czy udalo sie wyslac wiadomosc.
	 */
	public Status send(final LiftEvent event)
	{
		forSending.add(event);
		
		return Status.OK;
	}
	
	/**
	 * Zwraca wiadomosc od serwera jezeli jakas jest.
	 * 
	 * @return Wiadomosc od serwera.
	 */
	public final LiftEvent recieve()
	{
		return forRecieving.get();
	}

	/**
	 * Rozlacza sie z serwerem.
	 * 
	 */
	public void disconnect()
	{
		
	}
}
