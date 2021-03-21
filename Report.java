import java.util.*;
public class Report{


    HashMap<String, String> messages = new HashMap<>();

    public void addToMap(){
        messages.put("SOFTWAREUPDATE"," URGENT - Software Update needed.");
        messages.put("UPDATESUCCESS"," SUCCESS - Software Update Success.");
        messages.put("TERMINATE", " URGENT - Terminate mission.");
        messages.put("FAIL", " WARNING - Failing.");
        messages.put("TELEMETRY", "tel");
        messages.put("DATA", "data");

    }
    public String Report(String messageType, int messageSize){
        addToMap();
        String messageContext = messages.get(messageType);
        return messageContext;

        }


}