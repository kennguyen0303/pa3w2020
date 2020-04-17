/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
	 static int numberOfPhilosopher;//number of philosopher attribute
	private static boolean[] haveEnoughChopstick;// checking if the person can eat
	private static boolean[] haveEaten;// checking if the person have ever eaten?
	private static int numberOfPeopleHaveNotEaten;//number of people never eat to prevent starving
	enum state {FULL,HUNGRY,EATING,THINKING};//three states
	private static state[] state_arr;//state for each phil
	private static int numbOfPeopleEating=0;
	private static int numbOfPeopleTalking=0;
	//private static boolean MUTEX = false;

	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		// TODO: set appropriate number of chopsticks based on the # of philosophers
		numberOfPhilosopher=piNumberOfPhilosophers;//set the number of philosopher to the input
		haveEnoughChopstick=new boolean[piNumberOfPhilosophers];
		numberOfPeopleHaveNotEaten=piNumberOfPhilosophers;
		haveEaten=new boolean[piNumberOfPhilosophers];
		state_arr=new state[piNumberOfPhilosophers];
		//initialize the arrays
		for(int i=0;i<piNumberOfPhilosophers;i++) {
			state_arr[i]=state.THINKING;//setting default as THINKING
			haveEaten[i]=false;
			haveEnoughChopstick[i]=false;
		}
		//the default is 4 set in DiningPhilosophers.java
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */
	public synchronized void test(final int piTID) {//passing index value from pickup, so that need to increment for accurate pTID
		int index=piTID;
		if((state_arr[index]==state.HUNGRY)
				&&(state_arr[(index+1)%numberOfPhilosopher]!=state.EATING)
				&&(state_arr[(index+(numberOfPhilosopher-1))%numberOfPhilosopher]!=state.EATING)
			) {
			//System.out.println("*******TID:"+(piTID+1)+"my left:"+state_arr[(index+1)%numberOfPhilosopher]);
			//System.out.println("*******TID:"+(piTID+1)+"my right:"+state_arr[(index+(numberOfPhilosopher-1))%numberOfPhilosopher]);
			state_arr[index]=state.EATING;//turn to eating
			haveEnoughChopstick[index]=true;
			//System.out.println("******DEBUG-TEST-HAVE ENOUGH CHOPSTICK:"+haveEnoughChopstick[index]+", how many talking: "+numbOfPeopleTalking+"\n"
					//+"how many is eating: "+numbOfPeopleEating);
		}
		else haveEnoughChopstick[index]=false;
	}
	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{
		//while(MUTEX) try{//try to obtain the mutex
			//this.wait();
		//}catch(Exception e) {
			//System.out.println(e.getMessage());
		//};
		//at here, get the mutex, mutex = true
		//MUTEX=true;
		int index=piTID-1;
		// ...
		//check if this person ever eat
		System.out.println("People never eat: "+numberOfPeopleHaveNotEaten+" is phils id: "+piTID+" eaten: ? "+haveEaten[index]+" ");
		while((numberOfPeopleHaveNotEaten>0&&haveEaten[index])) try {//have this wait for people who never eaten
			this.wait();//have this thread sleep 
			}catch(Exception e) {
			System.out.println(e.getMessage());
			};
		//either everyone has eaten or this person has never eaten or no one is talking
		state_arr[index]=state.HUNGRY;
		this.test(index);//test if it is possible to collect all the chopstick
		//System.out.println("test was run in pTID "+piTID);
		while(!haveEnoughChopstick[index]) {//not enough chopstick, wait until the others finish eating
			try {
				this.wait();//have this thread sleep or this monitor sleep ? Need to check
				this.test(index);//test again after being waken up by others
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		while(numbOfPeopleTalking>0) {//if somebody is talking, wait
			try {
				//System.out.println("Thread "+piTID+" is have chopstick: "+haveEnoughChopstick[index]+" waiting to someone is talking to eat");
				this.wait();//have this thread sleep or this monitor sleep ? Need to check
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		numbOfPeopleEating++;
		if(!haveEaten[index]) numberOfPeopleHaveNotEaten--;
		System.out.println("******I am,"+piTID+", eating, how many talking: "+numbOfPeopleTalking+"\n"
				+"how many is eating: "+numbOfPeopleEating+" enough chopstick: "+haveEnoughChopstick[index]);
		//release mutex
		//MUTEX = false;
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		int index=piTID-1;
		//Need: 1. set state to full
		state_arr[index]=state.FULL;
		//2. haveEaten => true
		haveEaten[index]=true;
		//4.people eating decrement
		numbOfPeopleEating--;
		//5. no longer have the chopstick
		//haveEnoughChopstick[index]=false;//reset the value
		//4. notify the others neighbors
		this.notifyAll();
		//MUTEX=false;
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		// Requirement: only one talking; no one eating
		//System.out.println("***************Want to talk ... ");
		while(numbOfPeopleEating>0||numbOfPeopleTalking>0) {//if somebody is either talking or eating wait
			try{
				//System.out.println("DEBUG- waiting to talk ");
				this.wait();
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		//1.2.  set state to TALKING_TOM - increment the counter
		numbOfPeopleTalking++;
		//System.out.println("******I am talking, how many talking: "+numbOfPeopleTalking+"\n"
			//	+"how many is eating: "+numbOfPeopleEating);
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		//1.decrement the counter
		numbOfPeopleTalking--;
		//2. notify others to do something else
		this.notifyAll();
	}
}

// EOF
