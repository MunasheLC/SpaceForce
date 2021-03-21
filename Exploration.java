import java.util.List;
import java.util.Random;
import java.util.*;

public class Exploration{
    Random rand = new Random();
    String[] findings = new String[]{"Water","Rocks", "Metal", "Alien"};

    public void getFinding(String name, MissionController controller){
        int randomIndex = rand.nextInt(findings.length);
        String find = findings[randomIndex];
        if ( find == "Water"){
            controller.mainWriter(name + ": Rover identified Water");
        }
        if ( find == "Rocks"){
            controller.mainWriter(name + ": Rover identified Rock");
        }
        if (find == "Metal"){
            controller.mainWriter(name + ": Rover identified Metal");
        }
        else{
            controller.mainWriter(name + ": Rover identified Unknown Species");
        }


    }
}