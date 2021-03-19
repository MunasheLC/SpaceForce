import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Mission extends Thread{

    private volatile boolean missionInProgress = true;

    int fuel;
    int thrusters;
    int intruments;
    int controlSystem;
    int powerPlant;
    Double networkSpeed;
    String stage;
    String status;
    String name;
    String destination;



    //Supplies allocated by mission controller based on destination planet
    public Mission (String name, int fuel, int thrusters, int instruments, int controlSystem, int powerPlant, String destination, double networkSpeed ) {
        this.fuel = fuel;
        this.thrusters = thrusters;
        this.intruments = instruments;
        this.controlSystem = controlSystem;
        this.powerPlant = powerPlant;
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

    public boolean errorCheck(){
        boolean failchecker = true; //currently failing
        Random rand = new Random();
        int failureProbability = rand.nextInt(9-5) + 5;
        System.out.println("Checking for Software Update for " + name + " at stage: " + stage);
        if (failureProbability > 5){
            System.out.println("Update available for " + name + " at stage: " + stage);
            Network nt = new Network();
            System.out.println("Developing Software Update for " + name + " at stage " + stage);
            int updateDevLength = nt.developSoftwareUpdate();
            try {
                Thread.sleep(updateDevLength);
            }
            catch (InterruptedException e){

                System.out.println("Update Development has been interrupted");

            }

            System.out.println("Updating..");
            int update = nt.initializeSoftwareUpdate();
            try {
                Thread.sleep(update);
            }
            catch (InterruptedException e){

                System.out.println("Update has been interrupted");

            }
            System.out.println("Update complete for " + name + " at stage " + stage);
            System.out.println(name +" at "+ stage + " recovered ");
            System.out.println();
            failchecker = false; //not failing
            return failchecker;
        }

        else{
            System.out.println("No update for " + name + " at stage: " + stage);
            System.out.println("Systems failed");
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

        if (!checkFailure(10)){
            try {

                Thread.sleep(100);

            } catch (InterruptedException e){

                System.out.println( name + " to" + destination + " has been interrupted");

            }

            stage = "interplanetary transit stage";
//            System.out.println( name + " to" + destination + " is entering interplanetary transit stage");
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
                Thread.sleep(distance);

            } catch (InterruptedException e){

                System.out.println( name + " to" + destination + " has been interrupted");

            }

            stage = "exploration stage";
//            System.out.println( name + " to" + destination + " is entering exploration stage");

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
//        else {
//            status = "failing";
//            transmitReport();
//            return false;
//
//        }


    }


    // Variable time to complete based on planet size
    private Boolean explorationStage(){
        int timeToExplore = fuel * 3;

        if (!checkFailure(10)){


            try {

                Thread.sleep(timeToExplore);

            } catch (InterruptedException e){

                System.out.println( name + " to" + destination + " has been interrupted");

            }

            stage = "exploration stage";
//            System.out.println( name + " to" + destination + " is now exploring");
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
