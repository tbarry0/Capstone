import java.util.*;
public class homeSystem {
    private String device;
    private boolean on;
    String location;
    //constructor to initialize homesystem/device
    public homeSystem(String device, String location){
        this.device = device;
        this.on = false; //default set to off
        this.location = location;
    }
    //get device name
    public String getDevice(){
        return device;
    }

    public String getLocation(){
        return location;
    }
    //turn the device on
    public void turnOn(){
        this.on = true;
        System.out.println(device + " is ON.");
    }
    //turn device off
    public void turnOff(){
        this.on = false;
        System.out.println(device + " is OFF.");
    }
    //Gets device's stauts
    public boolean getStatus(){
        return on;
    }

    public void displayInfo(){
        System.out.println("Device type: " + getDevice());
        System.out.println("Device Status: " + (getStatus() ? "ON" : "OFF"));
        System.out.println("Device location: " + location);
    }
}

class thermostat extends homeSystem{
    private int temp;

    public thermostat(String device, String location, int standardTemp){
        super(device, location);
        this.temp = standardTemp;
    }
    public int getTemp(){
        return temp;
    }
    public void setTemp(int temp){
        this.temp = temp;
        System.out.println("Temperature is set: " + temp + "ºF");
    }
}

class schedule{
    private Timer timer;

    public schedule(){
        timer = new Timer();
    }

    public void setTimer(long delay, homeSystem device, boolean turnOn) {
        timer.schedule(new TimerTask() {
            @Override
            public void run(){
                if(turnOn){
                    device.turnOn();
                }else{
                    device.turnOff();
                }
            }
        }, delay);
    }
}

class deviceLocationSorter implements Comparator<homeSystem> {
    @Override
    public int compare(homeSystem device1, homeSystem device2){
        return device1.getLocation().compareToIgnoreCase(device2.getLocation());
    }
}






