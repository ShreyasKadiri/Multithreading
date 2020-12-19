//Dining Philosophers Problem

/*
Executor Framework:
-> Java has the functionality og Executor Framework internally that makes multithreading very efficient by
offering executors
->Thread Pool is like a collection of threads where new threads can be added or removed

Types of executors:
i) SingleThreadExecutor
This has a single thread. Every process is executed by a single thread  

ii)FixedThreadPool
Create a thread pool of n threads
If there are more processes than n then they are stored in LinkedBlockingQueue

iii)CachedThreadPool
The number of threads is not bounded, if all the threads are busy executing some tasks and new task comes a new thread
will be added

iv)ScheduledExceutor
We can execute operations at regular intervals

*/


/***************************************************************************************************/
class Constants{
    Constants(){}
   
    public static final int NO_OF_PHILOSOPHERS = 5;
    public static final int NO_OF_CHOPSTICKS = 5;
    public static final int SIMULATION_RUNNING_TIME = 5*1000;
}

/***************************************************************************************************/
public enum  State{
    LEFT, RIGHT;
}

/***************************************************************************************************/

import java.util.Concurrent.locks.Lock;
import java.util.Concurrent.locks.ReentrantLock;

public class Chopstick{
    private static int id = 0;
    private Lock lock;
   
    Chopstick(int id){
        this.id = id;
        this.lock = new ReentrantLock();
    }
   
    public boolean pickUpChopstick(Philosopher philosopher, State state) throws InterruptedException {
        if(lock.tryLock(10, TimeUnit.MILLISECONDS)){
            System.out.println(philosopher + "picked up" + state.toString() + " "+  this);
            return true;
        }
        return false;
    }
   
    @Override
    public String toString(){
        return "Chopstick" + id;
    }
   
    public void putDown(Philosopher philosopher, State state) throws InterruptedException {
        lock.unlock();
        System.out.println(philosopher + "puts down" + state.toString() + " " + this);
    }
}

/***************************************************************************************************/

public class Philosopher implements Runnable{
    private int id;
    private volatile boolean full;
    private int eatingCounter = 0;
    private Chopstick leftChopstick;
    private Chopstick rightChopstick;
    private Random random;
   
    public Chopstick(int id, Chopstick leftChopstick, Chopstick rightChopstick){
        this.id=id;
        this.leftChopstick = leftChopstick;
        this.rightChopstick = rightChopstick;
        this.random = new Random();
    }
   
   
    @Override
    public void run(){
        try{
            while(!full){
                think();
                if(leftChopstick.pickUpChopstick(this, State.LEFT)){
                    if(rightChopstick.pickUpChopstick(this, State.RIGHT)){
                        eat();
                        rightChopstick.putDown(this, State.RIGHT);
                    }
                    leftChopstick.putDown(this, State.LEFT);
                }  
            }
        }
        catch(Exception e){
            e.prinStackTrace();
        }
    }
   
    private void think() throws InterruptedException{
        System.out.println(this + "is thinking");
        //The philosopher thinks for a random time [1,1000]
        Thread.sleep(random.nextInt(1000));
    }
   
    private void eat() throws InterruptedException{
        System.out.println(this + "is eating");
        eatingCounter++;
        Thread.sleep(random.nextInt(1000));
    }
   
    public void setFull(boolean full){
        this.full=full;
    }
   
   
    public boolean isFull(){
        return this.full;
    }
    
    public int getEatingCounter(){
        return this.eatingCounter;
    }
   
   
    @Override
    public String toString(){
        return "Philosopher" + id;
    }
}

 /**************************************************************************************/
 public class Solution{
     public static void main(String args[]) throws InterruptedException{
          Philosopher philosophers[] = null;
          Chopstick chopsticks[]= null;
          ExecuterService executerService[] = null;
          try{
             philosophers[] = new Philosopher[Constants.NO_OF_PHILOSOPHERS];
             chopsticks[] = new Chopstick[Constants.NO_OF_CHOPSTICKS];
             
             for(int i=0; i<Constants.NO_OF_CHOPSTICKS; i++)
             chopsticks[i] = new Chopstick(i);
             executerService = Executors.newFixedThreadPool(Constants.NO_OF_PHILOSOPHERS);
             
             for(int i=0; i<Constants.NO_OF_PHILOSOPHERS; i++){
             philosophers[i] = new Philosopher(i, chopsticks[i], chopsticks[(i+1)%Constants.NO_OF_CHOPSTICKS]);
             executerService.execute(philosophers[i]);
            }
            Thread.sleep(Constants.SIMULATION_RUNNING_TIME);
            
            for(Philosopher philosopher : philosophers){
                philosopher.setFull(true);
             }
          }
         
          finally{
            executerService.shutdown();
            while(!executerService.isTerminated())
                Thread.sleep(1000);
                             
            for(Philosopher philosopher : philosophers){
             System.out.println(philosopher + "eat #" + philosopher.getEatingCounter() + "times");
            }
          }
         
     }
 }
