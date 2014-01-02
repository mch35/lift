package lift.common;


/*
 * Event mowiacy o tym ze na danym pietrze zostala wygenerowana nowa osoba
 * jadaca w danym kierunku
 */
public class GeneratePersonEvent extends LiftEvent{

	private final int homeFloor;
	private final Direction direction;
	
	public GeneratePersonEvent(int homeFloor, Direction direct)
	{
		direction = direct;
		this.homeFloor = homeFloor;
	}

	public Direction getDirection() {
		return direction;
	}




}
