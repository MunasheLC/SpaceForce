import javax.naming.ldap.Control;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Mission extends Thread{

    private volatile boolean missionInProgress = true;

    int fuel;
    int dividedFuel;
    int dividedthrusters;
    int dividedpowerplants;
    int dividedCS;
    int dividedInstruments;
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
        return true;
    }


    public String missionInitilize(){

        String missionStatus;
        missionStatus = "The destination for " + name + " is " + destination;

        return missionStatus;
    }


    // Boost is instant, run first
    private Boolean boostStage(){
        System.out.println("Components before use: " + components);
        if (!checkFailure(10)){
            try {
                transmitReport();
                Thread.sleep(500);

            } catch (InterruptedException e){

                System.out.println( name + " to" + destination + " has been interrupted");

            }

            stage = "interplanetary transit stage";

            divideComponent();

            decrement();

            System.out.println("Components used: " + components);
//            transmitReport();
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

            decrement();
            System.out.println("Components used: " + components);
//            transmitReport();
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
                Random rand = new Random();
                int findProbability = rand.nextInt(6-5) + 5;
                if (findProbability == 5) {
                    Exploration ex = new Exploration();
                    ex.getFinding(name);
                }
                Thread.sleep(timeToExplore);

            } catch (InterruptedException e){

                System.out.println( name + " to" + destination + " has been interrupted");

            }

            stage = "exploration stage";
//            System.out.println( name + " to" + destination + " is now exploring");

            decrement();
            System.out.println("Components used: " + components);
//            transmitReport();
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
                "%s: " + "Destination: %s, " + "Stage: %s, " + "Status: %s ",
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

    public int divideCalc(String comp){
        int component = components.get(name).get(comp);
        int tmp = (component/3);
        return tmp;

    }
    public void divideComponent() {
        dividedFuel = divideCalc("Fuel");
        dividedthrusters = divideCalc("Thrusters");
        dividedCS = divideCalc("ControlSystems");
        dividedInstruments = divideCalc("Instruments");
        dividedpowerplants = divideCalc("PowerPlants");
    }


    public void decrementComponent(String comp, int dividedcomp){
        int component = components.get(name).get(comp);
        Components system_components = new Components();
        int decrementedComponent = system_components.decrementComponent(component, dividedcomp);
        components.get(name).replace(comp, decrementedComponent);
    }
    public void decrement() {
        decrementComponent("Fuel", dividedFuel);
        decrementComponent("Thrusters", dividedthrusters);
        decrementComponent("ControlSystems", dividedCS);
        decrementComponent("Instruments", dividedInstruments);
        decrementComponent("PowerPlants", dividedpowerplants);
    }


    // Method to calculate distance
    private int calculateDistance() {
        return fuel * 10;
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
