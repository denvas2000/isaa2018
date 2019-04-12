package phd;

//RUNNABLE EXAMPLE
 
/*
class RunDemo  implements Runnable {
   public Thread t;
   public String threadName;
   
   RunDemo( String name) {
      threadName = name;
      System.out.println("Creating " +  threadName );
   }
   
   public void run() {
      System.out.println("Running " +  threadName );
      try {
         for(int i = 4; i > 0; i--) {
            System.out.println("Thread: " + threadName + ", " + i);
            // Let the thread sleep for a while.
            Thread.sleep(50);
         }
      } catch (InterruptedException e) {
         System.out.println("Thread " +  threadName + " interrupted.");
      }
      System.out.println("Thread " +  threadName + " exiting.");
   }
   
   public void start () {
      System.out.println("Starting " +  threadName );
      if (t == null) {
         t = new Thread (this, threadName);
         t.start ();
      }
   }
}



public class Thread_RunTest {

    public static void main(String args[]) {
      RunDemo R1 = new RunDemo( "Thread-1");
      R1.start();
      
      RunDemo R2 = new RunDemo( "Thread-2");
      R2.start();
    try {
        System.out.println("Waiting for threads to finish.");
        R1.t.join();
        R2.t.join();
    } catch (InterruptedException e) {
      System.out.println("Main thread Interrupted");
    }      
      System.out.println("AAAAAA");
   }//main   

   }//Thread_RunTest   

*/

class RunDemo  implements Runnable {
   private String threadName;
   private int loopbound;
   
   RunDemo( String name, int bound) {
      threadName = name;
      loopbound=bound;
      //System.out.println("Creating " +  threadName );
   }
   
   public void run() {
      System.out.println("Running " +  threadName );
      for(int i = 0; i <= loopbound; i++) {
            System.out.println("Thread: " + threadName + ", " + i);
         }
      System.out.println("Thread " +  threadName + " exiting.");
   } //run
   
   
}//RunDemo

public class Thread_RunTest {

public static void main(String args[]) {

RunDemo R1 = new RunDemo("Thread-1",10);
RunDemo R2 = new RunDemo("Thread-2",5);

Thread t1 = new Thread( R1,"t1");
t1.start();
      
Thread t2 = new Thread( R2,"t2");
t2.start();

try {
        System.out.println("Waiting for threads to finish.");
        t1.join();
        t2.join();
} 
catch (InterruptedException e) {
      System.out.println("Main thread Interrupted");
}      
      System.out.println("AAAAAA");
}//main   

}//Thread_RunTest   
