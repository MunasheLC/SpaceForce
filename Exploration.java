import java.util.List;
import java.util.Random;
import java.util.*;

public class Exploration{
    Random rand = new Random();
    String[] findings = new String[]{"Water","Rocks", "Metal", "Alien"};

    public void getFinding(String name){
        int randomIndex = rand.nextInt(findings.length);
        String find = findings[randomIndex];
        if ( find == "Water"){
            System.out.println(name + ": Rover identified Water");
        }
        if ( find == "Rocks"){
            System.out.println(name + ": Rover identified Rock");
        }
        if (find == "Metal"){
            System.out.println(name + ": Rover identified Metal");
        }
        else{
            System.out.println(name + ": Rover identified Unknown Species");
        }


    }
}