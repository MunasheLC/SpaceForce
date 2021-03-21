
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;


public class MissionController { //wants to put asa timer task, to constantly request information.
String SOFTWAREUPDATE = "need Software Update";
String UPDATESUCCESS = "Software Update Success";
String TERMINATE = "Terminate mission";
String FAIL = "Failure";

    Network nt = new Network();

    // List of the missions

    private List<Mission> listOfMissions = new ArrayList<>();
    private boolean noMoreMissions = false;
    HashMap<String, HashMap<String,Integer>> map_components = new HashMap<String, HashMap<String,Integer>>();



    public MissionController(int numberOfMissions) {

        // For the number of missions to be monitored, create a mission
        for (int i = 0; i <= numberOfMissions; i++) {
            String name = "Mission-" + i; // Mission number

            Components comps = new Components(); // Generate mission components

            String destination = comps.destination()[0]; //choose a destination?
            double networkSpeed = nt.getSpeed();

//            System.out.println("The destination for " + name + " is " + destination);
            map_components.put(name, new HashMap<String,Integer>());
            map_components.get(name).put("Fuel",comps.fuel());
            map_components.get(name).put("Thrusters",comps.thrusters());
            map_components.get(name).put("Instruments",comps.instruments());
            map_components.get(name).put("ControlSystems",comps.controlSystems());
            map_components.get(name).put("PowerPlants",comps.powerPlants());

            Mission mission = new Mission(map_components, name, destination, networkSpeed);

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

    public synchronized void recieveReport(String message){
        if (message == SOFTWAREUPDATE) {
            System.out.println("developing Software Update..");
//            SendSoftwareUpdate();
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
