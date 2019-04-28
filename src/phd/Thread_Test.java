package phd;

//THREAD
/*
class ThreadDemo extends Thread {
   private Thread t;
   private String threadName;
   
   ThreadDemo( String name) {
      threadName = name;
      System.out.println("Creating " +  threadName );
   }
   
   public void run() {
      System.out.println("Running " +  threadName );
      try {
         for(int i = 4; i > 0; i--) {
            System.out.println("Thread: " + threadName + ", " + i);
            // Let the thread sleep for a while.
            Thread.sleep(10000);
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

public class Thread_Test {

   public static void main(String args[]) {
      ThreadDemo T1 = new ThreadDemo( "Thread-1");
      T1.start();
      ThreadDemo T2 = new ThreadDemo( "Thread-2");
      T2.start();
      
    try {
        System.out.println("Waiting for threads to finish.");
        T1.join();
        T2.join();
    } catch (InterruptedException e) {
      System.out.println("Main thread Interrupted");
    }      
      System.out.println("AAAAAA");
   }   
}
*/

class ThreadDemo extends Thread {
//   private Thread t;
   private String threadName;
   private int loopbound;
   
   ThreadDemo( String name, int bound) {
      threadName = name;
      loopbound=bound;
//      System.out.println("Creating " +  threadName );
   }
   
   public void run() {
      System.out.println("Running " +  threadName );
      for(int i = 0; i <= loopbound; i++) {
            System.out.println("Thread: " + threadName + ", " + i);
         }
      System.out.println("Thread " +  threadName + " exiting.");
   }
   
//   public void start () {
//      System.out.println("Starting " +  threadName );
//      if (t == null) {
//         t = new Thread (this, threadName);
//         t.start ();
//      }
//   }
}

public class Thread_Test {

   public static void main(String args[]) {
      ThreadDemo T1 = new ThreadDemo( "Thread-1",20);
      T1.start();
      ThreadDemo T2 = new ThreadDemo( "Thread-2",10);
      T2.start();
      
    try {
        System.out.println("Waiting for threads to finish.");
        T1.join();
        T2.join();
    } catch (InterruptedException e) {
      System.out.println("Main thread Interrupted");
    }      
    System.out.println("AAAAAA");
   }   
}