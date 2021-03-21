import java.util.concurrent.BlockingQueue;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.*;

public class Network{

    BlockingQueue<String> FAST = new LinkedBlockingDeque<>();
    BlockingQueue<String> MEDIUM = new LinkedBlockingDeque<>();
    BlockingQueue<String> SLOW = new LinkedBlockingDeque<>();

    boolean sendToController = false;
    boolean sendToMission = false;

    BlockingQueue<String> reports = new LinkedBlockingDeque<>();



    private Lock lock = new ReentrantLock();
    List<Double> bandwidth= Arrays.asList(0.02, 0.2, 2.0);

    public void getQueue(String name, double networkspeed){
        if (networkspeed == 2.0){
            FAST.add(name);
        }
        if (networkspeed == 0.2){
            MEDIUM.add(name);
        }
        if (networkspeed == 0.02){
            SLOW.add(name);
        }
    }

    public String checkSpeed(String name){
        if(FAST.contains(name)){
            System.out.println("Network speed @ 2.0 -- FAST network connection ");
            return "FAST";
        }
        if(MEDIUM.contains(name)){
          System.out.println("Network speed @ 0.2 -- MEDIUM network connection");
            return "MEDIUM";

        }
        else{
            System.out.println("Network speed @ 0.02 -- SLOW network connection");
            return "SLOW";

        }
    }

    public double getSpeed(){
        Random rand = new Random();
        double randomElement = bandwidth.get(rand.nextInt(bandwidth.size()));
        return randomElement;
    }

    public void sleep(int updateDevLength, int speed){
        try {
            Thread.sleep(updateDevLength * speed);
        }
        catch (InterruptedException e){

            System.out.println("Update Development has been interrupted");

        }
    }

    public void developSoftwareUpdate(String name){
        Random rand = new Random();
        int updateDevLength = (rand.nextInt(9-2) + 2);
        String networkq = checkSpeed(name);
        if (networkq == "FAST") {
            sleep(updateDevLength, 2000);
        }

        if(networkq == "MEDIUM") {
            sleep(updateDevLength, 4000);
        }

        if (networkq == "SLOW"){
            sleep(updateDevLength, 6000);
    }}

    public void initializeSoftwareUpdate(String name){
        Random rand = new Random();
        int updateLength = (rand.nextInt(9-2) + 2);
        String networkq = checkSpeed(name);
        System.out.println("Updating..");
        if (networkq == "FAST") {
            sleep(updateLength, 2000);
        }

        if(networkq == "MEDIUM") {
            sleep(updateLength, 4000);
        }

        if (networkq == "SLOW") {
            sleep(updateLength, 60000);
        }
    }

//    public void componentsQueue(String name,HashMap<String,List> hash) {
//
//        List components = hash.get(name);
//
//        for (int i = 0; i < components.size(); i++) {
//            double speed = 2.0;
//
//            if (speed == 2.0){
//                System.out.println(components.get(i));
//            }
//        }
//    }
    public void AddComponentReportToControllerQ(String name, double networkSpeed , Object component, String message, int messageSize){
        System.out.println(name + " Component: " + component + " makes request to network: " + networkSpeed + " at time: " + " for message: " + message);
//        reports.add(message); // add report to queue
//        SendReportToController();

    }
    public void AddReportToControllerQ(String name, double networkSpeed , String messageType, int messageSize){
        Report re = new Report();
        String message = re.Report(messageType, messageSize);
        System.out.println(name + " makes request to network: " + networkSpeed + " at time: " + " for message: " + message);
        reports.add(message); // add report to queue.
        SendReportToController();
    }

    public void SendReportToController(){
        String message = reports.poll(); //.poll gets the first report from the queue and removes it.
        MissionController mc = new MissionController(0);
        mc.recieveReport(message); //send report to mission controller
    }



    }