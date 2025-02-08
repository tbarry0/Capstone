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


