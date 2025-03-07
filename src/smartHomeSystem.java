import java.util.*;
public class smartHomeSystem {
    public static schedule getSchedule;
    private static List<homeSystem> homeSystems;
    private thermostat thermostat;
    private schedule schedule;

    public static void recusiveDeviceControl(List<homeSystem> devices, boolean turnOn) {
        if(devices.isEmpty()){
            System.out.println("No devices");
        }else{
            String currentDevice = String.valueOf(devices.get(0));
            controlDevice(currentDevice, turnOn);

            recursiveDeviceControl(devices.subList(1, devices.size()), turnOn);
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
    }

    public static homeSystem getDeviceByName(String deviceName){
        for(homeSystem homeSystem: homeSystems){
            if(homeSystem.getDevice().equalsIgnoreCase(deviceName)){
                return homeSystem;
            }
        }
        return null;
    }

    public static void recursiveDeviceControl(List<homeSystem> deviceNames, boolean turnOn){

    }

    public static void controlDevice(String deviceName, boolean turnOn){
        for(homeSystem homeSystem : homeSystems){
            if(homeSystem.getDevice().equalsIgnoreCase(deviceName)){
                if(turnOn){
                    homeSystem.turnOn();
                }else{
                    homeSystem.turnOff();
                }
            }
        }
    }
}
