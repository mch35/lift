package lift.residents;


import java.util.ArrayList;
import java.util.Random;

import lift.common.events.ChangeDirectionEvent;
import lift.common.events.GuiGeneratePersonEvent;
import lift.common.events.LiftEvent;
import lift.common.events.LiftOnTheFloorEvent;
import lift.common.events.SetTimeIntervalEvent;
import lift.server.Connection;
import lift.server.ModuleID;
import lift.server.Server;
import lift.server.exception.ConnectionExitsException;
import lift.server.exception.ServerSleepsExeption;



public class ResidentsSimulation implements Runnable{
        
        private final ArrayList<Floor> floorList;
        Random rand = new Random();
        private int numberOfFloors;
        private final Lift lift;        
        /** Polaczenie z serwerem */
        private final Connection connection;
        private int minTimeAnticipating;
        private int maxTimeAnticipating;
        
        public ResidentsSimulation(int N, final Server server) throws ConnectionExitsException, ServerSleepsExeption
        {
        	
                this.connection = server.connect(ModuleID.MIESZKANCY);
                
                minTimeAnticipating = 0;
                maxTimeAnticipating = 2;
                
                numberOfFloors = N;
                floorList = new ArrayList<Floor>(N);
                for(int i = 0; i < N;i++)
                {
                        Floor pietro = new Floor(i,connection);
                        floorList.add(i,pietro);
                }
                lift = new Lift(8,connection);
        }
        
        /**
         * Funkcja tworzy czlowieka na losowym pietrze oraz wysyla informacje o tym gdzie
         * sie pojawil oraz w ktora strone chce isc do modulu posredniczacego
         */
        private void generatePerson()
        {
                int homeFloor = rand.nextInt(numberOfFloors + 3);
                int destFloor = rand.nextInt(numberOfFloors);
                if(homeFloor >= numberOfFloors)
                        homeFloor = 0;                                                                                        //zawsze najwiecej ludzi jest na zerowym
                if(destFloor == homeFloor)
                        destFloor+=1%numberOfFloors;                                                        // Jakby chcial jechac na to pietro na ktorym jest
                Person nextPerson = new Person(homeFloor, destFloor);
                
                //przekazuje czlowieka "pietru"
                floorList.get(homeFloor).addPerson(nextPerson);
                System.out.println("Robie typa na " + homeFloor);
                                        
        }
        
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
                        destFloor+=1%numberOfFloors;                                                        // Jakby chcial jechac na to pietro na ktorym jest
                Person nextPerson = new Person(homeFloor, destFloor);
                
                //przekazuje czlowieka "pietru"
                floorList.get(homeFloor).addPerson(nextPerson);
                System.out.println("Robie typa na " + homeFloor);
                                        
        }
        
        /**
         * Metoda odpowiedzialna za odbieranie eventow od modulu posredniczacego
         * @param event
         */
        public synchronized void send(LiftEvent event)
        {
                System.out.println("Kaszanka");
                if(event.getClass() == LiftOnTheFloorEvent.class)
                {
                        LiftOnTheFloorEvent e = (LiftOnTheFloorEvent) event;
                        int floor = e.getFloor();
                        lift.liftOnTheFloor(floor,floorList.get(floor));
                }
                if(event.getClass() == ChangeDirectionEvent.class)
                {
                        ChangeDirectionEvent e = (ChangeDirectionEvent) event;
                        lift.setDirection(e.getNewDirection());
                        
                }
                
                if(event.getClass() == GuiGeneratePersonEvent.class)
                {
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
        
        public void run()
        {
                Random generator = new Random();
                while(true)
                {
                        generatePerson();
                        double randomValue = minTimeAnticipating + (generator.nextDouble()*(maxTimeAnticipating - minTimeAnticipating));
                        
                        long anticipatingTime = (long)(100* randomValue);
                        
                        try
                        {
                                Thread.sleep(anticipatingTime);
                        } catch (InterruptedException e)
                        {
                                e.printStackTrace();
                        }
                }
                
        
        }
        

        
        
        

}
