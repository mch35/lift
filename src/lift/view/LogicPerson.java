package lift.view;

import lift.common.Direction;

public class LogicPerson {
	
	private final int homeFloor;
	private final Direction direction;
	
	//id odrozniajace czlowieka w obrebie windy, jesli czlowiek nie jest w windzie, dostaje id = -1;
	private int idInLift;
	
	public LogicPerson(final Direction direction,final int homeFloor)
	{
		setIdInLift(-1);
		
		
		this.homeFloor = homeFloor;
		this.direction = direction;
	}
	
	/**
	 * Do tej funkcji moim zdaniem bedzie trzeba dodac jakis parametr mowiacy o tym ktory jest w 
	 * kolejce np, zeby wiedziec jak narysowac
	 */
	public void drawPerson(final int x)
	{
		
	}

	public Direction getDirection() {
		return direction;
	}

	public int getIdInLift() {
		return idInLift;
	}

	public void setIdInLift(int idInLift) {
		this.idInLift = idInLift;
	}

}
