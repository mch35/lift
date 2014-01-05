package lift.driver;

import java.util.HashMap;
import java.util.Map;

import lift.common.*;
import lift.common.events.*;
import lift.server.Connection;
import lift.server.ModuleID;
import lift.server.Server;
import lift.server.exception.ConnectionExitsException;

public class LiftDriver implements Runnable {
	private final Connection connection;
	private final int numberOfFloors;
	public boolean readyToRide;
	private final LiftButton[] buttonPanel;
	private final LiftButton[][] directionButtonPanels;
	private Floor actualFloor;
	private Floor destinationFloor;
	private Direction direction;

	
	/** Map associating LiftEvents with appropriate actions */
	private final Map<Class<? extends LiftEvent>, LiftAction> eventActionMap;
	/** Implementation of a Singleton pattern. */
	private static LiftDriver instance = null;
	 /** @param communication 
	 * @param numberOfFloors 
	 * @return Lift class instance. 
	 * @throws ServerSleepsExeption 
	 * @throws ConnectionExitsException */
	  public static synchronized LiftDriver getInstance(int numberOfFloors, final Server server) throws ConnectionExitsException
	  {
		    if(instance == null) instance = new LiftDriver(server, numberOfFloors);
		    return instance;
	  }
	
	  
	/** Lift class constructor 
	 * @throws ConnectionExitsException
	 * */
	private LiftDriver(final Server server, int numberOfFloors) throws ConnectionExitsException
	{
		this.numberOfFloors=numberOfFloors;
		this.connection=server.connect(ModuleID.WINDA);
		this.readyToRide=true;
		buttonPanel=new LiftButton[numberOfFloors];
		directionButtonPanels=new LiftButton[numberOfFloors][2];
		actualFloor=destinationFloor=new Floor(0);
		direction=Direction.STOP;
		
		for(int i=0;i<numberOfFloors;i++){
			buttonPanel[i]=LiftButton.NOT_ACTIVE;
			directionButtonPanels[i][0]=directionButtonPanels[i][1]=LiftButton.NOT_ACTIVE;
		}
		
		eventActionMap = new HashMap<Class<? extends LiftEvent>, LiftAction>();
		fillEventActionMap();
	}
	
	
	@Override
	public void run() {
		while(true){
			try {
			LiftEvent event = connection.recieve();
			LiftAction liftAction = eventActionMap.get(event.getClass());

			liftAction.execute(event);
			} catch(Exception e) {
		        e.printStackTrace();
		        throw new RuntimeException(e);
		      } 
		}	
	}
	
		
	private void fillEventActionMap() {
		
		eventActionMap.put(UpButtonEvent.class, new LiftAction() {
			@Override
			public void execute(LiftEvent e) {
				UpButtonEvent event = (UpButtonEvent) e;
				pushButtonUp(new Floor(event.getFloor()));
				if (direction == Direction.STOP) {
					if (event.getFloor() > actualFloor.getNumber()) {
						connection.send(new ChangeDirectionEvent(Direction.UP,actualFloor.getNumber()));
						direction=Direction.UP;
					}
					else if (event.getFloor() < actualFloor.getNumber()) {
						connection.send(new ChangeDirectionEvent(Direction.DOWN,actualFloor.getNumber()));
						direction=Direction.DOWN;
					}
					else {
						connection.send(new ChangeDirectionEvent(Direction.UP,event.getFloor()));
						direction=Direction.UP;
						clearButtonUp(actualFloor);
					}
					readyToRide=true;
				}
			}
		});
		
		eventActionMap.put(DownButtonEvent.class, new LiftAction() {
			@Override
			public void execute(LiftEvent e) {
				DownButtonEvent event = (DownButtonEvent) e;
				pushButtonDown(new Floor(event.getFloor()));
				if (direction == Direction.STOP) {
					if (event.getFloor() > actualFloor.getNumber()) {
						connection.send(new ChangeDirectionEvent(Direction.UP,actualFloor.getNumber()));
						direction=Direction.UP;
					}
					else if (event.getFloor() < actualFloor.getNumber()) {
						connection.send(new ChangeDirectionEvent(Direction.DOWN,actualFloor.getNumber()));
						direction=Direction.DOWN;
					}
					else {
						connection.send(new ChangeDirectionEvent(Direction.DOWN,event.getFloor()));
						direction=Direction.DOWN;
						clearButtonDown(actualFloor);
					}
					readyToRide=true;
				}
			}
		});
		
		eventActionMap.put(InnerButtonEvent.class, new LiftAction() {	
			@Override
			public void execute(LiftEvent e) {
				InnerButtonEvent event = (InnerButtonEvent) e;
				pushButton(new Floor(event.getFloor()));
				if (direction == Direction.STOP) {
					if (actualFloor.getNumber() == event.getFloor()) {
						connection.send(new LiftStopEvent(event.getFloor()));
						clearButton(actualFloor);
					}
					else {
						if (event.getFloor() > actualFloor.getNumber()) {
							connection.send(new ChangeDirectionEvent(Direction.UP, actualFloor.getNumber()));
							direction=Direction.UP;
						}
						else {
							connection.send(new ChangeDirectionEvent(Direction.DOWN, actualFloor.getNumber()));
							direction=Direction.DOWN;
						}
						readyToRide=true;
					}
				}
			}
		});
		
		eventActionMap.put(LiftIsReadyEvent.class, new LiftAction() {
			@Override
			public void execute(LiftEvent e) {
				readyToRide=true;	//kierunek
			}
		});
		
		eventActionMap.put(LiftOnTheFloorEvent.class, new LiftAction(){
			@Override
			public void execute(LiftEvent e) {
				LiftOnTheFloorEvent event = (LiftOnTheFloorEvent) e;
				
				actualFloor = new Floor(event.getFloor());
				destinationFloor=getDestinationFloor(directionButtonPanels, buttonPanel, direction, actualFloor);
				
				
				if (buttonPanel[event.getFloor()] == LiftButton.ACTIVE) {
					if (direction == Direction.DOWN) {
						if (directionButtonPanels[event.getFloor()][0] == LiftButton.ACTIVE) {
							connection.send(new LiftStopEvent(event.getFloor()));
							clearButtonDown(actualFloor);
						}
						else if (directionButtonPanels[event.getFloor()][1] == LiftButton.ACTIVE) {
							if (actualFloor.getNumber() == destinationFloor.getNumber()) {
								connection.send(new ChangeDirectionEvent(Direction.UP, event.getFloor()));
								clearButtonUp(actualFloor);
								direction=Direction.UP;
							}
							else {
								connection.send(new LiftStopEvent(event.getFloor()));
							}
						}
						else {
							if (actualFloor.getNumber() == destinationFloor.getNumber()) {
								if (getHighestDirBut(directionButtonPanels).getNumber()==0) {
									if (getHighestButPan(buttonPanel).getNumber()==0) {
										connection.send(new ChangeDirectionEvent(Direction.STOP, event.getFloor()));
										direction=Direction.STOP;
									}
									else {
										connection.send(new ChangeDirectionEvent(Direction.UP, event.getFloor()));
										direction=Direction.UP;
									}
								}
								else {
									connection.send(new ChangeDirectionEvent(Direction.UP, event.getFloor()));
									direction=Direction.UP;
								}
							}
							else {
								connection.send(new LiftStopEvent(event.getFloor()));
							}
						}
					}
					else if (direction == Direction.UP) {
						if (directionButtonPanels[event.getFloor()][1] == LiftButton.ACTIVE) {
							connection.send(new LiftStopEvent(event.getFloor()));
							clearButtonUp(actualFloor);
						}
						else if (directionButtonPanels[event.getFloor()][0] == LiftButton.ACTIVE) {
							if (actualFloor.getNumber() == destinationFloor.getNumber()) {
								connection.send(new ChangeDirectionEvent(Direction.DOWN, event.getFloor()));
								clearButtonDown(actualFloor);
								direction=Direction.DOWN;
							}
							else {
								connection.send(new LiftStopEvent(event.getFloor()));
							}
						}
						else {
							if (actualFloor.getNumber() == destinationFloor.getNumber()) {
								if (getLowestDirBut(directionButtonPanels).getNumber()==numberOfFloors-1) {
									if (getLowestButPan(buttonPanel).getNumber()==numberOfFloors-1) {
										connection.send(new ChangeDirectionEvent(Direction.STOP, event.getFloor()));
										direction=Direction.STOP;
									}
									else {
										connection.send(new ChangeDirectionEvent(Direction.DOWN, event.getFloor()));
										direction=Direction.DOWN;
									}
								}
								else {
									connection.send(new ChangeDirectionEvent(Direction.DOWN, event.getFloor()));
									direction=Direction.DOWN;
								}
							}
							else {
								connection.send(new LiftStopEvent(event.getFloor()));
							}
						}
					}
					clearButton(actualFloor);
					readyToRide = false;
				}
				else {
					if (direction == Direction.DOWN) {
						if (directionButtonPanels[event.getFloor()][0] == LiftButton.ACTIVE) {
							connection.send(new LiftStopEvent(event.getFloor()));
							clearButtonDown(actualFloor);
							readyToRide = false;
						}
						else if (directionButtonPanels[event.getFloor()][1] == LiftButton.ACTIVE) {
							if (actualFloor.getNumber() == destinationFloor.getNumber()) {
								connection.send(new ChangeDirectionEvent(Direction.UP, event.getFloor()));
								clearButtonUp(actualFloor);
								direction=Direction.UP;
								readyToRide = false;
							}
						}
					}
					else if (direction == Direction.UP) {
						if (directionButtonPanels[event.getFloor()][1] == LiftButton.ACTIVE) {
							connection.send(new LiftStopEvent(event.getFloor()));
							clearButtonUp(actualFloor);
							readyToRide = false;
						}
						else if (directionButtonPanels[event.getFloor()][0] == LiftButton.ACTIVE) {
							if (actualFloor.getNumber() == destinationFloor.getNumber()) {
								connection.send(new ChangeDirectionEvent(Direction.DOWN, event.getFloor()));
								clearButtonDown(actualFloor);
								direction=Direction.DOWN;
								readyToRide = false;
							}
						}
					}
				}
			}
		});
		
	}


	private void pushButtonUp(Floor floor)
	{
		directionButtonPanels[floor.getNumber()][1]=LiftButton.ACTIVE;
	}
	
	private void pushButtonDown(Floor floor)
	{
		directionButtonPanels[floor.getNumber()][0]=LiftButton.ACTIVE;
	}
	
	private void clearButtonUp(Floor floor)
	{
			directionButtonPanels[floor.getNumber()][1]=LiftButton.NOT_ACTIVE;
	}
	
	private void clearButtonDown(Floor floor)
	{
			directionButtonPanels[floor.getNumber()][0]=LiftButton.NOT_ACTIVE;
	}
	
	private void pushButton(Floor floor)
	{
			buttonPanel[floor.getNumber()]=LiftButton.ACTIVE;
	}
	
	private void clearButton(Floor floor)
	{
			buttonPanel[floor.getNumber()]=LiftButton.NOT_ACTIVE;
	}

	private Floor getHighestButPan(LiftButton[] bP){
		
		int x=numberOfFloors-1;
		while(x>=0){
			if (bP[x]==LiftButton.ACTIVE) return new Floor(x);
			x--;
		}
		return new Floor(0);
	}
	
	private Floor getLowestButPan(LiftButton[] bP){
		
		int x=0;
		while(x<numberOfFloors){
			if(bP[x]==LiftButton.ACTIVE)return new Floor(x);
			x++;
		}
		return new Floor(numberOfFloors-1);
	}
	
	private Floor getHighestDirBut(LiftButton[][] dbp) {
		int x = numberOfFloors-1;
		while(x>=0){
			if(dbp[x][0]==LiftButton.ACTIVE || dbp[x][1]==LiftButton.ACTIVE) return new Floor(x);
			x--;
		}
		return new Floor(0);
	}
	
	private Floor getLowestDirBut(LiftButton[][] dbp) {
		int x = 0;
		while(x<numberOfFloors){
			if(dbp[x][0]==LiftButton.ACTIVE || dbp[x][1]==LiftButton.ACTIVE) return new Floor(x);
			x++;
		}
		return new Floor(numberOfFloors-1);
	}
	
	private Floor getDestinationFloor(LiftButton[][] dbp, LiftButton[] bp, Direction direction, Floor actualFloor) {
		int df, x;
			if (direction == Direction.DOWN) {
				x=0;
				df=actualFloor.getNumber();
				while(x < actualFloor.getNumber()) {
					if ((dbp[x][0] == LiftButton.ACTIVE) || (dbp[x][1] == LiftButton.ACTIVE)) {
						df=x;
						break;
					}
					else x++;
				}
				if (getLowestButPan(bp).getNumber() < df) {
					df = getLowestButPan(bp).getNumber();
				}
			}
			else if (direction == Direction.UP) {
				x=numberOfFloors-1;
				df=actualFloor.getNumber();
				while(x > actualFloor.getNumber()) {
					if ((dbp[x][0] == LiftButton.ACTIVE) || (dbp[x][1] == LiftButton.ACTIVE)) {
						df=x;
						break;
					}
					else x--;
				}
				if (getHighestButPan(bp).getNumber() > df) {
					df = getHighestButPan(bp).getNumber();
				}
			}
			else {
				df = actualFloor.getNumber();
				//skomplikowane
			}
			return new Floor(df);
	}
	
	
}
