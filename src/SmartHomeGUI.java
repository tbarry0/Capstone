import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.List;
import java.util.Scanner;

public class SmartHomeGUI extends JFrame {
    private static final String DEVICES_FILE = "src/devices.txt";
    private smartHomeSystem<homeSystem> smartHome = new smartHomeSystem<>();
    private DefaultTableModel model = new DefaultTableModel(new String[]{"Device", "Location", "Status"}, 0);
    private JTable table = new JTable(model);

    public SmartHomeGUI() {
        setTitle("Smart Home");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        UIManager.put("Label.font", new Font("SansSerif", Font.PLAIN, 12));
        UIManager.put("TextArea.font", new Font("SansSerif", Font.PLAIN, 12));
        UIManager.put("Table.font", new Font("SansSerif", Font.PLAIN, 12));
        UIManager.put("Button.font", new Font("SansSerif", Font.PLAIN, 12));

        loadDevicesFromFile();

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(this::refreshDevices);

        JButton controlAll = new JButton("Toggle All");
        controlAll.addActionListener(this::toggleAllDevices);

        JButton displayStacks = new JButton("Show ON/OFF Devices");
        displayStacks.addActionListener(this::displayOnOffDevices);

        JButton controlSingle = new JButton("Control Device");
        controlSingle.addActionListener(e -> controlDevice());

        JButton scheduleButton = new JButton("Schedule Device");
        scheduleButton.addActionListener(e -> scheduleDevice());

        JPanel panel = new JPanel();
        panel.add(refresh);
        panel.add(controlAll);
        panel.add(controlSingle);
        panel.add(scheduleButton);
        panel.add(displayStacks);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
        refreshDevices(null);
        setVisible(true);
    }

    private void refreshDevices(ActionEvent e) {
        model.setRowCount(0);
        for (homeSystem d : smartHomeSystem.getAllDevices()) {
            model.addRow(new Object[]{d.getDevice(), d.getLocation(), d.getStatus() ? "ON" : "OFF"});
        }
    }

    private void toggleAllDevices(ActionEvent e) {
        boolean anyOff = smartHomeSystem.getAllDevices().stream().anyMatch(d -> !d.getStatus());
        for (homeSystem d : smartHomeSystem.getAllDevices()) {
            if (anyOff) d.turnOn();
            else d.turnOff();
        }
        refreshDevices(null);
    }

    private void controlDevice() {
        String deviceName = JOptionPane.showInputDialog(this, "Enter device name:");
        if (deviceName == null || deviceName.trim().isEmpty()) return;

        homeSystem device = smartHomeSystem.getDeviceByName(deviceName.trim());
        if (device == null) {
            JOptionPane.showMessageDialog(this, "Device not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Turn ON the device?", "Control Device", JOptionPane.YES_NO_OPTION);
        boolean turnOn = (confirm == JOptionPane.YES_OPTION);
        smartHomeSystem.controlDevice(deviceName, turnOn);
        refreshDevices(null);
    }

    private void scheduleDevice() {
        String deviceName = JOptionPane.showInputDialog(this, "Enter device name to schedule:");
        if (deviceName == null || deviceName.trim().isEmpty()) return;

        homeSystem device = smartHomeSystem.getDeviceByName(deviceName.trim());
        if (device == null) {
            JOptionPane.showMessageDialog(this, "Device not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String delayStr = JOptionPane.showInputDialog(this, "Enter delay in milliseconds:");
        try {
            long delay = Long.parseLong(delayStr.trim());
            int confirm = JOptionPane.showConfirmDialog(this, "Turn ON the device after delay?", "Schedule Device", JOptionPane.YES_NO_OPTION);
            boolean turnOn = (confirm == JOptionPane.YES_OPTION);
            smartHome.getScedule().setTimer(delay, device, turnOn);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid delay.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayOnOffDevices(ActionEvent e) {
        JTextArea outputArea = new JTextArea(15, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        GenericStack<homeSystem> onDevices = new GenericStack<>();
        GenericStack<homeSystem> offDevices = new GenericStack<>();

        for (homeSystem device : smartHomeSystem.getAllDevices()) {
            if (device.getStatus()) {
                onDevices.push(device);
            } else {
                offDevices.push(device);
            }
        }

        outputArea.append("=== ON Devices ===\n");
        while (!onDevices.isEmpty()) {
            homeSystem d = onDevices.pop();
            outputArea.append("- " + d.getDevice() + " in " + d.getLocation() + "\n");
        }

        outputArea.append("\n=== OFF Devices ===\n");
        while (!offDevices.isEmpty()) {
            homeSystem d = offDevices.pop();
            outputArea.append("- " + d.getDevice() + " in " + d.getLocation() + "\n");
        }

        JOptionPane.showMessageDialog(this, scrollPane, "Device States (Stacks)", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadDevicesFromFile() {
        File file = new File(DEVICES_FILE);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "Device file not found: " + DEVICES_FILE, "Error", JOptionPane.ERROR_MESSAGE);
            return;
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
                            thermostat t = new thermostat(type, location, defaultTemp);
                            smartHome.addThermostat(t);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid temperature for thermostat: " + line);
                        }
                    } else {
                        homeSystem device = new homeSystem(type, location);
                        smartHome.addDevice(device);
                    }
                }
            }
            smartHome.sortDevicesByLocation();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to read devices file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmartHomeGUI::new);
    }
}
