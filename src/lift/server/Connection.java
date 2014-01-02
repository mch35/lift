/**
 * 
 */
package lift.server;

import lift.common.events.LiftEvent;

/**
 * Reprezentuje polaczenie miedzy danym modulem, a serwerem.
 * 
 * @author Micha³ Chilczuk  
 *
 */
public class Connection
{
	/** Kolejka do ktorej beda wysylane wiadomosci do serwera */
	private final Channel<LiftEvent> clientToServer;
	/** Kolejka do ktorej beda wysylane wiadomosci od serwera */
	private final Channel<LiftEvent> serverToClient;
	/** Status polaczenia */
	private final Status status;
	
	/** 
	 * @param clientToServer kanal do ktorego beda wysylane wiadomosci do serwera
	 * @param serverToClient kanal do ktorego beda wysylane wiadomosci od serwera
	 */
	Connection(final Channel<LiftEvent> forSending, final Channel<LiftEvent> forRecieving)
	{
		this.clientToServer = forSending;
		this.serverToClient = forRecieving;
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
		clientToServer.add(event);
		
		return Status.OK;
	}
	
	/**
	 * Zwraca wiadomosc od serwera jezeli jakas jest.
	 * 
	 * @return Wiadomosc od serwera.
	 */
	public final LiftEvent recieve()
	{
		return serverToClient.get();
	}

	/**
	 * Rozlacza sie z serwerem.
	 * 
	 */
	public void disconnect()
	{
		
	}
}
