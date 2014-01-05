package lift.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lift.common.events.LiftEvent;
import lift.server.exception.ConnectionExitsException;

/**
 * Jeden z modulow symulatora windy.
 * Posredniczy w komunikacji miedzy poszczegolnymi modulami symulatora.
 * 
 * @author Michal Chilczuk
 * 
 */
public class Server
{
	/** Kanal do otrzymywania wiadomosci */
	private final Channel<Packet> recieved;
	/** Mapa listenerow */
	private final ConcurrentMap<ModuleID, Listener> listeners;
	/** Przetwarzacz pakietow otrzymanych od listenerow */
	private final Worker worker;
	/** Czy serwer pracuje */
	private Boolean isRunning;
	
	/**
	 * 
	 */
	public Server()
	{		
		recieved = new Channel<>();
		listeners = new ConcurrentHashMap<ModuleID, Listener>();
		worker = new Worker(recieved);
		isRunning = false;
	}
	
	/**
	 * Startuje prace serwera.
	 * 
	 */
	public void start()
	{
		if(!isRunning)
		{
			(new Thread(worker)).start();
		}
		
		isRunning = true;
	}
		
	/** 
	 * Ustanawia polaczenie z serwerem.
	 * 
	 * @param id Id klienta 
	 * @param reciever Miejsce gdzie maja byc wysylane wiadomosci od serwera.
	 * 
	 * @return Polaczenie, za pomoca ktorego beda przesylane wiadomosci.
	 * 
	 * @throws ConnectionExitsException zwracany gdy istnieje juz polaczenie z klientem o takim id
	 */
	public Connection connect(final ModuleID id) throws ConnectionExitsException
	{
		System.out.println("SERWER: LACZENIE...");
		// kanaly do komunikacji
		Channel<LiftEvent> forSending = new Channel<>();
		Channel<LiftEvent> forListening = new Channel<>();
		
		// nowe polaczenie na podstawie kanalow wczesniej zrobionych
		Connection connection = new Connection(forListening, forSending);
		
		// listener sluchajacy na kanale w ktorym klient wrzuca wiadomosci
		Listener listener = new Listener(id, forListening, recieved);
		
		// dodaje do mapy jezeli nie ma jeszcze takiego klucza
		if(listeners.putIfAbsent(id, listener) != null)
		{
			throw new ConnectionExitsException();
		}
		
		worker.addChannel(id, forSending);
		
		(new Thread(listener)).start();

		System.out.println("SERWER: POLACZONO z " + id);
		return connection;
	}
}
