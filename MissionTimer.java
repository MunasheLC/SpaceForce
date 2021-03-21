import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MissionTimer extends TimerTask {
//    private final Timer timer;
//    private final Random random;

    Mission mission;
    String startTime;
    MissionController controller;

    MissionTimer (Mission mission, MissionController control){

//        this.timer = timer;
//        this.random = random;
        this.mission = mission;
        this.controller = control;




    }


    public void run(){
        mission.getStartTime();
        mission.start();
        controller.mainWriter( mission.name + " Started at: " + mission.startTime);


    }

}
