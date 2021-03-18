package com.company;


// Essentially a thread pool of the missions


import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;


public class MissionController {

    // Request Report
    public void requestReport(){



    }


    Network nt = new Network();

    // List of the missions

    BlockingQueue netQ = nt.waiting_Queue;
    private List<Mission> listOfMissions = new ArrayList<>();
    private boolean noMoreMissions = false;






    public MissionController(int numberOfMissions) {

        // For the number of missions to be monitored, create a mission
        for (int i = 0; i <= numberOfMissions; i++) {
            String name = "Mission-" + i; // Mission number

            Components comps = new Components(); // Generate mission components

            String destination = comps.destination()[0]; //choose a destination?

//            System.out.println("The destination for " + name + " is " + destination);

            Mission mission = new Mission(name, comps.fuel(), comps.thrusters(), comps.instruments(), comps.controlSystems(), comps.powerPlants(), destination);

            mission.networkSpeed = nt.getSpeed();

            System.out.println(mission.missionInitilize());


            listOfMissions.add(mission);

        }

        for (Mission mission : listOfMissions) {

            mission.start();

        }
    }

    public synchronized  void stop(){

        noMoreMissions = true;
        for (Mission mission : listOfMissions){

            mission.killMission();
        }

    }



    // Could make changes here if all the threads aren't using the network then nothing is happening
    public synchronized void waitForAllMissionsToEnd(){

        for (Mission mission : listOfMissions){

            if (mission.status.equals("failed") || mission.status.equals("complete")){

                try{

                    Thread.sleep(1);
                }
                catch (InterruptedException e){

                    e.printStackTrace();
                }

            }

        }

    }

}
