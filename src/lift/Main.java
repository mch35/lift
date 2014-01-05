package lift;

import lift.driver.LiftDriver;
import lift.residents.ResidentsSimulation;
import lift.server.Server;
import lift.server.exception.ConnectionExitsException;
import lift.view.LiftSimulation;


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
		
		// Start gui windy
		try
		{
			(new Thread(new LiftSimulation(server))).start();
		}
		catch (ConnectionExitsException e)
		{		
			e.printStackTrace();
		}
		
		// Start drivera windy
		try
		{
			(new Thread(LiftDriver.getInstance(5, server))).start();
		}
		catch (ConnectionExitsException e)
		{		
			e.printStackTrace();
		}
		
		// Start modulu mieszkancow
		try
		{
			(new Thread(new ResidentsSimulation(5, server))).start();
		}
		catch (ConnectionExitsException e)
		{		
			e.printStackTrace();
		}

		server.start();
	}
}
