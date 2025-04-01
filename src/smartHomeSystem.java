import java.util.*;
public class smartHomeSystem {
    public static schedule getSchedule;
    private static List<homeSystem> homeSystems = new ArrayList<>();
    private thermostat thermostat;
    private schedule schedule;

    public smartHomeSystem(){
        schedule = new schedule();
    }

    public static void recusiveDeviceControl(List<homeSystem> devices, boolean turnOn) {
        if(devices.isEmpty()){
            System.out.println("No devices");
            return;
        }
        homeSystem currentDevice = devices.get(0);
        if(turnOn){
            currentDevice.turnOn();
        }else{
            currentDevice.turnOff();
        }

        if(devices.size() > 1){
            recusiveDeviceControl(devices.subList(1, devices.size()), turnOn);
        }
    }


    public schedule getScedule(){
        return schedule;
    }

    public void addDevice(homeSystem homeSystem) {
        homeSystems.add(homeSystem);
    }

    public void addTheromostat(thermostat thermostat){
        this.thermostat = thermostat;
        addDevice(thermostat);
    }

    public static homeSystem getDeviceByName(String deviceName){
        for(homeSystem device : homeSystems){
            if(device.getDevice().equalsIgnoreCase(deviceName)){
                return device;
            }
        }
        return null;
    }

    public static boolean controlDevice(String deviceName, boolean turnOn){
        boolean deviceFound = false;
        for(homeSystem device : homeSystems){
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

    public static List<homeSystem> getAllDevices() {
        return new ArrayList<>(homeSystems);
    }

    public thermostat getThermostat() {
        return thermostat;
    }

    public void sortDevicesByLocation(){
        homeSystems.sort(new deviceLocationSorter());
    }

    public void displayDevices(){
        if(homeSystems.isEmpty()){
            System.out.println("No available devices.");
            return;
        }else{
            for(homeSystem device : homeSystems){
                device.displayInfo();
                System.out.println(", ");
            }
        }
    }
}
