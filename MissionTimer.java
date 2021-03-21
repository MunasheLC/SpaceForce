package com.company;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MissionTimer extends TimerTask {
//    private final Timer timer;
//    private final Random random;

    Mission mission;
    String startTime;

    MissionTimer (Mission mission){

//        this.timer = timer;
//        this.random = random;
        this.mission = mission;




    }


    public void run(){
        mission.getStartTime();
        mission.start();
        System.out.println( mission.name + " Started at: " + mission.startTime);


    }

}
