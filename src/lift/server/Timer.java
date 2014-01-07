package lift.server;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class Timer implements Runnable
{
	private long currentTime;
	
	private final LinkedBlockingQueue<ToNotify> toNotify;
	
	private final Object toWait;
	
	private Boolean isWaiting;
	
	public Timer()
	{
		currentTime = System.currentTimeMillis();
		toNotify = new LinkedBlockingQueue<>();
		toWait = new Object();
		isWaiting = true;
	}
	
	public synchronized long getTime()
	{
		return currentTime;
	}
	
	public synchronized void increment()
	{
		++currentTime;
	}	
	
	public void notifyAt(final Object who, final long interval)
	{
		System.out.println("new");
		long when = currentTime + interval;
		
		if(when > currentTime)
		{
			toNotify.add(new ToNotify(who, when));
		}
		else
		{
			who.notify();
		}		
	}
	
	public void start()
	{
		synchronized(toWait)
		{
			isWaiting = false;
			toWait.notify();
		}
	}
	
	public void stop()
	{
		synchronized(isWaiting)
		{
			isWaiting = true;
		}
	}
	

	@Override
	public void run()
	{
		long oldTime;
		while(true)
		{
			oldTime = System.nanoTime();
			synchronized(toWait)
			{
				if(isWaiting)
				{
					try
					{
						toWait.wait();
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			Iterator<ToNotify> it = toNotify.iterator();
			
			while(it.hasNext())
			{
				ToNotify notify = it.next();
				if(notify.when <= currentTime)
				{
					synchronized(notify.who)
					{
						notify.who.notify();
					}
					it.remove();
				}
			}
			
			while(System.nanoTime() - oldTime < 1000000);
			increment();
		}
	}
	
	private class ToNotify
	{
		private final Object who;
		private final long when;
		
		public ToNotify(final Object who, final long when)
		{
			this.who = who;
			this.when = when;
		}
		
		public Object getWho()
		{
			return who;
		}
		
		public long getWhen()
		{
			return when;
		}
	}	
}