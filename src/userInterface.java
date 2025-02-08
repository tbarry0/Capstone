import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class userInterface {
    public static void main(String[] args) {
        String myFile = "devices.txt";
        File file = new File(myFile);
        List<homeSystem> devices = new ArrayList<>();

        if(file.exists()){
            try(BufferedReader buffer = new BufferedReader(new FileReader(file))){
                String line;
                while ((line = buffer.readLine()) != null){
                    String[] data = line.split(",");
                    if(data.length == 2){
                        String type = data[0].trim();
                        String location = data[1].trim();
                        homeSystem device = new homeSystem(type, location);
                        devices.add(device);
                    }
                }
            } catch (IOException e) {
                System.out.println("File not found" + e);
            }
            for(homeSystem device : devices){
                device.displayInfo();
            }
        }
    }
}
