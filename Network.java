import java.util.concurrent.BlockingQueue;
import java.util.Random;
import java.util.concurrent.*;
import java.util.*;

public class Network{
    BlockingQueue<String> reports = new LinkedBlockingDeque<>();
    boolean avalbility = false;
    int updateSize;

    List<Double> bandwidth= Arrays.asList(0.02, 0.2, 2.0);

    public void sleep(double speed){
        if (speed == 2.0 ){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {

                System.out.println("has been interrupted");

            }
        }
        if (speed == 0.2 ){
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {

                System.out.println("has been interrupted");

            }
        }
        if (speed == 0.02 ){
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {

                System.out.println("has been interrupted");

            }
        }
    }


    public double getSpeed(){
        Random rand = new Random();
        double randomElement = bandwidth.get(rand.nextInt(bandwidth.size()));
        return randomElement;
    }

    public void SoftwareUpdateAvailable(int US){
        avalbility = true;
        updateSize = US;

    }

    public int developSoftwareUpdate(){
        Random rand = new Random();
        int updateDevLength = (rand.nextInt(9-2) + 2);
        int bits = (rand.nextInt(9-2) + 2);
        try {
            Thread.currentThread().sleep(updateDevLength * 2000);
        }
        catch (InterruptedException e){

            System.out.println("Update Development has been interrupted");

        }
        return bits;
    }

    public void AddComponentReportToControllerQ(String name, double networkSpeed , Object component, String message, MissionController controller){
        System.out.println(name + " Component: " + component + " makes request to network: " + networkSpeed + " at time: " + " for message: " + message);
        reports.add(message); // add report to queue
        SendReportToController(name, controller, networkSpeed);

    }
    public void AddReportToControllerQ(String name, double networkSpeed , String messageType, int messageSize, MissionController controller){
        Report re = new Report();
        String message = re.Report(messageType, messageSize);
        System.out.println(name + " makes request to network: " + networkSpeed + " at time: " + " for message: " + message);
        reports.add(message); // add report to queue.
        SendReportToController(name, controller, networkSpeed);
    }

    public void SendReportToController(String name, MissionController controller, double networkSpeed){
        String message = reports.poll(); //.poll gets the first report from the queue and removes it.
        sleep(networkSpeed);
        controller.recieveReport(name, message, this, networkSpeed);
        //send report to mission controller
    }

    public String SendReportToMission(String name, double networkSpeed, String message){
        message = (name + " makes request to network: " + networkSpeed + " at time: " + " for message: " + message);
        sleep(networkSpeed);
        return message;
    }
}