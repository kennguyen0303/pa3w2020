import common.BaseThread;

/**
 * Class Philosopher.
 * Outlines main subrutines of our virtual philosopher.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;
	
	
	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done eating.
	 */
	public void eat()
	{
		try
		{
			// ...
			//condition for before start eating
			//start eating
			System.out.println("Phil with id: "+this.iTID+" START eating");
			this.randomYield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			this.randomYield();
			System.out.println("Phil with id: "+this.iTID+" FINISH eating");
			// what to do after finish eating
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done thinking.
	 */
	public void think()
	{
		try
		{
			// ...
			//condition for before start thinking
			System.out.println("Phil with id: "+this.iTID+" START thinking");
			this.randomYield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			this.randomYield();
			System.out.println("Phil with id: "+this.iTID+" FINISH thinking");
			// what to do after finish thinking
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.think():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
		
	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - yield
	 * - Say something brilliant at random
	 * - yield
	 * - The print that they are done talking.
	 */
	public void talk()
	{
		// ....
			//condition for before start eating
			//start eating
			System.out.println("Phil with id: "+this.iTID+" START talking\n");
			this.randomYield();
			saySomething();
			this.randomYield();
			System.out.println("Phil with id: "+this.iTID+" FINISH talking\n");
			// what to do after finish eating
		
	}

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	public void run()
	{
		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++)
		{
			DiningPhilosophers.soMonitor.pickUp(getTID());

			eat();

			DiningPhilosophers.soMonitor.putDown(getTID());

			think();//is blocked here ?

			/*
			 * TODO:
			 * A decision is made at random whether this particular
			 * philosopher is about to say something terribly useful.
			 */
			int randomNumber = (int)((Math.random())*Monitor.numberOfPhilosopher)+1;
			System.out.println("************random value: "+randomNumber+" for Thread: "+this.getTID());
			if(this.iTID==randomNumber) 
			{
				// Some monitor ops down here...
				System.out.println("Hello, TID: "+this.iTID+" getting request to talk");
				DiningPhilosophers.soMonitor.requestTalk();
				talk();
				DiningPhilosophers.soMonitor.endTalk();
				
				// ...
			}

			yield();
		}
	} // run()

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	public void saySomething()
	{
		String[] astrPhrases =
		{
			"EHH... I AM A GENIUS !!!!!!!!!",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID() + ""
		};

		System.out.println
		(
			"Philosopher " + getTID() + " says: " +
			astrPhrases[(int)(Math.random() * astrPhrases.length)]
		);
	}
}

// EOF
