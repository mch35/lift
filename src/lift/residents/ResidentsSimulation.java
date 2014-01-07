package lift.residents;


import java.util.ArrayList;
import java.util.Random;

import lift.common.events.ChangeDirectionEvent;
import lift.common.events.GuiGeneratePersonEvent;
import lift.common.events.LiftEvent;
import lift.common.events.LiftStopEvent;
import lift.common.events.SetTimeIntervalEvent;
import lift.server.Connection;
import lift.server.ModuleID;
import lift.server.Server;
import lift.server.Timer;
import lift.server.exception.ConnectionExitsException;



public class ResidentsSimulation implements Runnable{
        
        private final ArrayList<Floor> floorList;
        Random rand = new Random();
        private int numberOfFloors;
        private final Lift lift;        
        /** Polaczenie z serwerem */
        private final Connection connection;
        private int minTimeAnticipating;
        private int maxTimeAnticipating;
        
        private int id = 0;
        
        private final Timer timer;
        
        public ResidentsSimulation(int N, final Server server) throws ConnectionExitsException
        {
        	
                this.connection = server.connect(ModuleID.MIESZKANCY);
                
                minTimeAnticipating = 5;
                maxTimeAnticipating = 10;
                
                numberOfFloors = N;
                floorList = new ArrayList<Floor>(N);
                for(int i = 0; i < N;i++)
                {
                        Floor pietro = new Floor(i,connection);
                        floorList.add(i,pietro);
                }
                lift = new Lift(8,connection);
                
                this.timer = server.getTimer();
        }
        
        /**
         * Funkcja tworzy czlowieka na losowym pietrze oraz wysyla informacje o tym gdzie
         * sie pojawil oraz w ktora strone chce isc do modulu posredniczacego
         */
        private void generatePerson()
        {
                int homeFloor = rand.nextInt(numberOfFloors + 1);
                int destFloor = rand.nextInt(numberOfFloors-1);
                if(homeFloor >= numberOfFloors)
                        homeFloor = 0;                                                                                        //zawsze najwiecej ludzi jest na zerowym
                if(destFloor == homeFloor)
                        destFloor+=1%numberOfFloors;                                                        // Jakby chcial jechac na to pietro na ktorym jest
                Person nextPerson = new Person(homeFloor, destFloor, createId());
                
                //przekazuje czlowieka "pietru"
                floorList.get(homeFloor).addPerson(nextPerson);
                System.out.println("Robie typa na " + homeFloor);
                                        
        }
        
        /**
         * Funkcja tworzaca czlowieka zleconego przez uzytkownika z podanymi parametrami
         * @param hf - home floor
         * @param df - destination floor
         */
        private void generatePerson(final int hf, final int df)
        {
                int homeFloor = hf;
                int destFloor = df;
                //jakby ktos chcial jechac do piwnicy niech idzie schodami
                if(destFloor < 0 || homeFloor < 0)
                	return;
                //jakby ktos chcial jechac do nieba to pojedzie z 0 na 1 :)
                if(homeFloor >= numberOfFloors)
                        homeFloor = 0;
                if(destFloor >= numberOfFloors)
                	destFloor = 1;
                if(destFloor == homeFloor)
                        destFloor = (destFloor+1) % numberOfFloors;                                                        // Jakby chcial jechac na to pietro na ktorym jest
                Person nextPerson = new Person(homeFloor, destFloor, createId());
                
                //przekazuje czlowieka "pietru"
                floorList.get(homeFloor).addPerson(nextPerson);
                System.out.println("Robie typa na " + homeFloor);
                                        
        }
        
        /**
         * Metoda odpowiedzialna za nadanie unikalnego id czlowiekowi aby byl rozroznialny
         * @return
         */
        private synchronized int createId()
        {
        	//Zakladam ze wiecej ludzi niz 1 000 nie wejdzie do naszego budynku
        	id+=1%1000;
        	return id;
        }
        
        /**
         * Metoda odpowiedzialna za odbieranie eventow od modulu posredniczacego
         * @param event
         */
        public synchronized void eventReciever(LiftEvent event)
        {
                if(event.getClass() == LiftStopEvent.class)
                {
                        LiftStopEvent e = (LiftStopEvent) event;
                        int floor = e.getFloor();
                        lift.liftOnTheFloor(floor,floorList.get(floor));
                }
                if(event.getClass() == ChangeDirectionEvent.class)
                {
                        ChangeDirectionEvent e = (ChangeDirectionEvent) event;
                        int floor = e.getFloor();
                        lift.setDirection(e.getNewDirection());
                        lift.liftOnTheFloor(floor,floorList.get(floor));        
                }
                
                if(event.getClass() == GuiGeneratePersonEvent.class)
                {
                	System.out.println("GUI wygenerowalo czlowieka");
                	GuiGeneratePersonEvent e = (GuiGeneratePersonEvent) event;
                	generatePerson(e.getHomeFloor(),e.getDestinationFloor());
                }
                if(event.getClass() == SetTimeIntervalEvent.class)
                {
                	SetTimeIntervalEvent e = (SetTimeIntervalEvent) event;
                	setTime(e.getMin(),e.getMax());
                }
                //TODO: ewentualnie inne eventy do wylapania
                        
        }
        
        /**
         * Funkcja odpowiedzialna za sprawdzanie czy uzytkownik nie podal jakis glupot
         * w przedziale czasu pomiedzy kolejna generacja ludzi oraz za ewentualne ustawienie
         * nowo obowiazujacego przedzialu czasu
         */
        private void setTime(int min, int max)
        {
        	//Jesli ktos podal wartosci mniejsze od zera, ustawiam wartosci defaultowe
        	if(min < 0)
        		min = 0;
        	if(max > 0)
        		max = 2;
        	
        	
        	int tmp;
        	
        	if(min > max)
        	{
        		tmp = min;
        		min = max;
        		max = tmp;
        	}
        	
        	minTimeAnticipating = min;
        	maxTimeAnticipating = max;
        }
        
        
        public String toString()
        {
                
                String kaszanka = new String();
                for(int i =0; i < numberOfFloors+1; i++)
                {
                        for(Floor x: floorList)
                        {
                                System.out.println(x + "/n");
                        }
                }
                return kaszanka;
        }
        
        /**
         * Startuje generowanie mieszkancow.
         * 
         */
        public void start()
        {
        	new Thread(new Runnable()
        	{				
				@Override
				public void run()
				{
					Random generator = new Random();
	                while(true)
	                {
	                        double randomValue = minTimeAnticipating + (generator.nextDouble()*(maxTimeAnticipating - minTimeAnticipating));
	                        
	                        long anticipatingTime = (long)(1000* randomValue);
	                        
	                        try
	                        {
	                                Object obj = new Object();
	                                timer.notifyAt(obj, anticipatingTime);
	                                synchronized(obj)
	                                {
	                                	obj.wait();
	                                }
	                                
	                        } catch (InterruptedException e)
	                        {
	                        	System.out.println("Dziwny exception");
	                                e.printStackTrace();
	                        }
	                        generatePerson();
	                }	
				}
			}).start();
        }
        
        /**
         * Obsluga przychodzacych eventow
         * 
         */
        public void run()
        {
                while(true)
                {
                	LiftEvent event = connection.receive();
                	
                	eventReciever(event);
                }
        }
}
