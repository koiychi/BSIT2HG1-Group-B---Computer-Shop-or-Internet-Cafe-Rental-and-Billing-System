package system;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import service.StationsService;
import ui.Shop.dashboardTabPanel;
import ui.Station.stationsTabPanel;

public final class ShopManager {

    private static AdminAppWindow adminWindow;
    private static dashboardTabPanel dashboard;
    private static stationsTabPanel stations;
    private static boolean shopOpen;
    private static boolean statusLoaded;
    private static final StationsService stationsService = new StationsService();

    private ShopManager() {
    }

    public static synchronized boolean isShopOpen() {
        if (!statusLoaded) {
            try {
                shopOpen = DatabaseManager.getShopOpenStatus();
                statusLoaded = true;
            } catch (SQLException ex) {
                throw new IllegalStateException("Could not load the shop status.", ex);
            }
        }
        return shopOpen;
    }

    public static void setAdminWindow(AdminAppWindow window) {
        adminWindow = window;
        window.updateShopStatus(isShopOpen());
    }

    public static void setDashboard(dashboardTabPanel panel) {
        dashboard = panel;
        panel.updateShopState(isShopOpen());
    }

    public static void setStations(stationsTabPanel panel) {
        stations = panel;
        panel.updateShopState(isShopOpen());
    }

    public static void refreshDashboard() {
        if (dashboard != null) {
            dashboard.refreshStatistics();
        }
    }

    public static void toggleShop() {
        if (isShopOpen() && !confirmClosing()) {
            return;
        }
        setShopOpen(!isShopOpen());
    }

    private static boolean confirmClosing() {
            int response = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to close the shop?\nNew sessions and transactions will be disabled.",
                    "Confirm Shop Closure",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
        return response == JOptionPane.YES_OPTION;
    }

    public static synchronized void setShopOpen(boolean open) {
        if (statusLoaded && shopOpen == open) {
            return;
        }
        boolean reopening = statusLoaded && !shopOpen && open;
        try {
            if (reopening) {
                stationsService.resetOccupiedStations();
            }
            DatabaseManager.setShopOpenStatus(open);
            shopOpen = open;
            statusLoaded = true;
        } catch (SQLException ex) {
            throw new IllegalStateException("Could not save the shop status.", ex);
        }

        if (adminWindow != null) {
            adminWindow.updateShopStatus(open);
        }
        if (dashboard != null) {
            dashboard.updateShopState(open);
        }
        if (stations != null) {
            stations.updateShopState(open);
        }
    }

    public static boolean ensureShopOpen() {
        if (isShopOpen()) {
            return true;
        }
        JOptionPane.showMessageDialog(
                null,
                "The shop is closed. Open the shop before starting this operation.",
                "Shop Closed",
                JOptionPane.INFORMATION_MESSAGE);
        return false;
    }
}
