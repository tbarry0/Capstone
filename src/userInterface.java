import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class userInterface {
    private static final String DEVICES_FILE = "src/devices.txt";
    private static smartHomeSystem smartHome;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        smartHome = new smartHomeSystem();

        if (loadDevicesFromFile()) {
            displayAllDevices();
            //create a command loop
            commandLoop();
        } else {
            System.out.println("No devices loaded exiting.");
        }
    }
    //loads the devices from the file and prints them
    private static boolean loadDevicesFromFile() {
        File file = new File(DEVICES_FILE);
        if (!file.exists()) {
            System.out.println("Device file not found");
            return false;
        }
        try (BufferedReader buffer = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2) {
                    String type = data[0].trim();
                    String location = data[1].trim();

                    if (type.equalsIgnoreCase("thermostat") && data.length >= 3) {
                        try {
                            int defaultTemp = Integer.parseInt(data[2].trim());
                            thermostat thermostat = new thermostat(type, location, defaultTemp);
                            smartHome.addTheromostat(thermostat);
                            System.out.println("Added thermostat: " + type + " in " + location);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid temperature format for thermostat: " + data[2]);
                        }
                    } else if (data.length >= 2) {
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
    //prints all devices at the beginning
    public static void displayAllDevices(){
        System.out.println("Current devices: ");
        smartHome.displayDevices();
    }
    //method that allows user to change state of devices
    public static void controlDevice() {
        System.out.println("Enter device name");
        String deviceName = scanner.nextLine().trim();

        homeSystem device = smartHomeSystem.getDeviceByName(deviceName);
        if (device == null) {
            System.out.println("Device not found: " + deviceName);
            return;
        }

        System.out.print("Turn on? (yes/no): ");
        String userInput = scanner.nextLine().trim().toLowerCase();
        boolean turnOn = userInput.equals("yes"); // Only checks "yes" to turn on, "no" to turn off

        if (smartHomeSystem.controlDevice(deviceName, turnOn)) {
            System.out.println("Device " + deviceName + " is now " + (turnOn ? "ON" : "OFF"));
        } else {
            System.out.println("Unable to control device: " + deviceName);
        }
    }
    //method to schedule the timers on devices
    public static void scheduleDevice() {
        System.out.println("Enter device name: ");
        String deviceName = scanner.nextLine().trim();

        homeSystem device = smartHomeSystem.getDeviceByName(deviceName);
        if (device == null) {
            System.out.println("Device not found: " + deviceName);
            return;
        }

        System.out.print("Enter delay in milliseconds: ");
        long delay;
        try {
            delay = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid delay. Please enter a valid number.");
            return;
        }

        System.out.print("Turn on? (yes/no): ");
        String userInput = scanner.nextLine().trim().toLowerCase();
        boolean turnOn = userInput.equals("yes"); // Only checks "yes" to turn on

        schedule schedule = smartHome.getScedule();
        schedule.setTimer(delay, device, turnOn);

        System.out.println("Device " + deviceName + " scheduled to turn " + (turnOn ? "ON" : "OFF") + " in " + delay + "ms");
    }
    //commands that allow the user to acces the whole interface
    private static void commandLoop(){
        System.out.println("Smart Home System Command Interface");
        System.out.println("Type 'list' for commands or 'exit' to quit: ");

        boolean running = true;
        while(running){
            System.out.println("\nEnter Command: ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch(command) {
                case "list":
                    displayAllDevices(); // displayes all devices in the list
                    break;
                case "control":
                    controlDevice(); //user can turn device on or off
                    break;
                case "schedule":
                    scheduleDevice(); //user can create schedule to turn devices on or off
                    break;
                case "states":
                    smartHome.displayDeviceStatesUsingStacks(); //stack command so the user can see what devices are on or off
                    break;
                case "help":
                    displayHelp(); //help command to inform the user what other commands there are and what they do
                case "exit": //quits the coomand loop
                    System.out.println("Goodbye, Exiting Smart Home System");
                    running = false;
                    break;
                default:
                    System.out.println("Unknown command. Type 'help' to access commands.");
            }
        }
    }
    //method to inform the user on what each command does
    private static void displayHelp(){
        System.out.println("Commands:");
        System.out.println("help (h): displays this message");
        System.out.println("list (l): List all the devices");
        System.out.println("control (c): Controls a device(turning it on or off");
        System.out.println("schedule (s): Sets a schedule/timer to turn a device on or off");
        System.out.println("states (st): Shows lists of ON and OFF devices using stacks");
        System.out.println("exit (e): Quits the program");
    }
}



