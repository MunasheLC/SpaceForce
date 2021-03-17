import java.util.concurrent.BlockingQueue;
import java.util.Random;
import java.util.*;

public class Network implements Runnable {
    private BlockingQueue<Integer> numbersQueue;

    public void getSpeed(){
        //pick a bandwidth at random
        List<Double> bandwidth= Arrays.asList(0.02, 0.2, 2.0);
        Random rand = new Random();
        double randomElement = bandwidth.get(rand.nextInt(bandwidth.size()));
        System.out.println("Bandwidth: " + randomElement);
    }

    public void run() {
       getSpeed();
    }
}