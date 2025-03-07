import java.util.*;
public class smartHomeSystem {
    public static schedule getSchedule;
    private static List<homeSystem> homeSystems;
    private thermostat thermostat;
    private schedule schedule;


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

    public void recursiveDeviceControl(List<String> deviceNames, boolean turnOn){
        if(deviceNames.isEmpty()){
            return;
        }else{
            String currentDevice = deviceNames.get(0);
            controlDevice(currentDevice, turnOn);

            recursiveDeviceControl(deviceNames.subList(1, deviceNames.size()), turnOn);
        }
    }

    public void controlDevice(String deviceName, boolean turnOn){
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
