import java.util.*;
public class homeSystem {
    private String device;
    private boolean on;
    String location;
    private GenericStack<String> stateHistory;
    //constructor to initialize homesystem/device
    public homeSystem(String device, String location){
        this.device = device;
        this.on = false; //default set to off
        this.location = location;
        this.stateHistory = new GenericStack<>();
        stateHistory.push("OFF");
    }
    //get device name
    public String getDevice(){
        return device;
    }
    //get location
    public String getLocation(){
        return location;
    }
    //set location
    public void setLocation(String location){
        this.location = location;
    }
    //turn the device on
    public void turnOn(){
        this.on = true;
        stateHistory.push("ON");
        System.out.println(device + " is ON.");
    }
    //turn device off
    public void turnOff(){
        this.on = false;
        stateHistory.push("OFF");
        System.out.println(device + " is OFF.");
    }
    //Gets device's stauts
    public boolean getStatus(){
        return on;
    }

    public String getPreviousState(){
        if(stateHistory.size() <= 1){
            return "UNKNOW";
        }

        String current = stateHistory.pop();
        String previous = stateHistory.peek();
        stateHistory.push(current);

        return previous;
    }
    //method to display device info
    public void displayInfo(){
        System.out.println("Device type: " + getDevice());
        System.out.println("Device Status: " + (getStatus() ? "ON" : "OFF"));
        System.out.println("Device location: " + location);
        System.out.println("Previous state: " + getPreviousState());
    }
}
//thermostat class
class thermostat extends homeSystem{
    private int temp;
    private GenericStack<Integer> tempHistory;

    public thermostat(String device, String location, int standardTemp){
        super(device, location);
        this.temp = standardTemp;
        this.tempHistory = new GenericStack<>();
        tempHistory.push(standardTemp);
    }
    public int getTemp(){
        return temp;
    }
    public void setTemp(int temp){
        tempHistory.push(this.temp);
        this.temp = temp;
        System.out.println("Temperature is set: " + temp + "ºF");
    }
    public int getPreviousTemp() {
        if (tempHistory.size() <= 1) {
            return temp; // Return current if no history
        }
        return tempHistory.peek();
    }
    @Override
    public void displayInfo(){
        super.displayInfo();
        System.out.println("Current temperature: " + getTemp() + "ºF");
        System.out.println("Previous temperature: " + getPreviousTemp());
    }
}
//schedule class to create schedules and timers
class schedule{
    private Timer timer;
    private GenericLinkedList<ScheduledTask> scheduledTasks;

    static class ScheduledTask {
        homeSystem device;
        boolean turnOn;
        long scheduledTime;
        long delay;

        ScheduledTask(homeSystem device, boolean turnOn, long delay) {
            this.device = device;
            this.turnOn = turnOn;
            this.delay = delay;
            this.scheduledTime = System.currentTimeMillis() + delay;
        }

        String getFormattedTimeLeft() {
            long timeLeft = scheduledTime - System.currentTimeMillis();
            return smartHomeSystem.formatTime(timeLeft);
        }
    }


    public schedule(){
        timer = new Timer();
        scheduledTasks = new GenericLinkedList<>();
    }

    public void setTimer(long delay, homeSystem device, boolean turnOn) {
        ScheduledTask task = new ScheduledTask(device, turnOn, delay);
        scheduledTasks.add(task);
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
        String formattedTime = smartHomeSystem.formatTime(delay);
        System.out.println("Device " + device.getDevice() + " in " + device.getLocation() + " scheduled to turn " + (turnOn ? "ON" : "OFF") + " in " + formattedTime);
    }

    public List<ScheduledTask> getScheduledTasks() {
        return scheduledTasks.toList();
    }

    public void displayScheduledTasks(){
        List<ScheduledTask> tasks = scheduledTasks.toList();
        if(tasks.isEmpty()){
            System.out.println("No scheduled tasks.");
            return;
        }
        System.out.println("Scheduled Tasks:");
        for(ScheduledTask task : tasks){
            System.out.println("Device: " + task.device.getDevice() + " in " + task.device.getLocation() + " will turn " + (task.turnOn ? "ON" : "OFF") + " in " + task.getFormattedTimeLeft());
        }
    }

    public void cancelTimer(){
        timer.cancel();
        scheduledTasks = new GenericLinkedList<>();
        System.out.println("All scheduled tasks canceled");
    }
}
//class comparator that sorts the devices
class deviceLocationSorter implements Comparator<homeSystem> {
    @Override
    public int compare(homeSystem device1, homeSystem device2){
        return device1.getLocation().compareToIgnoreCase(device2.getLocation());
    }
}








