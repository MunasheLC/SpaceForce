import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Mission extends Thread{

    private volatile boolean missionInProgress = true;

    int fuel;
    int dividedFuel;
    HashMap<String, HashMap<String,Integer>> components;
    Double networkSpeed;
    String stage;
    String status;
    String name;
    String destination;


    //Supplies allocated by mission controller based on destination planet
    public Mission ( HashMap components, String name, String destination, double networkSpeed ) {
        this.components = components;
        this.stage = "boost";
        this.name= name;
        this.destination = destination;
        this.status= "normal";
        this.networkSpeed = networkSpeed;

    }


    //Calculate Failure Rate
    public boolean checkFailure(int failureRate) {

        int random = ThreadLocalRandom.current().nextInt(0, 100);
//        System.out.println("failure calc is: " + random);
        return random <= failureRate;
    }

    public synchronized boolean errorCheck(){
        boolean failchecker = true; //currently failing
        Random rand = new Random();
        int failureProbability = rand.nextInt(6-5) + 5;
        System.out.println("======== " + "Failure report for " + name + " at stage: " + stage +" ========");
        System.out.println("Checking for Software Update..");
        if (failureProbability == 5){
            System.out.println("Update available");

            Network nt = new Network();
            nt.getQueue(name, networkSpeed);
            transmitReport();
//            nt.componentsQueue(name,hash);
//            System.out.println("fast net" + nt.fastNetwork);

            System.out.println("Developing Software Update..");
            nt.developSoftwareUpdate(name);

            System.out.println("Update received");
            System.out.println("Starting Update..");
            nt.initializeSoftwareUpdate(name);

            System.out.println("Update complete.");
            System.out.println(name +" has recovered ");
            System.out.println("=============================================================");
            System.out.println();
            failchecker = false; //not failing
            return failchecker;
        }

        else{
            System.out.println("No update for " + name + " at stage: " + stage);
            System.out.println("Systems failed");
            System.out.println("=============================================================");
            return failchecker;
        }
    }


    public String missionInitilize(){

        String missionStatus;
        missionStatus = "The destination for " + name + " is " + destination;

        return missionStatus;
    }


    // Method to calculate distance
    private int calculateDistance() {
        return fuel * 10;
    }

    // Boost is instant, run first
    private Boolean boostStage(){
        System.out.println("component list " + components);
        if (!checkFailure(10)){
            try {
                transmitReport();
                Thread.sleep(500);

            } catch (InterruptedException e){

                System.out.println( name + " to" + destination + " has been interrupted");

            }

            stage = "interplanetary transit stage";

            int fuel = components.get(name).get("Fuel");
            int m = (fuel/3);
            dividedFuel = m;

            Components system_components = new Components();
            int newFuel = system_components.getFuel(fuel, dividedFuel);
            components.get(name).replace("Fuel", newFuel);
            System.out.println("component list new " + components);
            transmitReport();
            return true;
        }
        else{
            status = "failing";
            transmitReport();
            if (!errorCheck()){
                status = "recovered";
                transmitReport();
                return true;
            }
            return false;

        }
    }

    // Variable time to complete based on the amount of fuel and the destination
    private Boolean interplanetaryTransitStage(){

        if (!checkFailure(10)){
            try {

                int distance = calculateDistance();

                transmitReport();
                Thread.sleep(distance);

            } catch (InterruptedException e){

                System.out.println( name + " to" + destination + " has been interrupted");

            }

            stage = "exploration stage";
//            System.out.println( name + " to" + destination + " is entering exploration stage");

            Components system_components = new Components();
            int fuel = components.get(name).get("Fuel");
            int newFuel = system_components.getFuel(fuel, dividedFuel);
            components.get(name).replace("Fuel", newFuel);
            System.out.println("component list new " + components);
            transmitReport();
            return true;
        }
        else{
            status = "failing";
            transmitReport();
            if (!errorCheck()){
                status = "recovered";
                transmitReport();
                return true;
            }
            return false;

        }
    }

    // Variable time to complete based on planet size
    private Boolean explorationStage(){
        int timeToExplore = fuel * 3;

        if (!checkFailure(10)){


            try {
                transmitReport();
                Thread.sleep(timeToExplore);

            } catch (InterruptedException e){

                System.out.println( name + " to" + destination + " has been interrupted");

            }

            stage = "exploration stage";
//            System.out.println( name + " to" + destination + " is now exploring");

            Components system_components = new Components();
            int fuel = components.get(name).get("Fuel");
            int newFuel = system_components.getFuel(fuel, dividedFuel);
            components.get(name).replace("Fuel", newFuel);
            System.out.println("component list new " + components);
            transmitReport();
            return false;
        }
        else{
            status = "failing";
            transmitReport();
            if (!errorCheck()){
                status = "recovered";
                transmitReport();
                return true;
            }
            return false;

        }
    }



    // Transmit status of the mission to mission control, sleep until reply from control
    public void transmitReport(){
        String report = String.format(
                "Mission: %s\n" + "Destination: %s\n" + "Stage: %s\n" + "Status: %s\n ",
                name,
                destination,
                stage,
                status);


        System.out.println(report);

    }

    public void killMission(){

        System.out.println( name + " failed");

        missionInProgress = false;

    }



    //Execute Mission Thread on call. Override the Run

    @Override
    public void run() {


        //System.out.println("Test Run");
        while (missionInProgress) {

            if (!boostStage() || !interplanetaryTransitStage() || !explorationStage()){

                killMission();
            }

        }



    }



}
