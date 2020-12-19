class Worker implements Runnable{
    //When it is made volatile the variable is stored in main memory and not in cache
    private volatile boolean terminated;
    @Override
    public void run(){
        while(!terminated){
            System.out.println("Worker class is running");
            try{
                Thread.sleep(1000);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    
    public boolean isTerminated(){
        return terminated;
    }
    
    public void setTerminated(boolean terminated){
        this.terminated = terminated;
    }
}



public class Volatile{
    public static void main(String args[]){
        Worker worker = new Worker();
        Thread t1 = new Thread(worker);
        t1.start();
    }
}
