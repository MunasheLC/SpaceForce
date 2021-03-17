package com.company;


// Essentially a thread pool of the missions


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MissionController {


    // List of the missions
    private List<Mission> listOfMissions = new ArrayList<>();

    public MissionController(int numberOfMissions) {


        // For the number of missions to be monitored, create a mission
        for (int i = 0; i <= numberOfMissions; i++) {
            String name = "Mission" + i; // Mission number

            Component comps = new Component(); // Generate mission components

            String destination = comps.destination()[0]; //choose a destination?

            System.out.println("The destination for " + name + " is " + destination);

            Mission mission = new Mission(name, comps.fuel(), comps.thrusters(), comps.instruments(), comps.controlSystems(), comps.powerPlants(), destination);

            listOfMissions.add(mission);

        }

        for (Mission mission : listOfMissions){

            mission.start();

        }
    }

}
