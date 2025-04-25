import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class SimpleSmartHomeGUI extends JFrame {
    private smartHomeSystem<homeSystem> smartHome = new smartHomeSystem<>();
    private DefaultTableModel model = new DefaultTableModel(new String[]{"Device", "Location", "Status"}, 0);
    private JTable table = new JTable(model);

    public SimpleSmartHomeGUI() {
        setTitle("Smart Home");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(this::refreshDevices);

        JButton controlAll = new JButton("Toggle All");
        controlAll.addActionListener(this::toggleAllDevices);

        JPanel panel = new JPanel();
        panel.add(refresh);
        panel.add(controlAll);

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
            if (anyOff) {
                d.turnOn();
            } else {
                d.turnOff();
            }
        }
        refreshDevices(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimpleSmartHomeGUI::new);
    }
}
