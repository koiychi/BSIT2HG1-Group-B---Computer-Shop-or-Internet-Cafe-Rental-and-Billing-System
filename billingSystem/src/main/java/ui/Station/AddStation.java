package ui.Station;

import service.StationsService;
import logic.GamingStation;
import logic.RegularStation;
import logic.Station;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * Form for adding a station (new AddStation()) or editing one (new AddStation(station)).
 * Saves through StationsService. The callback is called after a successful save so the
 * caller can refresh its screen.
 */
public class AddStation extends JFrame {

    private final StationsService service = new StationsService();
    private final Station editing;                 // null = add mode
    private Consumer<Station> onStationSaved;

    private final JTextField txtId = new JTextField(18);
    private final JComboBox<String> cboType = new JComboBox<>(new String[]{"Regular", "Gaming"});
    private final JCheckBox chkInternet = new JCheckBox("Has Internet");
    private final JTextField txtRate = new JTextField();
    private final JComboBox<String> cboStatus =
            new JComboBox<>(new String[]{Station.AVAILABLE, Station.IN_USE, Station.MAINTENANCE});
    private final JTextField txtCpu = new JTextField();
    private final JTextField txtRam = new JTextField();
    private final JLabel lblGpu = new JLabel("GPU");
    private final JTextField txtGpu = new JTextField();
    private final JLabel lblHz = new JLabel("Monitor Hz");
    private final JTextField txtHz = new JTextField();
    private final JCheckBox chkPrinter = new JCheckBox("Has Printer");

    public AddStation() {
        this(null);
    }

    public AddStation(Station existing) {
        this.editing = existing;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        buildForm();
        if (existing != null) {
            fillFrom(existing);
        }
        updateTypeFields();
        pack();
        setLocationRelativeTo(null);
    }

    public void setOnStationSaved(Consumer<Station> callback) {
        this.onStationSaved = callback;
    }

    public String getStatus() {
        return (String) cboStatus.getSelectedItem();
    }

    // ---------- layout ----------
    private void buildForm() {
        setTitle(editing == null ? "Add Station" : "Edit Station");

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        int r = 0;
        addRow(form, r++, new JLabel("Station ID"), txtId);
        addRow(form, r++, new JLabel("Station Type"), cboType);
        addRow(form, r++, null, chkInternet);
        addRow(form, r++, new JLabel("Hourly Rate"), txtRate);
        addRow(form, r++, new JLabel("Status"), cboStatus);
        addRow(form, r++, new JLabel("CPU"), txtCpu);
        addRow(form, r++, new JLabel("RAM (GB)"), txtRam);
        addRow(form, r++, lblGpu, txtGpu);
        addRow(form, r++, lblHz, txtHz);
        addRow(form, r++, null, chkPrinter);

        JButton btnCancel = new JButton("Cancel");
        JButton btnSave = new JButton("Save");
        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> save());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.add(btnCancel);
        buttons.add(btnSave);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = r;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(14, 4, 0, 4);
        form.add(buttons, c);

        cboType.addActionListener(e -> {
            updateTypeFields();
            pack();
        });

        setContentPane(form);
        getRootPane().setDefaultButton(btnSave);
    }

    private void addRow(JPanel form, int row, JLabel label, Component field) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = row;
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.WEST;
        if (label != null) {
            c.gridx = 0;
            form.add(label, c);
            c.gridx = 1;
            c.weightx = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            form.add(field, c);
        } else {
            c.gridx = 1;
            form.add(field, c);
        }
    }

    private void updateTypeFields() {
        boolean gaming = "Gaming".equals(cboType.getSelectedItem());
        lblGpu.setVisible(gaming);
        txtGpu.setVisible(gaming);
        lblHz.setVisible(gaming);
        txtHz.setVisible(gaming);
        chkPrinter.setVisible(!gaming);
    }

    private void fillFrom(Station s) {
        txtId.setText(s.getStationID());
        txtId.setEditable(false);                  // ID cannot change
        cboType.setSelectedItem(s.getType());
        cboType.setEnabled(false);                 // type cannot change
        chkInternet.setSelected(s.hasInternet());
        txtRate.setText(String.valueOf(s.getHourlyRate()));
        cboStatus.setSelectedItem(s.getStatus());
        txtCpu.setText(s.getCpu());
        txtRam.setText(String.valueOf(s.getRamGb()));
        if (s instanceof GamingStation) {
            GamingStation g = (GamingStation) s;
            txtGpu.setText(g.getGpu());
            txtHz.setText(String.valueOf(g.getMonitorHz()));
        } else if (s instanceof RegularStation) {
            chkPrinter.setSelected(((RegularStation) s).hasPrinter());
        }
    }

    // ---------- save ----------
    private void save() {
        String id = txtId.getText().trim();
        String type = (String) cboType.getSelectedItem();
        String status = (String) cboStatus.getSelectedItem();
        boolean gaming = "Gaming".equals(type);
        String rateText = txtRate.getText().trim();
        String cpu = txtCpu.getText().trim();
        String ramText = txtRam.getText().trim();
        String gpu = txtGpu.getText().trim();
        String hzText = txtHz.getText().trim();

        if (id.isEmpty() || rateText.isEmpty() || cpu.isEmpty() || ramText.isEmpty()
                || (gaming && (gpu.isEmpty() || hzText.isEmpty()))) {
            warn("Missing Information", "Please fill in all fields.");
            return;
        }

        double rate;
        int ram;
        int hz = 0;
        try {
            rate = Double.parseDouble(rateText);
        } catch (NumberFormatException e) {
            warn("Invalid Rate", "Please enter a valid hourly rate.");
            return;
        }
        try {
            ram = Integer.parseInt(ramText);
        } catch (NumberFormatException e) {
            warn("Invalid RAM", "RAM must be a whole number in GB.");
            return;
        }
        if (gaming) {
            try {
                hz = Integer.parseInt(hzText);
            } catch (NumberFormatException e) {
                warn("Invalid Monitor Hz", "Monitor refresh rate must be a whole number.");
                return;
            }
        }

        try {
            boolean internet = chkInternet.isSelected();
            Station station;
            if (gaming) {
                List<String> games = (editing instanceof GamingStation)
                        ? ((GamingStation) editing).getGamesInstalled() : new ArrayList<String>();
                station = new GamingStation(id, rate, internet, status, cpu, ram, gpu, hz, games);
            } else {
                List<String> software = (editing instanceof RegularStation)
                        ? ((RegularStation) editing).getSoftwareList() : new ArrayList<String>();
                station = new RegularStation(id, rate, internet, status, cpu, ram,
                        software, chkPrinter.isSelected());
            }

            if (editing == null) {
                service.addStation(station);
            } else {
                service.editStation(station);
            }

            if (onStationSaved != null) {
                onStationSaved.accept(station);
            }
            JOptionPane.showMessageDialog(this,
                    editing == null ? "Station added successfully!" : "Station updated successfully!");
            dispose();

        } catch (IllegalArgumentException | IllegalStateException ex) {
            warn("Could Not Save", ex.getMessage());
        }
    }

    private void warn(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new AddStation().setVisible(true));
    }
}