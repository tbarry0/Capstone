import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class userInterface {
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        String myFile = "devices.txt";
        File file = new File(myFile);
        List<homeSystem> devices = new ArrayList<>();

        if(file.exists()){
            try(BufferedReader buffer = new BufferedReader(new FileReader(file))){
                String line;
                while ((line = buffer.readLine()) != null){
                    String[] data = line.split(",");
                    if(data.length == 3){
                        String type = data[0].trim();
                        String location = data[1].trim();
                        homeSystem device;

                        if(type.equalsIgnoreCase("thermostat")){
                            int defualtTemp = Integer.parseInt(data[2].trim());
                            device = new thermostat(type, location, defualtTemp);
                        }else{
                            device = new homeSystem(type, location);
                        }
                        devices.add(device);
                    }
                }
            } catch (IOException e) {
                System.out.println("File not found" + e);
            }
            devices.sort(new deviceLocationSorter());

            for(homeSystem device : devices){
                device.displayInfo();
            }
            System.out.println("Enter device name to set a timer or turn off a timer: ");
            while (true) {
                String deviceName = scnr.nextLine();
                System.out.println("Enter delay in milliseconds: ");
                long delay = scnr.nextLong();
                System.out.println("Trun on or off?");
                boolean turnOn = scnr.nextBoolean();
                homeSystem device = smartHomeSystem.getDeviceByName(deviceName);
                if(device != null){
                    smartHomeSystem.getSchedule.setTimer(delay, device, turnOn);
                    System.out.println(device + "scheduled to turn " + (turnOn ? "ON" : "OFF") + " in " + delay + " ms");
                }else{
                    System.out.println("Device not found.");
                }

            }
        }
    }
}

