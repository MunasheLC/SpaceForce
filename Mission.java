package com.company;

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
    public Mission (String name, int fuel, int thrusters, int instruments, int controlSystem, int powerPlant, String destination ) {
        this.fuel = fuel;
        this.thrusters = thrusters;
        this.intruments = instruments;
        this.controlSystem = controlSystem;
        this.powerPlant = powerPlant;
        this.stage = "boost";
        this.name= name;
        this.destination = destination;
        this.status= "normal";
        this.networkSpeed = 0.0;

    }


    //Calculate Failure Rate
    public boolean checkFailure(int failureRate) {

        int random = ThreadLocalRandom.current().nextInt(0, 100);
//        System.out.println("failure calc is: " + random);
        return random <= failureRate;
    }

    public String missionInitilize(){

        String missionStatus;
        missionStatus = "The destination for " + name + " is " + destination;

        return missionStatus;
    }

//    public void failureCheck(){
//
//        boolean failure = false;
//
//
//        // possibly not necessary because this stuff needs to go in the report anyway. More for us testing really
//        System.out.println(this.name + "is checking the status of the mission");
//
//
//    }

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
        else {
            status = "failing";
            transmitReport();
            return false;

        }


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
