/**
 * 
 */
package lift.server;

import java.util.concurrent.ConcurrentHashMap;

import lift.common.events.*;

/**
 * Przetwarza wiadomosci otrzymane od listenerow i wysyla je do odpowiednich klientow
 * 
 * @author Micha� Chilczuk
 *
 * @TODO przy obsludze strategii trzeba zapewnic ze istnieje dany channel do wysylania
 */
class Worker implements Runnable
{
	/** Mapa kanalow do ktorych moze wysylac wiadomosci */
	private final ConcurrentHashMap<ModuleID, Channel<LiftEvent>> channels;
	/** Mapa strategii dla danych modulow */
	private final ConcurrentHashMap<ModuleID, ClientStrategy> clientsStrategies;
	/** Kanal w ktorym przesylane sa wiadomosci do przetworzenia */
	private final Channel<Packet> recieved;
	
	private final Object monitor;
	
	private final Timer timer;
	
	public Worker(final Channel<Packet> recieved, final Timer timer)
	{
		this.channels = new ConcurrentHashMap<>();
		this.clientsStrategies = new ConcurrentHashMap<>();
		this.recieved = recieved;
		this.monitor = new Object();
		this.timer = timer;
		addStrategies();
	}
	
	/**
	 * Dodaje nowy kanal do ktorego moze wysylac wiadomosci.
	 *  
	 * @param id identyfikator klienta
	 * @param channel kanal do ktorego bedzie wysylal
	 */
	public void addChannel(final ModuleID id, final Channel<LiftEvent> channel)
	{
		channels.putIfAbsent(id, channel);
	}
	
	/**
	 * Dodaje strategie obslugi klientow
	 * 
	 */
	private void addStrategies()
	{
		ClientStrategy liftStrategy = new ClientStrategy();
		liftStrategy.addStrategy(ChangeDirectionEvent.class, new ChangeDirectionStrategy());
		liftStrategy.addStrategy(LiftStopEvent.class, new LiftStopStrategy());
		clientsStrategies.put(ModuleID.WINDA, liftStrategy);
		
		ClientStrategy symStrategy = new ClientStrategy();
		symStrategy.addStrategy(GetOffEvent.class, new GetOffStrategy());
		symStrategy.addStrategy(GetOnEvent.class, new GetOnStrategy());
		symStrategy.addStrategy(DownButtonEvent.class, new DownButtonStrategy());
		symStrategy.addStrategy(UpButtonEvent.class, new UpButtonStrategy());
		symStrategy.addStrategy(GeneratePersonEvent.class, new GeneratePersonStrategy());
		symStrategy.addStrategy(InnerButtonEvent.class, new InnerButtonStrategy());
		symStrategy.addStrategy(LiftIsReadyEvent.class, new LiftIsReadyStrategy());
		clientsStrategies.put(ModuleID.MIESZKANCY, symStrategy);
		
		ClientStrategy guiStrategy = new ClientStrategy();
		guiStrategy.addStrategy(SimulationStopEvent.class, new SimulationStopStrategy());
		guiStrategy.addStrategy(SimulationStartEvent.class, new SimulationStartStrategy());
		guiStrategy.addStrategy(StepSimulationEvent.class, new StepSimulationStrategy());
		guiStrategy.addStrategy(NextStepEvent.class, new NextStepStrategy());
		guiStrategy.addStrategy(GuiGeneratePersonEvent.class, new GuiGeneratePersonStrategy());
		guiStrategy.addStrategy(SetTimeIntervalEvent.class, new SetTimeIntervalStrategy());
		guiStrategy.addStrategy(LiftOnTheFloorEvent.class, new LiftOnTheFloorStrategy());
		clientsStrategies.put(ModuleID.GUI, guiStrategy);
	}
	
	/**
	 * Usuwa kanal klienta o danym id
	 * 
	 * @param id identyfikator klienta usuwanego kanalu
	 */
	public void removeChannel(final ModuleID id)
	{
		channels.remove(id);
	}

	/**
	 * Pobiera wiadomosci z kolejki i uruchamia odpowiednie strategie
	 * 
	 */
	@Override
	public void run()
	{
		while(true)
		{
			/*synchronized (monitor)
			{
				try
				{
					monitor.wait();
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			
			Packet packet = recieved.get();
			
			ModuleID sender = packet.getSender();
			LiftEvent event = packet.getEvent();			
			
			clientsStrategies.get(sender).process(event);
		}
	}
	
	
	// TODO: Jezeli dwa moduly dostana zdarzenie to beda oba pracowaly na tym samym obiekcie (sekcja krytyczna)
	/**
	 * Strategia obslugi eventu
	 * 
	 * @author Micha� Chilczuk
	 *
	 */
	abstract class LiftEventStrategy
	{
		public abstract void execute(final LiftEvent event);
	}
	
	class GuiGeneratePersonStrategy extends LiftEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{
			channels.get(ModuleID.MIESZKANCY).add(event);
		}
	}
	
	class SetTimeIntervalStrategy extends LiftEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{
			channels.get(ModuleID.MIESZKANCY).add(event);
		}
	}
	
	class GetOffStrategy extends LiftEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{
			//channels.get(ModuleID.WINDA).add(event);	-Krzysiek tego nie powinien dostawac
			channels.get(ModuleID.GUI).add(event);
		}
	}
	
	class GetOnStrategy extends LiftEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{
			//channels.get(ModuleID.WINDA).add(event);	-Krzysiek j.w.
			channels.get(ModuleID.GUI).add(event);
		}
	}
	
	class DownButtonStrategy extends LiftEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{
			channels.get(ModuleID.WINDA).add(event);
			channels.get(ModuleID.GUI).add(event);
		}
	}
	
	class UpButtonStrategy extends LiftEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{
			channels.get(ModuleID.WINDA).add(event);
			channels.get(ModuleID.GUI).add(event);
		}
	}
	
	class GeneratePersonStrategy extends LiftEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{
			channels.get(ModuleID.GUI).add(event);
		}
	}
	
	class ChangeDirectionStrategy extends LiftEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{
			channels.get(ModuleID.MIESZKANCY).add(event);
			channels.get(ModuleID.GUI).add(event);
		}
	}
	
	class LiftIsReadyStrategy extends LiftEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{
			channels.get(ModuleID.WINDA).add(event);
			channels.get(ModuleID.GUI).add(event);
		}
	}
	
	class LiftStopStrategy extends LiftEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{
			channels.get(ModuleID.MIESZKANCY).add(event);
			channels.get(ModuleID.GUI).add(event);
		}
	}
	
	class InnerButtonStrategy extends LiftEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{
			channels.get(ModuleID.WINDA).add(event);
			channels.get(ModuleID.GUI).add(event);
		}
	}
	
	class LiftOnTheFloorStrategy extends LiftEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{
			channels.get(ModuleID.WINDA).add(event);
			channels.get(ModuleID.MIESZKANCY).add(event);
		}
	}
	
	class SimulationStopStrategy extends LiftEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{
			timer.stop();
		}
	}
	
	class SimulationStartStrategy extends LiftEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{			
			timer.start();
		}
	}
	
	class StepSimulationStrategy extends LiftEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{
			timer.setStepWorking(!timer.getIsStepWorking());			
		}
	}
	
	class NextStepStrategy extends LiftEventStrategy
	{
		@Override
		public void execute(final LiftEvent event)
		{
			timer.doStep();
		}
	}
}
