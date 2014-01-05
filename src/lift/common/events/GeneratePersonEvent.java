package lift.common.events;


/**
 * Zdarzenie przesylane, gdy pojawil sie nowy czlowiek.
 *
 *
 */
public class GeneratePersonEvent extends LiftEvent
{
	private final int homeFloor;
	private final int id;
	
	public GeneratePersonEvent(final int startFloor,final int id)
	{
		this.id = id;

		homeFloor = startFloor;
	}

	public int getHomeFloor()
	{
		return homeFloor;
	}

	public int getId() {
		return id;
	}

	
}
