/**
 * 
 */
package lift.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Kanal przez ktory mozna przesylac wiadomosci.
 * 
 * @param MessageType typ przesylanych wiadomosci
 * @author Micha³
 *
 */
class Channel<MessageType>
{
	/** Kolejka ktora przechowuje wiadomosci przesylane przez kanal */
	private final BlockingQueue<MessageType> channel;
	
	public Channel()
	{
		this.channel = new LinkedBlockingQueue<MessageType>();
	}
	
	/**
	 *  
	 * @param message przesylana wiadomosc
	 */
	public void add(MessageType message)
	{
		channel.add(message);
	}
	
	/**
	 * Zwraca wiadomosc otrzymana przez kanal lub czeka gdy nie ma zadnej.
	 * 
	 * @return odebrana wiadomosc
	 */
	public MessageType get()
	{
		try
		{
			return channel.take();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
