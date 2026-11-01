/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.Station;

/**
 *
 * @author koi
 */

import service.StationsService;
import logic.GamingStation;
import logic.Station;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import system.ShopManager;

public class stationsTabPanel extends javax.swing.JPanel
{
    // ---------- colors ----------
    private static final Color BG = new Color(10, 10, 15);
    private static final Color PANEL = new Color(22, 26, 34);
    private static final Color ORANGE = new Color(255, 106, 0);
    private static final Color GREEN = new Color(34, 160, 70);
    private static final Color RED = new Color(200, 55, 55);
    private static final Color AMBER = new Color(230, 160, 30);
    private static final Color BLUE = new Color(60, 90, 160);
    private final Color normalColor = new Color(26, 29, 38);
    private final Color selectedColor = new Color(40, 46, 60);

    // ---------- data ----------
    private final StationsService service = new StationsService();
    private JPanel selectedCard = null;
    private String selectedId = null;          // remembered across reloads

    // ---------- controls ----------
    private final JTextField txtSearch = new JTextField(14);
    private final JComboBox<String> cboTypeFilter =
            new JComboBox<>(new String[]{"All", "Regular", "Gaming"});
    private final JComboBox<String> cboStatusFilter =
            new JComboBox<>(new String[]{"All", Station.AVAILABLE, Station.IN_USE, Station.MAINTENANCE});
    private final JComboBox<String> cboSort =
            new JComboBox<>(new String[]{StationsService.SORT_ID,
                StationsService.SORT_RATE, StationsService.SORT_STATUS});
    private final JButton addBtn = new JButton("+ Add Station");
    private final JButton statusBtn = new JButton("Set Status \u25BE");
    private final JButton deleteBtn = new JButton("Delete");

    public stationsTabPanel() {
        initComponents();
        applyTheme();
        setupToolbar();
        ShopManager.setStations(this);
        loadStations();
    }

    public void updateShopState(boolean open) {
        statusBtn.setEnabled(open);
        cboStatusFilter.setEnabled(open);
        loadStations();
    }

    private void applyTheme() {
        setBackground(BG);
        MidPanel.setBackground(BG);
        RegularPanel.setBackground(BG);
        RegularPanel2.setBackground(BG);
        regularcardsPanel.setBackground(BG);
        regularcardsPanel2.setBackground(BG);
        jPanel1.setBackground(PANEL);

        styleHeader(RegularHeader, jLabel1);
        styleHeader(RegularHeader2, jLabel3);

        styleButton(editStationBtn, ORANGE);
    }

    private void styleHeader(JPanel header, JLabel label) {
        header.setBackground(PANEL);
        header.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, ORANGE));
        label.setForeground(Color.WHITE);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 20f));
        label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
    }

    private void styleButton(JButton b, Color bg) {
        b.setPreferredSize(new Dimension(140, 35));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private JLabel whiteLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        return l;
    }

    // ---------- toolbar: filters on the left, buttons on the right ----------
    private void setupToolbar() {
        editStationBtn.setText("Edit Station");
        styleButton(addBtn, GREEN);
        styleButton(statusBtn, BLUE);
        styleButton(deleteBtn, RED);

        addBtn.addActionListener(e -> addStation());
        statusBtn.addActionListener(e -> showStatusMenu());
        deleteBtn.addActionListener(e -> deleteStation());

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        filterBar.setOpaque(false);
        filterBar.add(whiteLabel("Search:"));  filterBar.add(txtSearch);
        filterBar.add(whiteLabel("Type:"));    filterBar.add(cboTypeFilter);
        filterBar.add(whiteLabel("Status:"));  filterBar.add(cboStatusFilter);
        filterBar.add(whiteLabel("Sort:"));    filterBar.add(cboSort);

        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        buttonBar.setOpaque(false);
        buttonBar.add(addBtn);
        buttonBar.add(editStationBtn);
        buttonBar.add(statusBtn);
        buttonBar.add(deleteBtn);

        jPanel1.removeAll();
        jPanel1.setLayout(new BorderLayout());
        jPanel1.setPreferredSize(new Dimension(100, 56));
        jPanel1.add(filterBar, BorderLayout.WEST);
        jPanel1.add(buttonBar, BorderLayout.EAST);

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { loadStations(); }
            @Override public void removeUpdate(DocumentEvent e)  { loadStations(); }
            @Override public void changedUpdate(DocumentEvent e) { loadStations(); }
        });
        cboTypeFilter.addActionListener(e -> loadStations());
        cboStatusFilter.addActionListener(e -> loadStations());
        cboSort.addActionListener(e -> loadStations());
    }

    // ---------- load cards from the database ----------
        private void loadStations() {
        regularcardsPanel.removeAll();
        regularcardsPanel2.removeAll();
        selectedCard = null;

        List<Station> shown = service.searchFilterSort(
                service.getAllStations(),
                txtSearch.getText(),
                (String) cboTypeFilter.getSelectedItem(),
                (String) cboStatusFilter.getSelectedItem(),
                (String) cboSort.getSelectedItem());

        for (Station s : shown) {
            JPanel card = buildCard(s);
            if (s instanceof GamingStation) {
                regularcardsPanel2.add(card);
            } else {
                regularcardsPanel.add(card);
            }
            if (s.getStationID().equals(selectedId)) {
                selectCard(card);
            }
        }

        if (selectedCard == null) {
            selectedId = null;       // the selected station is hidden by a filter or was deleted
        }

        updateCounts();
        regularcardsPanel.revalidate();
        regularcardsPanel.repaint();
        regularcardsPanel2.revalidate();
        regularcardsPanel2.repaint();
    }

    private JPanel buildCard(Station s) {
        boolean shopOpen = ShopManager.isShopOpen();
        String displayStatus = shopOpen ? s.getStatus() : "-";
        Color statusColor;
        switch (displayStatus) {
            case Station.IN_USE:
                statusColor = RED;
                break;
            case Station.MAINTENANCE:
                statusColor = AMBER;
                break;
            default:
                statusColor = GREEN;
                break;
        }
        String hex = String.format("#%02x%02x%02x",
                statusColor.getRed(), statusColor.getGreen(), statusColor.getBlue());

        final JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(190, 135));
        card.setBackground(normalColor);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(statusColor, 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        card.putClientProperty("stationId", s.getStationID());
        card.setToolTipText("<html>" + s.getStationInfo().replace("\n", "<br>") + "</html>");

        JLabel icon = new JLabel();
        URL iconUrl = getClass().getResource("/icons/computer_30dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.png");
        if (iconUrl != null) {
            icon.setIcon(new ImageIcon(iconUrl));
        }
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel text = new JLabel("<html><center>"
                + "<font size='5'><b>" + s.getStationID() + "</b></font><br>"
                + "<font color='#aab0ba'>" + s.getType() + " \u00B7 \u20B1"
                + String.format("%.2f", s.getHourlyRate()) + "/hr</font><br>"
                + "<font color='" + hex + "'>&#9679;</font> " + displayStatus
                + "</center></html>");
        text.setForeground(Color.WHITE);
        text.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(icon);
        card.add(text);

        MouseAdapter click = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectCard(card);
            }
        };
        card.addMouseListener(click);
        icon.addMouseListener(click);
        text.addMouseListener(click);
        return card;
    }

    private void selectCard(JPanel card) {
        if (selectedCard != null) selectedCard.setBackground(normalColor);
        selectedCard = card;
        selectedId = (String) card.getClientProperty("stationId");
        card.setBackground(selectedColor);
    }

    // ---------- actions ----------
    private void addStation() {
        AddStation form = new AddStation();
        form.setOnStationSaved(saved -> {
            selectedId = saved.getStationID();
            loadStations();
        });
        form.setVisible(true);
    }

    private void deleteStation() {
        if (!hasSelection()) return;
        int choice = JOptionPane.showConfirmDialog(this,
                "Delete " + selectedId + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) return;
        try {
            service.deleteStation(selectedId);
            selectedId = null;
            loadStations();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Could Not Delete", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showStatusMenu() {
        if (!hasSelection()) return;
        JPopupMenu menu = new JPopupMenu();
        for (String st : new String[]{Station.AVAILABLE, Station.IN_USE, Station.MAINTENANCE}) {
            JMenuItem item = new JMenuItem(st);
            item.addActionListener(e -> changeSelectedStatus(st));
            menu.add(item);
        }
        menu.show(statusBtn, 0, statusBtn.getHeight());
    }

    private void changeSelectedStatus(String newStatus) {
        try {
            service.changeStatus(selectedId, newStatus);   // writes to the database
            loadStations();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Could Not Change Status", JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean hasSelection() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Please select a station first.");
            return false;
        }
        return true;
    }
    /**
     * This method is called from within the constructor to initialize the form.
        * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MidPanel = new javax.swing.JPanel();
        RegularPanel = new javax.swing.JPanel();
        RegularHeader = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        regularcardsPanel = new javax.swing.JPanel();
        pc01Panel = new javax.swing.JPanel();
        pc01Icon = new javax.swing.JLabel();
        pc01Label = new javax.swing.JLabel();
        pc02Panel = new javax.swing.JPanel();
        pc02Icon = new javax.swing.JLabel();
        pc02Label = new javax.swing.JLabel();
        pc03Panel = new javax.swing.JPanel();
        pc03Icon = new javax.swing.JLabel();
        pc03Label = new javax.swing.JLabel();
        pc04Panel = new javax.swing.JPanel();
        pc04Icon = new javax.swing.JLabel();
        pc04Label = new javax.swing.JLabel();
        pc05Panel = new javax.swing.JPanel();
        pc05Icon = new javax.swing.JLabel();
        pc05Label = new javax.swing.JLabel();
        RegularPanel2 = new javax.swing.JPanel();
        RegularHeader2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        regularcardsPanel2 = new javax.swing.JPanel();
        pc01Panel1 = new javax.swing.JPanel();
        pc01Icon2 = new javax.swing.JLabel();
        pc01Label2 = new javax.swing.JLabel();
        pc02Panel2 = new javax.swing.JPanel();
        pc02Icon2 = new javax.swing.JLabel();
        pc02Label2 = new javax.swing.JLabel();
        pc03Panel2 = new javax.swing.JPanel();
        pc03Icon2 = new javax.swing.JLabel();
        pc03Label2 = new javax.swing.JLabel();
        pc04Panel2 = new javax.swing.JPanel();
        pc04Icon2 = new javax.swing.JLabel();
        pc04Label2 = new javax.swing.JLabel();
        pc05Panel2 = new javax.swing.JPanel();
        pc05Icon2 = new javax.swing.JLabel();
        pc05Label2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        editStationBtn = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        MidPanel.setLayout(new java.awt.GridLayout(2, 1, 0, 10));

        RegularPanel.setLayout(new java.awt.BorderLayout());

        RegularHeader.setBackground(new java.awt.Color(217, 217, 217));
        RegularHeader.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Regular Station (5)");
        RegularHeader.add(jLabel1, java.awt.BorderLayout.CENTER);

        RegularPanel.add(RegularHeader, java.awt.BorderLayout.NORTH);

        regularcardsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 10));

        pc01Panel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        pc01Panel.setMaximumSize(new java.awt.Dimension(190, 135));
        pc01Panel.setMinimumSize(new java.awt.Dimension(190, 135));
        pc01Panel.setPreferredSize(new java.awt.Dimension(190, 135));
        pc01Panel.setLayout(new javax.swing.BoxLayout(pc01Panel, javax.swing.BoxLayout.Y_AXIS));

        pc01Icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc01Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/computer_30dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        pc01Icon.setAlignmentX(0.5F);
        pc01Panel.add(pc01Icon);

        pc01Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc01Label.setText("PC 01");
        pc01Label.setAlignmentX(0.5F);
        pc01Panel.add(pc01Label);

        regularcardsPanel.add(pc01Panel);

        pc02Panel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        pc02Panel.setMaximumSize(new java.awt.Dimension(190, 135));
        pc02Panel.setMinimumSize(new java.awt.Dimension(190, 135));
        pc02Panel.setPreferredSize(new java.awt.Dimension(190, 135));
        pc02Panel.setLayout(new javax.swing.BoxLayout(pc02Panel, javax.swing.BoxLayout.Y_AXIS));

        pc02Icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc02Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/computer_30dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        pc02Icon.setAlignmentX(0.5F);
        pc02Panel.add(pc02Icon);

        pc02Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc02Label.setText("PC 02");
        pc02Label.setAlignmentX(0.5F);
        pc02Panel.add(pc02Label);

        regularcardsPanel.add(pc02Panel);

        pc03Panel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        pc03Panel.setMaximumSize(new java.awt.Dimension(190, 135));
        pc03Panel.setMinimumSize(new java.awt.Dimension(190, 135));
        pc03Panel.setPreferredSize(new java.awt.Dimension(190, 135));
        pc03Panel.setLayout(new javax.swing.BoxLayout(pc03Panel, javax.swing.BoxLayout.Y_AXIS));

        pc03Icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc03Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/computer_30dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        pc03Icon.setAlignmentX(0.5F);
        pc03Panel.add(pc03Icon);

        pc03Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc03Label.setText("PC 03");
        pc03Label.setAlignmentX(0.5F);
        pc03Panel.add(pc03Label);

        regularcardsPanel.add(pc03Panel);

        pc04Panel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        pc04Panel.setMaximumSize(new java.awt.Dimension(190, 135));
        pc04Panel.setMinimumSize(new java.awt.Dimension(190, 135));
        pc04Panel.setPreferredSize(new java.awt.Dimension(190, 135));
        pc04Panel.setLayout(new javax.swing.BoxLayout(pc04Panel, javax.swing.BoxLayout.Y_AXIS));

        pc04Icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc04Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/computer_30dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        pc04Icon.setAlignmentX(0.5F);
        pc04Panel.add(pc04Icon);

        pc04Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc04Label.setText("PC 04");
        pc04Label.setAlignmentX(0.5F);
        pc04Panel.add(pc04Label);

        regularcardsPanel.add(pc04Panel);

        pc05Panel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        pc05Panel.setMaximumSize(new java.awt.Dimension(190, 135));
        pc05Panel.setMinimumSize(new java.awt.Dimension(190, 135));
        pc05Panel.setPreferredSize(new java.awt.Dimension(190, 135));
        pc05Panel.setLayout(new javax.swing.BoxLayout(pc05Panel, javax.swing.BoxLayout.Y_AXIS));

        pc05Icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc05Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/computer_30dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        pc05Icon.setAlignmentX(0.5F);
        pc05Panel.add(pc05Icon);

        pc05Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc05Label.setText("PC 05");
        pc05Label.setAlignmentX(0.5F);
        pc05Panel.add(pc05Label);

        regularcardsPanel.add(pc05Panel);

        RegularPanel.add(regularcardsPanel, java.awt.BorderLayout.CENTER);

        MidPanel.add(RegularPanel);

        RegularPanel2.setLayout(new java.awt.BorderLayout());

        RegularHeader2.setBackground(new java.awt.Color(217, 217, 217));
        RegularHeader2.setLayout(new java.awt.BorderLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Gaming Station (5)");
        RegularHeader2.add(jLabel3, java.awt.BorderLayout.CENTER);

        RegularPanel2.add(RegularHeader2, java.awt.BorderLayout.NORTH);

        regularcardsPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 10));

        pc01Panel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        pc01Panel1.setMaximumSize(new java.awt.Dimension(190, 135));
        pc01Panel1.setMinimumSize(new java.awt.Dimension(190, 135));
        pc01Panel1.setPreferredSize(new java.awt.Dimension(190, 135));
        pc01Panel1.setLayout(new javax.swing.BoxLayout(pc01Panel1, javax.swing.BoxLayout.Y_AXIS));

        pc01Icon2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc01Icon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/computer_30dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        pc01Icon2.setAlignmentX(0.5F);
        pc01Panel1.add(pc01Icon2);

        pc01Label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc01Label2.setText("PC 06");
        pc01Label2.setAlignmentX(0.5F);
        pc01Panel1.add(pc01Label2);

        regularcardsPanel2.add(pc01Panel1);

        pc02Panel2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        pc02Panel2.setMaximumSize(new java.awt.Dimension(190, 135));
        pc02Panel2.setMinimumSize(new java.awt.Dimension(190, 135));
        pc02Panel2.setPreferredSize(new java.awt.Dimension(190, 135));
        pc02Panel2.setLayout(new javax.swing.BoxLayout(pc02Panel2, javax.swing.BoxLayout.Y_AXIS));

        pc02Icon2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc02Icon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/computer_30dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        pc02Icon2.setAlignmentX(0.5F);
        pc02Panel2.add(pc02Icon2);

        pc02Label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc02Label2.setText("PC 07");
        pc02Label2.setAlignmentX(0.5F);
        pc02Panel2.add(pc02Label2);

        regularcardsPanel2.add(pc02Panel2);

        pc03Panel2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        pc03Panel2.setMaximumSize(new java.awt.Dimension(190, 135));
        pc03Panel2.setMinimumSize(new java.awt.Dimension(190, 135));
        pc03Panel2.setPreferredSize(new java.awt.Dimension(190, 135));
        pc03Panel2.setLayout(new javax.swing.BoxLayout(pc03Panel2, javax.swing.BoxLayout.Y_AXIS));

        pc03Icon2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc03Icon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/computer_30dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        pc03Icon2.setAlignmentX(0.5F);
        pc03Panel2.add(pc03Icon2);

        pc03Label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc03Label2.setText("PC 08");
        pc03Label2.setAlignmentX(0.5F);
        pc03Panel2.add(pc03Label2);

        regularcardsPanel2.add(pc03Panel2);

        pc04Panel2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        pc04Panel2.setMaximumSize(new java.awt.Dimension(190, 135));
        pc04Panel2.setMinimumSize(new java.awt.Dimension(190, 135));
        pc04Panel2.setPreferredSize(new java.awt.Dimension(190, 135));
        pc04Panel2.setLayout(new javax.swing.BoxLayout(pc04Panel2, javax.swing.BoxLayout.Y_AXIS));

        pc04Icon2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc04Icon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/computer_30dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        pc04Icon2.setAlignmentX(0.5F);
        pc04Panel2.add(pc04Icon2);

        pc04Label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc04Label2.setText("PC 09");
        pc04Label2.setAlignmentX(0.5F);
        pc04Panel2.add(pc04Label2);

        regularcardsPanel2.add(pc04Panel2);

        pc05Panel2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        pc05Panel2.setMaximumSize(new java.awt.Dimension(190, 135));
        pc05Panel2.setMinimumSize(new java.awt.Dimension(190, 135));
        pc05Panel2.setPreferredSize(new java.awt.Dimension(190, 135));
        pc05Panel2.setLayout(new javax.swing.BoxLayout(pc05Panel2, javax.swing.BoxLayout.Y_AXIS));

        pc05Icon2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc05Icon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/computer_30dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        pc05Icon2.setAlignmentX(0.5F);
        pc05Panel2.add(pc05Icon2);

        pc05Label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pc05Label2.setText("PC 10");
        pc05Label2.setAlignmentX(0.5F);
        pc05Panel2.add(pc05Label2);

        regularcardsPanel2.add(pc05Panel2);

        RegularPanel2.add(regularcardsPanel2, java.awt.BorderLayout.CENTER);

        MidPanel.add(RegularPanel2);

        add(MidPanel, java.awt.BorderLayout.CENTER);

        jPanel1.setPreferredSize(new java.awt.Dimension(100, 50));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        editStationBtn.setText("+ Edit Station");
        editStationBtn.setPreferredSize(new java.awt.Dimension(150, 35));
        editStationBtn.addActionListener(this::editStationBtnActionPerformed);
        jPanel1.add(editStationBtn);

        add(jPanel1, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void editStationBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editStationBtnActionPerformed
        // TODO add your handling code here:
         if (!hasSelection()) return;
        Station s = service.findStation(selectedId);
        if (s == null) {
            selectedId = null;
            loadStations();
            return;
        }

        java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(this);
        java.awt.Frame owner = (w instanceof java.awt.Frame) ? (java.awt.Frame) w : null;
        EditStation dialog = new EditStation(owner, true);
        dialog.setStation(s);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);                       // modal: waits here until it closes

        try {
            if (dialog.isDeleted()) {
                service.deleteStation(s.getStationID());
                selectedId = null;
                loadStations();
            } else if (dialog.isSaved()) {
                service.editStation(dialog.getEditedStation());
                loadStations();
            }
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Could Not Save Changes", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_editStationBtnActionPerformed
    private void updateCounts() {
        jLabel1.setText("Regular Station (" + countCards(regularcardsPanel) + ")");
        jLabel3.setText("Gaming Station (" + countCards(regularcardsPanel2) + ")");
    }

    private int countCards(JPanel container) {
        int n = 0;
        for (Component c : container.getComponents()) {
            if (c instanceof JPanel) {
                n++;
            }
        }
        return n;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MidPanel;
    private javax.swing.JPanel RegularHeader;
    private javax.swing.JPanel RegularHeader2;
    private javax.swing.JPanel RegularPanel;
    private javax.swing.JPanel RegularPanel2;
    private javax.swing.JButton editStationBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel pc01Icon;
    private javax.swing.JLabel pc01Icon2;
    private javax.swing.JLabel pc01Label;
    private javax.swing.JLabel pc01Label2;
    private javax.swing.JPanel pc01Panel;
    private javax.swing.JPanel pc01Panel1;
    private javax.swing.JLabel pc02Icon;
    private javax.swing.JLabel pc02Icon2;
    private javax.swing.JLabel pc02Label;
    private javax.swing.JLabel pc02Label2;
    private javax.swing.JPanel pc02Panel;
    private javax.swing.JPanel pc02Panel2;
    private javax.swing.JLabel pc03Icon;
    private javax.swing.JLabel pc03Icon2;
    private javax.swing.JLabel pc03Label;
    private javax.swing.JLabel pc03Label2;
    private javax.swing.JPanel pc03Panel;
    private javax.swing.JPanel pc03Panel2;
    private javax.swing.JLabel pc04Icon;
    private javax.swing.JLabel pc04Icon2;
    private javax.swing.JLabel pc04Label;
    private javax.swing.JLabel pc04Label2;
    private javax.swing.JPanel pc04Panel;
    private javax.swing.JPanel pc04Panel2;
    private javax.swing.JLabel pc05Icon;
    private javax.swing.JLabel pc05Icon2;
    private javax.swing.JLabel pc05Label;
    private javax.swing.JLabel pc05Label2;
    private javax.swing.JPanel pc05Panel;
    private javax.swing.JPanel pc05Panel2;
    private javax.swing.JPanel regularcardsPanel;
    private javax.swing.JPanel regularcardsPanel2;
    // End of variables declaration//GEN-END:variables
}
