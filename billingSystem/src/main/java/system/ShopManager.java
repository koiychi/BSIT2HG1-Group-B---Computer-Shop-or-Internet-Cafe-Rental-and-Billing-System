package system;

import java.awt.Component;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import javax.swing.JOptionPane;

public final class ShopManager {

    private static final List<Consumer<Boolean>> STATE_LISTENERS = new CopyOnWriteArrayList<>();
    private static Boolean shopOpen;

    private ShopManager() {
    }

    public static synchronized boolean isShopOpen() {
        if (shopOpen == null) {
            try {
                shopOpen = DatabaseManager.getShopOpenStatus();
            } catch (SQLException ex) {
                throw new IllegalStateException("Could not load the shop status.", ex);
            }
        }
        return shopOpen;
    }

    public static void addStateListener(Consumer<Boolean> listener) {
        STATE_LISTENERS.add(listener);
        listener.accept(isShopOpen());
    }

    public static void toggleShop(Component parent) {
        if (isShopOpen() && !confirmClosing(parent)) {
            return;
        }
        setShopOpen(!isShopOpen());
    }

    private static boolean confirmClosing(Component parent) {
            int response = JOptionPane.showConfirmDialog(
                    parent,
                    "Are you sure you want to close the shop?\nNew sessions and transactions will be disabled.",
                    "Confirm Shop Closure",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
        return response == JOptionPane.YES_OPTION;
    }

    public static synchronized void setShopOpen(boolean open) {
        if (shopOpen != null && shopOpen == open) {
            return;
        }
        try {
            DatabaseManager.setShopOpenStatus(open);
            shopOpen = open;
        } catch (SQLException ex) {
            throw new IllegalStateException("Could not save the shop status.", ex);
        }

        STATE_LISTENERS.forEach(listener -> listener.accept(open));
    }

    public static boolean ensureShopOpen(Component parent) {
        if (isShopOpen()) {
            return true;
        }
        JOptionPane.showMessageDialog(
                parent,
                "The shop is closed. Open the shop before starting this operation.",
                "Shop Closed",
                JOptionPane.INFORMATION_MESSAGE);
        return false;
    }
}
