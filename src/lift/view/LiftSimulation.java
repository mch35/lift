package lift.view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lift.common.Direction;
import lift.common.events.ChangeDirectionEvent;
import lift.common.events.DownButtonEvent;
import lift.common.events.GeneratePersonEvent;
import lift.common.events.GetOffEvent;
import lift.common.events.GetOnEvent;
import lift.common.events.GuiGeneratePersonEvent;
import lift.common.events.InnerButtonEvent;
import lift.common.events.LiftEvent;
import lift.common.events.LiftIsReadyEvent;
import lift.common.events.LiftOnTheFloorEvent;
import lift.common.events.LiftStopEvent;
import lift.common.events.SetTimeIntervalEvent;
import lift.common.events.SimulationStartEvent;
import lift.common.events.SimulationStopEvent;
import lift.common.events.UpButtonEvent;
import lift.server.Connection;
import lift.server.ModuleID;
import lift.server.Server;
import lift.server.exception.ConnectionExitsException;

public class LiftSimulation extends JFrame implements Runnable
{
   private static final long serialVersionUID = 1L;
	
   // Name-constants for the various dimensions
   public static final int CANVAS_WIDTH = 950;
   public final int CANVAS_HEIGHT;
   public static final Color CANVAS_BG_COLOR = new Color(183, 221, 230);
   public static final int IMAGE_WIDTH = 85;
   public static final int IMAGE_HEIGHT = 126;
 
   private DrawCanvas canvas; // the custom drawing canvas (extends JPanel)
   private Building building;
   private ElevatorShaft shaft;
   private ElevatorBox box;
   
   private final LinkedList<Resident> listOfPeople;
   
   /** Liczba pieter */
   private final int numberOfFloors;
   
   //Lista pieter do ktorych bede dodawac ludzi w kolejce
   private LogicFloor[] floorList;
   
   private int currentFloor;
   private Direction currentDirection;
   private boolean readyToRide;
   private boolean allIn;
   
   private LogicLift lift;
   private LiftInsideSimulation liftInsideSimulation;
   private ButtonPanel buttonPanel;
   
   /** Polaczenie z serwerem */
   private final Connection connection;

 
   /** 
    * Constructor to set up the GUI
    * 
    * @throws ConnectionExitsException
    *  
 	*/
   public LiftSimulation(final int iloscPieter, final Server server) throws ConnectionExitsException
   {  
	  CANVAS_HEIGHT = iloscPieter * IMAGE_HEIGHT;
      building = new Building(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, Color.BLACK, iloscPieter);
      shaft = new ElevatorShaft(CANVAS_WIDTH-IMAGE_WIDTH-50, 0, IMAGE_WIDTH, CANVAS_HEIGHT, Color.YELLOW);
      box = new ElevatorBox(CANVAS_WIDTH-IMAGE_WIDTH-50, CANVAS_HEIGHT-IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT, Color.DARK_GRAY);
      
      this.numberOfFloors = iloscPieter;
      floorList = new LogicFloor[numberOfFloors];
      
      currentDirection=Direction.STOP;
      currentFloor=0;
      readyToRide=false;
      allIn=false;
      
      
      listOfPeople = new LinkedList<Resident>();
      
      for(int i = 0; i < numberOfFloors; ++i)
      {
    	  floorList[i] = new LogicFloor(i);
      }
      
      lift = new LogicLift();
      liftInsideSimulation = new LiftInsideSimulation(IMAGE_WIDTH, IMAGE_HEIGHT, lift);
      buttonPanel = new ButtonPanel(lift,numberOfFloors);
      
      try
      {
		this.connection = server.connect(ModuleID.GUI);
      }
      catch (ConnectionExitsException e)
      {
    	  e.printStackTrace();
    	  throw e;
      }
      
      
      // Set up the custom drawing canvas (JPanel)
      canvas = new DrawCanvas();
      canvas.setPreferredSize(new Dimension(CANVAS_WIDTH+10, CANVAS_HEIGHT+10));
      
 
      // Set up a panels for the buttons
      JPanel btnPanel = new JPanel(new FlowLayout());
      JPanel addResidentPanel = new JPanel(new FlowLayout());
      JPanel setTimeIntervalPanel = new JPanel(new FlowLayout());

      // Set up buttons
      JButton startSimulation = new JButton("Start simulation");
      JButton stopSimulation = new JButton("Stop simulation");
      JButton addNewResident = new JButton("Add new Resident ");
      JButton setTimeInterval = new JButton("Set time intervals");
      JButton stepOrAutomatic = new JButton("Step/Automatic simulation");
      JButton nextStep = new JButton("Next step");
      
      JButton btnMoveManLeft = new JButton("Move Man Left ");
      JButton btnMoveManRight= new JButton("Move Man Right ");
      JButton btnMoveBoxUp = new JButton("Move Box Up");
      JButton btnMoveBoxDown= new JButton("Move Box Down");
      
      // Set up TextFields and Labels
      final JLabel srcFloorJLabel = new JLabel("Source Floor:");
      final JTextField srcJTextField = new JTextField(2);
      final JLabel dstFloorJLabel = new JLabel("Destination Floor:");
      final JTextField dstJTextField = new JTextField(2);
      
     
      final JLabel minTimeLabel = new JLabel("Min Time Interval [s]:");
      final JTextField minTimeTextField = new JTextField(4);
      final JLabel maxTimeLabel = new JLabel("Max Time Interval [s]:");
      final JTextField maxTimeTextField = new JTextField(4);
      
      // Add buttons to Panels
      addResidentPanel.add(startSimulation);
      addResidentPanel.add(stopSimulation);
      
      addResidentPanel.add(addNewResident);
      addResidentPanel.add(srcFloorJLabel);
      addResidentPanel.add(srcJTextField);
      addResidentPanel.add(dstFloorJLabel);
      addResidentPanel.add(dstJTextField);
      
      setTimeIntervalPanel.add(stepOrAutomatic);
      setTimeIntervalPanel.add(nextStep);
      setTimeIntervalPanel.add(setTimeInterval);
      setTimeIntervalPanel.add(minTimeLabel);
      setTimeIntervalPanel.add(minTimeTextField);
      setTimeIntervalPanel.add(maxTimeLabel);
      setTimeIntervalPanel.add(maxTimeTextField);
      
      btnPanel.add(btnMoveManLeft);
      btnPanel.add(btnMoveManRight);
      btnPanel.add(btnMoveBoxUp);
      btnPanel.add(btnMoveBoxDown);
      
      startSimulation.addActionListener(new ActionListener()
      {    		
    		@Override
    		public void actionPerformed(ActionEvent arg0)
    		{
    			connection.send(new SimulationStartEvent());    			
    		}
      });
      
      stopSimulation.addActionListener(new ActionListener()
      {    		
    		@Override
    		public void actionPerformed(ActionEvent arg0)
    		{
    			connection.send(new SimulationStopEvent());    			
    		}
      });
      
      addNewResident.addActionListener(new ActionListener() {
  		
    	  @Override
  		public void actionPerformed(ActionEvent arg0)
  		{
    		int homeFloor = Integer.parseInt(srcJTextField.getText());
    		int destinationFloor = Integer.parseInt(dstJTextField.getText());
    		
  			connection.send(new GuiGeneratePersonEvent(homeFloor, destinationFloor));    			
  		}
  	});
      
      setTimeInterval.addActionListener(new ActionListener() {
    		
    	  @Override
  		public void actionPerformed(ActionEvent arg0)
  		{
    		int minTime = Integer.parseInt(minTimeTextField.getText());
    		int maxTime = Integer.parseInt(maxTimeTextField.getText());
    		
  			connection.send(new SetTimeIntervalEvent(minTime, maxTime));    			
  		}
  	});
      
      stepOrAutomatic.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
	});
      
      nextStep.addActionListener(new ActionListener() {
  		
  		@Override
  		public void actionPerformed(ActionEvent e) {
  			// TODO Auto-generated method stub
  			
  		}
  	});
      
      btnMoveManLeft.addActionListener(new ActionListener()
      {		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
		
			openTheDoor();
            requestFocus(); // change the focus to JFrame to receive KeyEvent
			
		}
	});
      btnMoveManRight.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
			closeTheDoor();
            requestFocus(); // change the focus to JFrame to receive KeyEvent
			
		}
	});
      
      btnMoveBoxUp.addActionListener(new ActionListener() {
  		
  		@Override
  		public void actionPerformed(ActionEvent arg0) {
  			// TODO Auto-generated method stub
  			moveBoxUp();
              requestFocus(); // change the focus to JFrame to receive KeyEvent
  			
  		}
  	});
        btnMoveBoxDown.addActionListener(new ActionListener() {
  		
  		@Override
  		public void actionPerformed(ActionEvent arg0) {
  			// TODO Auto-generated method stub
  			moveBoxDown();
              requestFocus(); // change the focus to JFrame to receive KeyEvent
  			
  		}
  	});
 
      // Add both panels to this JFrame
      Container cp = getContentPane();
      cp.setLayout(new BorderLayout());
      cp.add(canvas, BorderLayout.CENTER);
      cp.add(setTimeIntervalPanel, BorderLayout.SOUTH);
      cp.add(addResidentPanel, BorderLayout.NORTH);

 
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setTitle("Real Time Lift Simulator");
      pack();            // pack all the components in the JFrame
      setVisible(true);  // show it
      requestFocus();    // "this" JFrame requests focus to receive KeyEvent
      
      animateLiftMovement();
   }
   
   /**
    * Startuje modul GUI
    * 
    */
   public void start()
   {
	   
   }
   public void animateLiftMovement(){
	   Thread liftAnimationThread = new Thread(){
		   @Override
			   public void run(){
				   while(true){
					   try {
						Thread.sleep(25);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					   if(readyToRide==true){
						   if(currentDirection==Direction.UP){
							   moveBoxUp();
						   }
						   else if(currentDirection==Direction.DOWN){
							   moveBoxDown();
						   }
					   }
				   }
			   }
	   };
	   liftAnimationThread.start();
   }
   
   public void openTheDoor() 
   {
//	   Thread animationThread = new Thread () {
//         @Override
//	         public void run() {
//			   while(box.width > 0)
//			   {
//				   box.width--;
//				   canvas.repaint();
//				   try {
//					Thread.sleep(50);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			   
//		       }
//         	}
//	   };
//	   animationThread.start();
//	   while(box.width > 0)
//		   {
//			   box.width--;
//			   canvas.repaint();
//		   }
	  
   }
   
   public void closeTheDoor() 
   {
//	   Thread animationThread = new Thread () {
//         @Override
//	         public void run() {
//			   while(box.width < IMAGE_WIDTH)
//			   {
//				   box.width++;
//				   canvas.repaint();
//				   try {
//					Thread.sleep(50);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			   
//		       }
//			   readyToRide=true;
//         	}
//	   };
//	   animationThread.start(); 
//	   while(box.width < IMAGE_WIDTH)
//		   {
//			   box.width++;
//			   canvas.repaint();
//		   }
	   readyToRide=true;
   }
   
   
   
//   public void manWalkIntoLift()
//   {
//	   Thread animationThread = new Thread () {
//	         @Override
//		         public void run() {
//				   while(man.x < CANVAS_WIDTH-IMAGE_WIDTH)
//				   {
//					   man.x += 8;
//					   canvas.repaint();
//					   try {
//						Thread.sleep(200);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				   
//			       }
//	         	}
//		   };
//		   animationThread.start(); 
//   }


//   private void moveManLeft() {
//      // Save the current dimensions for repaint to erase the sprite
//      int savedX = man.x;
//      // update sprite
//      man.x -= 10;
//      // Repaint only the affected areas, not the entire JFrame, for efficiency
//      canvas.repaint(savedX, man.y, man.width, man.height); // Clear old area to background
//      canvas.repaint(man.x, man.y, man.width, man.height); // Paint new location
//   }
   

   private void moveManRight(final Resident res) {
      
	   Thread animationThread = new Thread () {
       @Override
	         public void run() {
			   while(res.tempX < res.x)
			   {
				   res.tempX += 8;
				   canvas.repaint();
				   try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   
		       }
       	}
	   };
	   animationThread.start();
   }
   
   /** Helper method to move the sprite left */
   private void moveBoxUp() {
      // Save the current dimensions for repaint to erase the sprite
      int saved_y = box.y;
      // update sprite
      box.y -= 1;  // coordinate system in Java is reversed!!
      if((box.y%IMAGE_HEIGHT)==0){
       	  System.out.println("pietro"+(numberOfFloors-1-(box.y/IMAGE_HEIGHT)));
    	  connection.send(new LiftOnTheFloorEvent(numberOfFloors-1-(box.y/IMAGE_HEIGHT)));
      }
      // Repaint only the affected areas, not the entire JFrame, for efficiency
      canvas.repaint(box.x, saved_y, box.width, box.height); // Clear old area to background
      canvas.repaint(box.x, box.y, box.width, box.height); // Paint new location
   }
   
   /** Helper method to move the sprite left */
   private void moveBoxDown() {
      // Save the current dimensions for repaint to erase the sprite
      int saved_y = box.y;
      // update sprite
      box.y += 1;	 // coordinate system in Java is reversed!!
      if((box.y%IMAGE_HEIGHT)==0){
    	  System.out.println("pietro"+(numberOfFloors-1-(box.y/IMAGE_HEIGHT)));
    	  connection.send(new LiftOnTheFloorEvent(numberOfFloors-1-(box.y/IMAGE_HEIGHT)));
      }
      // Repaint only the affected areas, not the entire JFrame, for efficiency
      canvas.repaint(box.x, saved_y, box.width, box.height); // Clear old area to background
      canvas.repaint(box.x, box.y, box.width, box.height); // Paint new location
   }
   
 
   /** DrawCanvas (inner class) is a JPanel used for custom drawing */
   class DrawCanvas extends JPanel {
    
	private static final long serialVersionUID = 1L;

	@Override
      public void paintComponent(Graphics g) {
         super.paintComponent(g);
         setBackground(CANVAS_BG_COLOR);

         
         for(int i = 0; i < numberOfFloors; i++)
         {
             floorList[i].paint(g);
             for(Resident person: floorList[i].getPeople())
             {
                 person.paint(g);
             }
         }
         building.paint(g);
         shaft.paint(g);
         box.paint(g);
         
      }
   }
   
   private void eventReceiver(LiftEvent event)
   {
	   
	   if(event.getClass() == GeneratePersonEvent.class)
	   {
		  System.out.println("Cokolwiek doszlo");
		  GeneratePersonEvent e = (GeneratePersonEvent) event;
		  
		  Resident newResident = floorList[e.getHomeFloor()].addPerson(e.getId(), e.getHomeFloor(), e.getDestFloor());
		  moveManRight(newResident);
		  listOfPeople.add(newResident);
		  canvas.repaint();	   
	   }
	   
	   if(event.getClass() == LiftIsReadyEvent.class)
	   {
		   System.out.println("Lift Ready");
		   //readyToRide=true;
		   if(currentDirection!=Direction.STOP){
			   closeTheDoor();
		   }
	   }
	   
	   if(event.getClass() == GetOnEvent.class)
	   {
		   GetOnEvent e = (GetOnEvent) event;
		   goToTheLift(e.getid());
	   }
	   if(event.getClass() == GetOffEvent.class)
	   {
		   	GetOffEvent e = (GetOffEvent) event;
		   	goOutOfLift(e.getid());
	   }
	   if(event.getClass() == LiftStopEvent.class)
	   {
		   
		   readyToRide=false;
		   LiftStopEvent e = (LiftStopEvent) event;
		   System.out.println("Lift Stop " + e.getFloor());
		   lift.setCurrentFloor( e.getFloor());
		   openTheDoor();
		   if(currentDirection==Direction.UP){
			   floorList[numberOfFloors-e.getFloor()-1].setUp(false);
			   canvas.repaint();
		   }
		   else if(currentDirection==Direction.DOWN){
			   floorList[numberOfFloors-e.getFloor()-1].setDown(false);
			   canvas.repaint();
		   }
	   }
	   if(event.getClass() == ChangeDirectionEvent.class)
	   {
		   
		   if(currentDirection==Direction.STOP){
			   //readyToRide=true;
		   }
		   else{
			   readyToRide=false;
			   openTheDoor();
		   }
		   ChangeDirectionEvent e = (ChangeDirectionEvent) event;
		   System.out.println("Change Direction "+e.getNewDirection());
		   lift.setCurrentDirection(e.getNewDirection());
		   currentDirection=e.getNewDirection();
		   lift.setCurrentFloor(e.getFloor());
		   if(currentDirection==Direction.UP){
			   floorList[numberOfFloors-e.getFloor()-1].setUp(false);
			   canvas.repaint();
		   }
		   else if(currentDirection==Direction.DOWN){
			   floorList[numberOfFloors-e.getFloor()-1].setDown(false);
			   canvas.repaint();
		   }
	   }
	   
       if(event.getClass() == DownButtonEvent.class)
   	   {
    	  System.out.println("Down Button");
   		  DownButtonEvent e = (DownButtonEvent) event;
   		  floorList[numberOfFloors-e.getFloor()-1].setDown(true);
   		  canvas.repaint();	   
   	   }
       
       if(event.getClass() == UpButtonEvent.class)
   	   {
   		  System.out.println("Up Button");
    	   UpButtonEvent e = (UpButtonEvent) event;
   		  floorList[numberOfFloors-e.getFloor()-1].setUp(true);
   		  canvas.repaint();	   
   	   }
       
       if(event.getClass() == InnerButtonEvent.class)
   	   {
   		  System.out.println("Inner Button");
    	   //InnerButtonEvent e = (InnerButtonEvent) event;
   		  //floorList[numberOfFloors-e.getFloor()-1].setUp(true);
   		  //canvas.repaint();	   
   	   }

	   
   }
   
   /**
    * Wprowadza mieszkanca do windy
    * @param id
    */
   public void goToTheLift(final int id)
   {
	   Resident newResident = findPerson(id);
	   
	   //Usuwam czlowieka z pietra
	   floorList[lift.getCurrentFloor()].getOn(newResident);
           
	   //Wsadzam czlowieka do windy
           lift.addToTheLift(newResident);
           
	   while(newResident.tempX < CANVAS_WIDTH - IMAGE_WIDTH)
	   {
		   newResident.tempX++;
		   canvas.repaint();
		   
		   
	   }
           liftInsideSimulation.pole.repaint();
           buttonPanel.pole.repaint();
   }
   
   public void goOutOfLift(final int id)
   {
	   Resident newResident = findPerson(id);
	   
	   lift.removeFromTheLift(newResident);
	   // tu mozna zadbac o jakas animacje wysiadania typa z windy
	   
	   // to nie jestem pewny czy trzeba wstawic
	   liftInsideSimulation.pole.repaint();
       buttonPanel.pole.repaint();
	   
   }
   
   /**
    * Znajduje mieszkanca po zadanym id
    * @param id
    * @return
    */
   public Resident findPerson(final int id)
   {
	   Resident residentToFind = null;
	   
	   //To bedzie najczestszy przypadek
	   /*if(id == listOfPeople..get(id).getId()) :: linkedlist.get(index) pobiera po indeksie a id nie musi byc < size()
	   {
		   residentToFind = listOfPeople.get(id);
	   }
	   else
	   {*/
		   for(Resident x: listOfPeople)
		   {
			   if(x.getId() == id)
				   residentToFind = x;
		   }
	   //}
	   
	   if(residentToFind == null)
		   System.out.println("Problem jest w wsadzeniu typa do windy");
	   
	   return residentToFind;
	   
   }

   @Override
   public void run()
   {
	   while(true)
		{
			LiftEvent event = connection.receive();			

			eventReceiver(event);
		}
   }
}
