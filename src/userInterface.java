import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class userInterface {
    private static final String devices_file = "devices.txt";
    private static smartHomeSystem smartHome;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        smartHome = new smartHomeSystem();

        if (loadDevicesFromFile()) {
            displayAllDevices();
            //create a command loop
        } else {
            System.out.println("No devices loaded exiting.");
        }
    }

    private static boolean loadDevicesFromFile(){
        File file = new File(devices_file);
        List<homeSystem> devices = new ArrayList<>();
        if(!file.exists()){
           System.out.println("Device file not found");
           return false;
        }
        try(BufferedReader buffer = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = buffer.readLine()) != null){
                String[] data = line.split(",");
                if(data.length >= 3){
                    String type = data[0].trim();
                    String location = data[1].trim();
                    if (type.equalsIgnoreCase("thermostat")) {
                        try {
                            int defaultTemp = Integer.parseInt(data[2].trim());
                            thermostat thermostat = new thermostat(type, location, defaultTemp);
                            smartHome.addTheromostat(thermostat);
                            System.out.println("Added thermostat: " + type + " in " + location);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid temperature format for thermostat: " + data[2]);
                        }
                    } else {
                        homeSystem device = new homeSystem(type, location);
                        smartHome.addDevice(device);
                        System.out.println("Added device: " + type + " in " + location);
                    }
                } else {
                    System.out.println("Invalid device data format: " + line);
                }
            }

            smartHome.sortDevicesByLocation();
            return true;

        } catch (IOException e) {
            System.out.println("Error reading device file: " + e.getMessage());
            return false;
        }
    }

    public static void displayAllDevices(){
        System.out.println("Current devices: ");
        smartHome.displayDevices();
    }

    public static void controlDevice(){
        System.out.println("Enter device name");
        String deviceName = scanner.nextLine().trim();

        homeSystem device = smartHomeSystem.getDeviceByName(deviceName);
        if(device == null){
            System.out.println("Device not found: " + deviceName);
            return;
        }

        System.out.print("Turn on? (yes/no): ");
        String userInput = scanner.nextLine().trim().toLowerCase();
        boolean turnOn = userInput.equals("yes") || userInput.equals("no");

        if(smartHomeSystem.controlDevice(deviceName, turnOn)){
            System.out.println("Device " + deviceName + " is now " + (turnOn ? "ON" : "OFF"));
        }else{
            System.out.println("Unable to control device: " + deviceName);
        }
    }

    public static void scheduleDevice(){
        System.out.println("Enter device name: ");
        String deviceName = scanner.nextLine().trim();

        homeSystem device = smartHomeSystem.getDeviceByName(deviceName);
        if(device == null){
            System.out.println("Device not found: " + deviceName);
            return;
        }

        System.out.print("Enter delay in milliseconds");
        long delay;
        try{
            delay = Long.parseLong(scanner.nextLine().trim());
        }catch(NumberFormatException e){
            System.out.println("Invalid delay. Please enter a number.");
            return;
        }

        System.out.print("Turn on? (yes/no): ");
        String userInput = scanner.nextLine().trim().toLowerCase();
        boolean turnOn = userInput.equals("yes") || userInput.equals("no");

        schedule schedule = smartHome.getScedule();
        schedule.setTimer(delay, device, turnOn);

        System.out.println("Device " + deviceName + " scheduled to turn " + (turnOn ? "ON" : "OFF") + " in " + delay + "ms");
    }
}

