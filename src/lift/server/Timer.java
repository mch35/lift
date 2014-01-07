package lift.server;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class Timer implements Runnable
{
	private long currentTime;
	
	private final long startTime;
	
	private final LinkedBlockingQueue<ToNotify> toNotify;
	
	private final LinkedBlockingQueue<Long> whenNotify;
	
	private final Object toWait;
	
	private final Object nextStep;
	
	private Boolean isWaiting;
	
	private Boolean stepWorking;
	
	public Timer()
	{
		currentTime = System.currentTimeMillis();
		toNotify = new LinkedBlockingQueue<>();
		whenNotify = new LinkedBlockingQueue<>();
		toWait = new Object();
		nextStep = new Object();
		isWaiting = true;
		stepWorking = false;
		startTime = currentTime;
	}
	
	public synchronized long getTime()
	{
		return currentTime;
	}
	
	public synchronized long getStartTime()
	{
		return startTime;
	}
	
	public synchronized void increment()
	{
		++currentTime;
	}	
	
	public void notifyAt(final Object who, final long interval)
	{
		long when = currentTime + interval;
		
		if(when > currentTime)
		{
			toNotify.add(new ToNotify(who, when));
			
			if(!whenNotify.contains(when))
			{
				try
				{
					whenNotify.put(when);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		}
		else
		{
			who.notify();
		}		
	}
	
	public synchronized void start()
	{
		synchronized(toWait)
		{
			isWaiting = false;
			toWait.notify();
		}
	}
	
	public synchronized void stop()
	{
		synchronized(isWaiting)
		{
			isWaiting = true;
		}
	}
	
	public synchronized void doStep()
	{
		synchronized(nextStep)
		{
			nextStep.notify();
		}
	}
	
	public synchronized void setStepWorking(Boolean stepWorking)
	{
		synchronized(this.stepWorking)
		{
			if(!stepWorking)
			{
				synchronized(nextStep)
				{
					nextStep.notify();
				}
			}
			this.stepWorking = stepWorking;
		}
	}
	
	public synchronized Boolean getIsStepWorking()
	{
		return stepWorking;
	}
	
	public synchronized Boolean getIsWaiting()
	{
		return isWaiting;
	}

	@Override
	public void run()
	{
		long oldTime;
		while(true)
		{
			if(getIsStepWorking())
			{
				synchronized (nextStep)
				{
					try
					{
						nextStep.wait();
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				
				while(!whenNotify.contains(currentTime))
				{
					increment();
					System.out.println(currentTime);
				}
			}
			else
			{
				oldTime = System.nanoTime();
				synchronized(toWait)
				{
					if(getIsWaiting())
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
				
				while(System.nanoTime() - oldTime < 1000000);
				increment();
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
					whenNotify.remove(notify.when);
					it.remove();
				}
			}
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
