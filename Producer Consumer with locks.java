/*Producer Consumer Problem with locks*/
import java.util.Concurrent.locks.Lock;
import java.util.Concurrent.locks.Condition;
import java.util.Concurrent.locks.ReentrantLock;


class ProducerConsumer{
    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();
    
    public static void produce throws InterruptedException(){
      lock.lock();                        //await is similar to wait in case of synchronization
      System.out.println("Producer Method");
      condition.await();
      System.out.println("Producer Method again");
      lock.unlock();
    }
    
    public static void consume throws InterruptedException(){
       lock.lock();                     //signalAll is similar to signal in case of synchronization
       Thread.sleep(2000);
       System.out.println("Consumer Method");
       condition.signalAll();
       lock.unlock();
    }
}

public class Solution{
    public static void main(String args[]){
        ProducerConsumer producerConsumer = new ProducerConsumer();
        
        Thread t1 = new Thread(new Runnable(){
           @Override
           public void run(){
               try{
               producerConsumer.produce();}
               catch(InterruptedException e){
                   e.printStackTrace();
               }
               
           }
        });
        
        Thread t2 = new Thread(new Runnable(){
           @Override
           public void run(){
               try{
               producerConsumer.consume();
               }
               catch(InterruptedException e){
                   e.printStackTrace();
               }
           }
        });
        
        
        t1.start();
        t2.start();
        
    } 
}
