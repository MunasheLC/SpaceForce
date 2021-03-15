package com.company;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Mission extends Thread{

    int fuel;
    int thrusters;
    int intruments;
    int controlSystem;
    int powerPlant;
    String stage;
    String name;


    //Supplies allocated by mission controller based on destination planet
    public Mission (String name, int fuel, int thrusters, int instruments, int controlSystem, int powerPlant, String destination ) {
        this.fuel = fuel;
        this.thrusters = thrusters;
        this.intruments = instruments;
        this.controlSystem = controlSystem;
        this.powerPlant = powerPlant;
        this.stage = "";
        this.name= name;

    }


    //Calculate Failure Rate
    public boolean calculateFailure(int failureRate) {

        int random = ThreadLocalRandom.current().nextInt(0, 100);
        return random <= failureRate;
    }



    public void failureCheck(){

        boolean failure = false;

        // possibly not necessary because this stuff needs to go in the report anyway. More for us testing really
        System.out.println(this.name + "is checking the status of the mission");


    }

    // Method to calculate distance
    private int calculateDistance() {

        return 0;
    }

    // Boost is instant, run first
    public void boostStage(){

    }

    // Variable time to complete based on the amount of fuel and the destination
    public void interplanetaryTransitStage(){


    }




    // Variable time to complete based on planet size
    public void explorationStage(){


    }

    // Transmit status of the mission to mission control, sleep until reply from control
    public void transmitReport(){


    }

    //Execute Mission Thread on call. Override the Run

    @Override
    public void run(){

        //Do something

    }



}
