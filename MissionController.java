import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

public class MissionController extends  Thread { //wants to put asa timer task, to constantly request information.
    String SOFTWAREUPDATE = "URGENT - Software Update needed.";
    String UPDATESUCCESS = "SUCCESS - Software Update Success.";
    String TERMINATE = "URGENT - Terminate mission.";
    String FAIL = "WARNING - Failing.";
    boolean running = true;
    Timer timer = new Timer();
    Network nt = new Network();
    static BufferedWriter writer;

    // List of the missions

    private List<Mission> listOfMissions = new ArrayList<>();
    private boolean noMoreMissions = false;
    HashMap<String, HashMap<String, Integer>> map_components = new HashMap<String, HashMap<String, Integer>>();


    public MissionController(int numberOfMissions) {



        // For the number of missions to be monitored, create a mission
        for (int i = 0; i <= numberOfMissions; i++) {
            String name = "Mission-" + i; // Mission number

            Components comps = new Components(); // Generate mission components

            String destination = comps.destination()[0]; //choose a destination?
            double networkSpeed = nt.getSpeed();

//            controller.mainWriter("The destination for " + name + " is " + destination);
            map_components.put(name, new HashMap<String, Integer>());
            map_components.get(name).put("Fuel", comps.fuel());
            map_components.get(name).put("Thrusters", comps.thrusters());
            map_components.get(name).put("Instruments", comps.instruments());
            map_components.get(name).put("ControlSystems", comps.controlSystems());
            map_components.get(name).put("PowerPlants", comps.powerPlants());

            Mission mission = new Mission(map_components, name, destination, networkSpeed, this);

            mainWriter(mission.missionInitilize());


            listOfMissions.add(mission);

        }


    }

    public void mainWriter(String s) {
        try {
            writer.append(s + "\n");
        } catch (Exception e) {

            System.out.println("");

        }
    }



    public synchronized void killController() {

        noMoreMissions = true;
        timer.cancel();

    }

    public synchronized void sendSoftwareUpdate(Network nt, int updateSize, double networkSpeed, String missionName, String time) {
        nt.SoftwareUpdateAvailable(updateSize);
        String message = ("Software update Available for " + missionName);
        nt.SendReportToMission("Mission Controller ", networkSpeed, message, time);

    }
    public String geTime(){
        String time = Calendar.getInstance().getTime().toString();
        return time;
//        controller.mainWriter("Start ime is: " + startTime);

    }

    public synchronized void recieveReport(String missionName, String message, Network nt, double networkSpeed) {
        if (message == SOFTWAREUPDATE) {
            mainWriter("Developing Software Update..");
            int updateSize = nt.developSoftwareUpdate();
            String time = geTime();
            sendSoftwareUpdate(nt, updateSize, networkSpeed, missionName,time);
        }

    }

    public synchronized void waitForAllMissionsToEnd() {

        listOfMissions.removeIf(mission -> !mission.missionInProgress);
        if (listOfMissions.isEmpty()){

            killController();
        }
    }


    @Override
    public void run () {

        try
        {
            writer = new BufferedWriter(new FileWriter("output.dat"));
        }
        catch (Exception e)
        {
            mainWriter("Something went wrong");
        }

        // For the number of missions to be monitored, create a mission
        for (Mission mission : listOfMissions) {

            Random random = new Random();
            timer.schedule(new MissionTimer(mission, this), random.nextInt(10000));

        }

        while (!noMoreMissions){

            waitForAllMissionsToEnd();
        }

        try {
            writer.close();

        }
        catch (Exception e){

            mainWriter("");
        }










    }

}


