package lift;
import javax.swing.SwingUtilities;

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
	   SwingUtilities.invokeLater(new Runnable() {
	      @Override
	      public void run() {
	         try {
				new LiftSimulation(server);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Let the constructor do the job
	      }
	   });
	}

}
