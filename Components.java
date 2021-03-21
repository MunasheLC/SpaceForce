import java.util.List;
import java.util.Random;
import java.util.*;

public class Components{
    Random rand = new Random();

    public String[] destination(){
        //linked list
        Map<String, Integer> destinations= new HashMap<>();
        destinations.put("Mars", 600); //destination and needed fuel
        destinations.put("Moon", 300);
        destinations.put("Venus", 500);

        List<String> destinationList = new ArrayList<>(destinations.keySet());
        int randomIndex = new Random().nextInt(destinationList.size());
        String randomKey = destinationList.get(randomIndex);

        String array[] = new String[2];
        array[0] = (randomKey);
        array[1] = Integer.toString(destinations.get(randomKey));

        return array;
    }
    public int getRandomNumber(int max, int min){
        //random number between min 200 and max 900
        return (rand.nextInt(max-min) + min);
    }
    public int fuel(){
        return getRandomNumber(900,200);
//        return (rand.nextInt(900-200) + 200);
    }

    public int thrusters(){
        return getRandomNumber(12,4);
    }

    public int instruments(){
        return getRandomNumber(18,9);
    }
    public int controlSystems(){
        return getRandomNumber(6,3);
    }
    public int powerPlants(){
        return getRandomNumber(9,4);
    }

    public int decrementComponent(int component, int divideNum){
        int decremented = component - divideNum;
        return decremented;
    }

}