package lift.common.events;


/**
 * Zdarzenie przesylane, gdy pojawil sie nowy czlowiek.
 *
 *
 */
public class GeneratePersonEvent extends LiftEvent
{
	private final int homeFloor;
        private final int destFloor;
	private final int id;
	
	public GeneratePersonEvent(final int startFloor, final int id, final int desFloor)
	{
		this.id = id;

		homeFloor = startFloor;
                destFloor = desFloor;
	}

	public int getHomeFloor()
	{
		return homeFloor;
	}
        
        public int getDestFloor()
	{
		return destFloor;
	}

	public int getId() {
		return id;
	}

	
}
