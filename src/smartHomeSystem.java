import java.util.*;
public class smartHomeSystem<T extends homeSystem>{
    private static GenericLinkedList<homeSystem> homeSystems = new GenericLinkedList<>();
    private thermostat thermostat;
    private schedule schedule;
    private GenericStack<String> commandHistory;
    private GenericStack<T> recentDevices;

    public smartHomeSystem(){
        schedule = new schedule();
        commandHistory = new GenericStack<>();
        recentDevices = new GenericStack<>();
    }
    //recursive method that controls the devices
    public static void recusiveDeviceControl(List<homeSystem> devices, boolean turnOn) {
        recursiveDeviceControlHelper(devices, 0, turnOn);
    }

    private static void recursiveDeviceControlHelper(List<homeSystem> devices, int index, boolean turnOn) {
        if (devices.isEmpty()) {
            System.out.println("No devices");
            return;
        }

        if (index >= devices.size()) {
            return;
        }

        homeSystem currentDevice = devices.get(index);
        if (turnOn) {
            currentDevice.turnOn();
        } else {
            currentDevice.turnOff();
        }

        // Recursive call to handle the next device
        recursiveDeviceControlHelper(devices, index + 1, turnOn);
    }

    public static List<homeSystem> recursiveSortByLocation(List<homeSystem> devices) {
        if (devices.size() <= 1) {
            return devices;
        }

        int mid = devices.size() / 2;
        List<homeSystem> left = recursiveSortByLocation(devices.subList(0, mid));
        List<homeSystem> right = recursiveSortByLocation(devices.subList(mid, devices.size()));

        return merge(left, right);
    }

    private static List<homeSystem> merge(List<homeSystem> left, List<homeSystem> right) {
        List<homeSystem> result = new ArrayList<>();
        int leftIndex = 0, rightIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).getLocation().compareToIgnoreCase(right.get(rightIndex).getLocation()) <= 0) {
                result.add(left.get(leftIndex));
                leftIndex++;
            } else {
                result.add(right.get(rightIndex));
                rightIndex++;
            }
        }

        // Add remaining elements
        while (leftIndex < left.size()) {
            result.add(left.get(leftIndex));
            leftIndex++;
        }

        while (rightIndex < right.size()) {
            result.add(right.get(rightIndex));
            rightIndex++;
        }

        return result;
    }

    public void addCommandToHistory(String command){
        commandHistory.push(command);
    }

    public String getLastCommand(){
        if(!commandHistory.isEmpty()){
            return commandHistory.peek();
        }
        return "No commands in History";
    }

    //returns schedule
    public schedule getScedule(){
        return schedule;
    }
    //method to add devices
    public void addDevice(homeSystem homeSystem) {
        homeSystems.add(homeSystem);
    }
    //method to add thermostat
    public void addTheromostat(thermostat thermostat){
        this.thermostat = thermostat;
        addDevice(thermostat);
    }
    //method to return the device names
    public static homeSystem getDeviceByName(String deviceName){
        List<homeSystem> devices = getAllDevices();
        for(homeSystem device : devices){
            if(device.getDevice().equalsIgnoreCase(deviceName)){
                return device;
            }
        }
        return null;
    }

    public static homeSystem getDeviceByNameAndLocation(String deviceName, String location) {
        List<homeSystem> devices = getAllDevices();
        for (homeSystem device : devices) {
            if (device.getDevice().equalsIgnoreCase(deviceName) &&
                    device.getLocation().equalsIgnoreCase(location)) {
                return device;
            }
        }
        return null;
    }

    //method to conrtol the state of the devices
    public static boolean controlDevice(String deviceName, boolean turnOn){
        boolean deviceFound = false;
        List<homeSystem> devices = getAllDevices();
        for(homeSystem device : devices){
            if(device.getDevice().equalsIgnoreCase(deviceName)){
                if(turnOn){
                    device.turnOn();
                }else{
                    device.turnOff();
                }
                deviceFound = true;
                break;
            }
        }
        return deviceFound;
    }
    //create an array list of the devices
    public static List<homeSystem> getAllDevices() {
        return homeSystems.toList();
    }

    public thermostat getThermostat() {
        return thermostat;
    }

    public T getMostRecentDevice() {
        if (!recentDevices.isEmpty()) {
            return recentDevices.peek();
        }
        return null;
    }

    //sorts devices based off location
    public void sortDevicesByLocation() {
        List<homeSystem> devices = getAllDevices();
        List<homeSystem> sortedDevices = recursiveSortByLocation(devices);

        // Clear and rebuild the linked list
        homeSystems = new GenericLinkedList<>();
        for (homeSystem device : sortedDevices) {
            homeSystems.add(device);
        }
    }

    //display the devices and info
    public void displayDevices() {
        List<homeSystem> devices = getAllDevices();
        if (devices.isEmpty()) {
            System.out.println("No available devices.");
        } else {
            for (homeSystem device : devices) {
                device.displayInfo();
                System.out.println();
            }
        }
    }

    public void displayDeviceStatesUsingStacks() {
        GenericStack<Object> onDevicesStack = new GenericStack<>();
        GenericStack<Object> offDevicesStack = new GenericStack<>();

        for (homeSystem device : getAllDevices()) {
            if (device.getStatus()) {
                onDevicesStack.push(device);
            } else {
                offDevicesStack.push(device);
            }
        }

        System.out.println("=== ON Devices ===");
        while (!onDevicesStack.isEmpty()) {
            homeSystem d = (homeSystem) onDevicesStack.pop();
            System.out.println("- " + d.getDevice() + " in " + d.getLocation());
        }

        System.out.println("=== OFF Devices ===");
        while (!offDevicesStack.isEmpty()) {
            homeSystem d = (homeSystem) offDevicesStack.pop();
            System.out.println("- " + d.getDevice() + " in " + d.getLocation());
        }
    }

    public static String formatTime(long milliseconds) {
        if (milliseconds < 1000) {
            return milliseconds + " milliseconds";
        } else if (milliseconds < 60000) {
            return (milliseconds / 1000) + " seconds";
        } else if (milliseconds < 3600000) {
            return (milliseconds / 60000) + " minutes";
        } else if (milliseconds < 86400000) {
            return (milliseconds / 3600000) + " hours";
        } else {
            return (milliseconds / 86400000) + " days";
        }
    }

    public void addThermostat(T t) {
    }
}
