package lift;

import lift.driver.LiftDriver;
import lift.residents.ResidentsSimulation;
import lift.server.Server;
import lift.server.exception.ConnectionExitsException;
import lift.view.LiftSimulation;
import lift.view.Resident;


/**
 * @author Micha³
 *
 */
public class Main
{	
	/**
	 * 
	 */
	public Main()
	{
		
	}
	
	
	/** The entry main() method */
	public static void main(String[] args)
	{
		final Server server = new Server();
		ResidentsSimulation residents = null;
		int floors = 5;
		
		try
		{
			residents = new ResidentsSimulation(floors, server);
		} catch (ConnectionExitsException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Start gui windy
		try
		{
			(new Thread(new LiftSimulation(floors, server))).start();
		}
		catch (ConnectionExitsException e)
		{		
			e.printStackTrace();
		}
		
		// Start drivera windy
		try
		{
			(new Thread(LiftDriver.getInstance(floors, server))).start();
		}
		catch (ConnectionExitsException e)
		{		
			e.printStackTrace();
		}
		
		// Start modulu mieszkancow
		(new Thread(residents)).start();

		server.start();
		residents.start();		
	}
}
