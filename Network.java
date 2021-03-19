// import java.util.concurrent.BlockingQueue;
// import java.util.Random;
// import java.util.*;

// public class Network implements Runnable {
//     private BlockingQueue<Integer> numbersQueue;

//     public void getSpeed(){
//         //pick a bandwidth at random
//         List<Double> bandwidth= Arrays.asList(0.02, 0.2, 2.0);
//         Random rand = new Random();
//         double randomElement = bandwidth.get(rand.nextInt(bandwidth.size()));
//         System.out.println("Bandwidth: " + randomElement);
//     }

//     public void run() {
//       getSpeed();
//     }
// }
import java.util.concurrent.BlockingQueue;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.*;

public class Network implements Runnable {
    public static Map<String, Double> failureThreads = new ConcurrentHashMap<>(3);
    BlockingQueue<String> waiting_Queue = new LinkedBlockingDeque<>();

    BlockingQueue<String> FAST = new LinkedBlockingDeque<>();
    BlockingQueue<String> MEDIUM = new LinkedBlockingDeque<>();
    BlockingQueue<String> SLOW = new LinkedBlockingDeque<>();

    private Lock lock = new ReentrantLock();
    List<Double> bandwidth= Arrays.asList(0.02, 0.2, 2.0);


    public double getSpeed(){
        Random rand = new Random();
        double randomElement = bandwidth.get(rand.nextInt(bandwidth.size()));
        return randomElement;
    }

    public void getQueue(){
        System.out.println("Failure Threads: " + failureThreads);
        System.out.println("Waiting Queue: " + waiting_Queue);
    }

    public int developSoftwareUpdate(){
        Random rand = new Random();
        return (rand.nextInt(9-2) + 2);
    }

    public int initializeSoftwareUpdate(){
        Random rand = new Random();
        return (rand.nextInt(9-2) + 2);
    }

    public synchronized void failure(){ //changed to synchronised as threads where reading at the same time.
        double x = getSpeed(); //get a random network speed
        System.out.println(Thread.currentThread().getName() + " " + x );
        if(!failureThreads.containsValue(x)){ //if the network speed is not in use, add the thread and speed to the hashmap.
            failureThreads.put(Thread.currentThread().getName(), x);
        }
        else{
          waiting_Queue.add(Thread.currentThread().getName());
            for (double i : bandwidth) {
                       if (!failureThreads.containsValue(i) && (failureThreads.size() <= 3)) {
                          System.out.println(Thread.currentThread().getName() + " failureThread doesnt contain " + i);
                            failureThreads.put(Thread.currentThread().getName(), i);
                        }
                   }
        }

        }

    public void run(){
    }
}