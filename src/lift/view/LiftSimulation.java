package lift.view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lift.common.Direction;
import lift.common.events.ChangeDirectionEvent;
import lift.common.events.GeneratePersonEvent;
import lift.common.events.GetOffEvent;
import lift.common.events.GetOnEvent;
import lift.common.events.GuiGeneratePersonEvent;
import lift.common.events.LiftEvent;
import lift.common.events.LiftIsReadyEvent;
import lift.common.events.LiftStopEvent;
import lift.common.events.SetTimeIntervalEvent;
import lift.common.events.SimulationStartEvent;
import lift.common.events.SimulationStopEvent;
import lift.server.Connection;
import lift.server.ModuleID;
import lift.server.Server;
import lift.server.exception.ConnectionExitsException;
import lift.server.exception.ServerSleepsExeption;

public class LiftSimulation extends JFrame {
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
// Name-constants for the various dimensions
   public static final int CANVAS_WIDTH = 640;
   public static final int CANVAS_HEIGHT = 504;
   public static final Color CANVAS_BG_COLOR = new Color(183, 221, 230);
   public static final int IMAGE_WIDTH = 85;
   public static final int IMAGE_HEIGHT = 126;
 
   private DrawCanvas canvas; // the custom drawing canvas (extends JPanel)

   private ArrayList<Man> residentsList;
   private Man man;
   private Man man2;
   private Building building;
   private ElevatorShaft shaft;
   private ElevatorBox box;
   
   //TODO: To chyba bedzie trzeba zmienic ale gdzies w kodzie widzialem ze tez jest na sztywno przypisywane
   private final int numberOfFloors = 4;
   
   //Lista pieter do ktorych bede dodawac ludzi w kolejce
   private LogicFloor[] floorList;
   
   private int currentFloor;
   private Direction currentDirection;
   
   private LogicLift lift;
   
   /** Polaczenie z serwerem */
   private final Connection connection;

 
   /** 
    * Constructor to set up the GUI
    * 
    * @throws ConnectionExitsException
    * @throws ServerSleepsExeption
    *  
 	*/
   public LiftSimulation(final Server server) throws Exception
   {  
	  // Set up elements of the Simulation
      man = new Man(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, 4, 2);
      man2 = new Man(0, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT, 1, 3);
      building = new Building(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, Color.BLACK);
      shaft = new ElevatorShaft(CANVAS_WIDTH-IMAGE_WIDTH, 0, IMAGE_WIDTH, CANVAS_HEIGHT, Color.YELLOW);
      box = new ElevatorBox(CANVAS_WIDTH-IMAGE_WIDTH, 0, IMAGE_WIDTH, IMAGE_HEIGHT, Color.DARK_GRAY);
      
      floorList = new LogicFloor[4];
      lift = new LogicLift();
      
      
      try
      {
		this.connection = server.connect(ModuleID.GUI);
      }
      catch (ConnectionExitsException | ServerSleepsExeption e)
      {
    	  e.printStackTrace();
    	  throw e;
      }
      
      residentsList = new ArrayList<Man>();
      residentsList.add(man);
      residentsList.add(man2);
      
      
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
      
      btnMoveManLeft.addActionListener(new ActionListener()
      {		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			moveManLeft();
			openTheDoor();
            requestFocus(); // change the focus to JFrame to receive KeyEvent
			
		}
	});
      btnMoveManRight.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			moveManRight();
			manWalkIntoLift();
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
   }
   
   /**
    * Startuje modul GUI
    * 
    */
   public void start()
   {
	   
   }

   public void openTheDoor() 
   {
	   Thread animationThread = new Thread () {
         @Override
	         public void run() {
			   while(box.width > 0)
			   {
				   box.width--;
				   canvas.repaint();
				   try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   
		       }
         	}
	   };
	   animationThread.start(); 
   }
   
   public void closeTheDoor() 
   {
	   Thread animationThread = new Thread () {
         @Override
	         public void run() {
			   while(box.width < IMAGE_WIDTH)
			   {
				   box.width++;
				   canvas.repaint();
				   try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   
		       }
         	}
	   };
	   animationThread.start(); 
   }
   
   public void manWalkIntoLift()
   {
	   Thread animationThread = new Thread () {
	         @Override
		         public void run() {
				   while(man.x < CANVAS_WIDTH-IMAGE_WIDTH)
				   {
					   man.x += 8;
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
   private void moveManLeft() {
      // Save the current dimensions for repaint to erase the sprite
      int savedX = man.x;
      // update sprite
      man.x -= 10;
      // Repaint only the affected areas, not the entire JFrame, for efficiency
      canvas.repaint(savedX, man.y, man.width, man.height); // Clear old area to background
      canvas.repaint(man.x, man.y, man.width, man.height); // Paint new location
   }
   
   /** Helper method to move the sprite left */
   private void moveManRight() {
      // Save the current dimensions for repaint to erase the sprite
      int savedX = man.x;
      // update sprite
      man.x += 10;
      // Repaint only the affected areas, not the entire JFrame, for efficiency
      canvas.repaint(savedX, man.y, man.width, man.height); // Clear old area to background
      canvas.repaint(man.x, man.y, man.width, man.height); // Paint new location
   }
   
   /** Helper method to move the sprite left */
   private void moveBoxUp() {
      // Save the current dimensions for repaint to erase the sprite
      int saved_y = box.y;
      // update sprite
      box.y -= 5;  // coordinate system in Java is reversed!!
      // Repaint only the affected areas, not the entire JFrame, for efficiency
      canvas.repaint(box.x, saved_y, box.width, box.height); // Clear old area to background
      canvas.repaint(box.x, box.y, box.width, box.height); // Paint new location
   }
   
   /** Helper method to move the sprite left */
   private void moveBoxDown() {
      // Save the current dimensions for repaint to erase the sprite
      int saved_y = box.y;
      // update sprite
      box.y += 5;	 // coordinate system in Java is reversed!!
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

         for (Man person : residentsList) {
			person.paint(g);
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
		   GeneratePersonEvent e = (GeneratePersonEvent) event;
		   LogicPerson newPerson = new LogicPerson(e.getDirection(), e.getHomeFloor());
		   
		   //przekazuje obsluge nowego czlowieka pietru
		   floorList[e.getHomeFloor()].addPerson(newPerson);
	   }
	   
	   if(event.getClass() == LiftIsReadyEvent.class)
	   {

		   //TODO: obsluga eventu
	   }
	   
	   if(event.getClass() == GetOnEvent.class)
	   {
		   GetOnEvent e = (GetOnEvent) event;
		   floorList[lift.getCurrentFloor()].getOn(lift.getCurrentDirection(), e.getNumberInLift(), lift);
	   }
	   if(event.getClass() == GetOffEvent.class)
	   {
		   	GetOffEvent e = (GetOffEvent) event;
		   	lift.getOff(e.getNumberInLift());
	   }
	   if(event.getClass() == LiftStopEvent.class)
	   {
		   LiftStopEvent e = (LiftStopEvent) event;
		   lift.setCurrentFloor( e.getFloor());
	   }
	   if(event.getClass() == ChangeDirectionEvent.class)
	   {
		   ChangeDirectionEvent e = (ChangeDirectionEvent) event;
		   lift.setCurrentDirection(e.getNewDirection());
	   }
	   
   }
}