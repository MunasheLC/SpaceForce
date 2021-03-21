package com.company;

import javax.naming.ldap.Control;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Mission extends Thread{

    public volatile boolean missionInProgress = true;

    int fuel;
    int dividedFuel;
    int dividedthrusters;
    int dividedpowerplants;
    int dividedCS;
    int dividedInstruments;

    int initialfuel;
    int initialthrusters;
    int initialpowerplants;
    int initialCS;
    int initialInstruments;

    HashMap<String, HashMap<String,Integer>> components;
    Double networkSpeed;
    String stage;
    String status;
    String name;
    String destination;
    MissionController controller;
    String startTime;
    String endTime;
    Report report;


    //Supplies allocated by mission controller based on destination planet
    public Mission ( HashMap components, String name, String destination, double networkSpeed, MissionController controller) {
        this.components = components;
        this.stage = "boost";
        this.name= name;
        this.destination = destination;
        this.status= "normal";
        this.networkSpeed = networkSpeed;
        this.controller = controller;
        this.startTime = "";
        this.endTime = "";





    }

    public void getStartTime(){

        this.startTime = Calendar.getInstance().getTime().toString();
//        System.out.println("Start ime is: " + startTime);

    }

    public String geTime(){
        String time = Calendar.getInstance().getTime().toString();
        return time;
//        System.out.println("Start ime is: " + startTime);

    }


    public void initialComponents(){
        initialfuel = components.get(name).get("Fuel");
        initialpowerplants = components.get(name).get("PowerPlants");
        initialInstruments = components.get(name).get("Instruments");
        initialthrusters = components.get(name).get("Thrusters");
        initialCS = components.get(name).get("ControlSystems");
    }


    //Calculate Failure Rate
    public boolean checkFailure(int failureRate) {

        int random = ThreadLocalRandom.current().nextInt(0, 100);
//        System.out.println("failure calc is: " + random);
        return random <= failureRate;
    }

    public boolean errorCheck() {
        boolean failchecker = true; //currently failing
        Network net = new Network();
        Random rand = new Random();

//
            // 25% of failiures are recoverable
            if (checkFailure(25)) {
                //Send message for SoftwareUpdate
                net.AddReportToControllerQ(name, networkSpeed, "SOFTWAREUPDATE", 5, controller);
                if (net.avalbility){
                    int US = net.updateSize;
                    initializeSoftwareUpdate(US);
                    failchecker = false;
                    return failchecker;
                }
            }

            //mission abort
            else {
                net.AddReportToControllerQ(name, networkSpeed, "TERMINATE", 5, controller);
                return failchecker;
            }
            return true;
        }


    HashMap<Object,Integer> divs = new HashMap<Object,Integer>();

    public void addKeyToMap(Object key2) {
        if (key2.equals("PowerPlants")) {
            divs.put(key2, initialpowerplants);
        }
        if (key2.equals("Instruments")) {
            divs.put(key2, initialInstruments);
        }
        if (key2.equals("Fuel")) {
            divs.put(key2, initialfuel);
        }
        if (key2.equals("Thrusters")) {
            divs.put(key2, initialthrusters);
        }
        if (key2.equals("ControlSystems")) {
            divs.put(key2, initialCS);
        }
    }
    public void sleep(int updateDevLength, int speed){
        try {
            Thread.sleep(updateDevLength * speed);
        }
        catch (InterruptedException e){

            System.out.println("Update Development has been interrupted");

        }
    }
    public void initializeSoftwareUpdate(int updateSize){
        Network net = new Network();
        if (networkSpeed == 2.0) {
            sleep(updateSize, 200);
            net.AddReportToControllerQ(name, networkSpeed, "UPDATESUCCESS", 5, controller);
        }

        if(networkSpeed == 0.2) {
            sleep(updateSize, 400);
            net.AddReportToControllerQ(name, networkSpeed, "UPDATESUCCESS", 5, controller);
        }

        if (networkSpeed == 0.02) {
            sleep(updateSize, 600);
            net.AddReportToControllerQ(name, networkSpeed, "UPDATESUCCESS", 5, controller);
        }
    }

    public void componentReport(){
        for (String Key1 : components.keySet()){
            HashMap innerMap = components.get(Key1);
            for (Object key2 : innerMap.keySet()) {
                Network net = new Network();
                addKeyToMap(key2);

                int initial = divs.get(key2);
                String message = (key2 + " is @ " + innerMap.get(key2) + "/" + initial);
                net.AddComponentReportToControllerQ(name, networkSpeed, key2, message, controller);
            }
        }
    }


    public String missionInitilize(){

        String missionStatus;
        missionStatus = "The destination for " + name + " is " + destination;
        initialComponents();

        return missionStatus;
    }


    // Boost is instant, run first
    private Boolean boostStage(){
        System.out.println("Components before use: " + components);
        String s = "Components before use: " + components;
        controller.mainWriter(s);

        System.out.println("");
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

            componentReport();
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
                System.out.println("");
                transmitReport();
                Thread.sleep(distance);

            } catch (InterruptedException e){

                System.out.println( name + " to" + destination + " has been interrupted");

            }

            stage = "exploration stage";
//            System.out.println( name + " to" + destination + " is entering exploration stage");

            decrement();
//            transmitReport();
            componentReport();
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
                System.out.println("");
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
            status = "complete";
//            System.out.println( name + " to" + destination + " is now exploring");

            decrement();
            componentReport();
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
        endTime = geTime();



        if (status.equals("failing")){

            status = "failed";
            System.out.println(name + " has FAILED! \n" +
                    "End Time: " + endTime);

        }
        else{
            System.out.println(name + " was Successful!\n" +
                    "End Time: " + endTime);
        }

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
