package lift;
import javax.swing.SwingUtilities;

import lift.driver.LiftDriver;
import lift.residents.ResidentsSimulation;
import lift.server.Server;
import lift.server.exception.ConnectionExitsException;
import lift.server.exception.ServerSleepsExeption;
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
		server.start();
		
	   // Run GUI construction on the Event-Dispatching Thread for thread safety
	   SwingUtilities.invokeLater(new Runnable()
	   {
		   @Override
		   public void run()
		   {
			   try
			   {
				   new LiftSimulation(server);
			   }
			   catch (Exception e)
			   {
				   e.printStackTrace();
			   }
		   }
	   });	   

	   try {
		(new Thread(new ResidentsSimulation(5, server))).start();
	} catch (ConnectionExitsException | ServerSleepsExeption e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   try {
		   (new Thread(LiftDriver.getInstance(5, server))).start();
	} catch (ConnectionExitsException | ServerSleepsExeption e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

}
