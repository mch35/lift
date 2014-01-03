package lift;
import javax.swing.SwingUtilities;

import lift.residents.ResidentsSimulation;
import lift.server.Server;
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
				   new ResidentsSimulation(5, server);
			   }
			   catch (Exception e)
			   {
				   e.printStackTrace();
			   }
		   }
	   });
	}

}
