import java.util.Concurrent.lock.Lock;
import java.util.Concurrent.lock.ReentrantLock;

public class Deadlock{
    private Lock lock1 =  new ReentrantLock(true);
    private Lock lock2 =  new ReentrantLock(true);
    
    public static void main(String args[]){
       Deadlock deadLock = new Deadlock();
       
       //Possible to create threads like this after java8
       new Thread(deadLock::worker1,"worker1").start();
       new Thread(deadLock::worker2,"worker2").start();
       
    }
    
    public void worker1(){
        lock1.lock();
        System.out.println("Worker1 thread is acquiring the lock1");
        try{
        Thread.sleep(3000);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        lock2.lock();
         System.out.println("Worker1 thread is acquiring the lock2");
        lock1.unlock();
        lock2.unlock();
    }
    
    public void worker2(){
        lock2.lock();
        System.out.println("Worker2 thread is acquiring the lock2");
        try{
        Thread.sleep(3000);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        lock1.lock();
         System.out.println("Worker2 thread is acquiring the lock1");
        lock2.unlock();
        lock1.unlock();
    }
    
}
