/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.Shop;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Window;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
/**
 *
 * @author cals
 */
public final class PaymentUtil {

    private PaymentUtil() {
    }

    // ---------- prices ----------
    public static final BigDecimal PC_RATE_GAMING = new BigDecimal("40.00");    // per hour
    public static final BigDecimal PC_RATE_REGULAR = new BigDecimal("30.00");   // per hour
    public static final BigDecimal PRICE_BW = new BigDecimal("2.00");           // per page
    public static final BigDecimal PRICE_COLOR = new BigDecimal("5.00");        // per page
    public static final BigDecimal MEMBER_DISCOUNT = new BigDecimal("0.10");

    public interface Committer {
        String commit(String paidBy);
    }
    private static final java.util.concurrent.CopyOnWriteArrayList<Runnable> paymentListeners
            = new java.util.concurrent.CopyOnWriteArrayList<>();

    public static void addPaymentListener(Runnable listener) {
        paymentListeners.addIfAbsent(listener);
    }

    public static void removePaymentListener(Runnable listener) {
        paymentListeners.remove(listener);
    }

    public static void firePaymentSaved() {
        for (Runnable r : paymentListeners) {
            r.run();
        }
    }

    // UI says Cash / Credit; the database only accepts 'Cash' or 'Member Credits'
    public static String paidBy(boolean usingCredit) {
        return usingCredit ? "Member Credits" : "Cash";
    }

    public static BigDecimal parseAmount(String text) {
        try {
            return new BigDecimal(text.replace("PHP", "").replace(",", "").trim());
        } catch (NumberFormatException ex) {
            return BigDecimal.ZERO;
        }
    }

    // "PHP 12.00"
    public static String money(BigDecimal value) {
        return "PHP " + value.setScale(2, RoundingMode.HALF_UP);
    }

    public static JPanel printRow(Component left, Component right, int indent) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setBorder(BorderFactory.createEmptyBorder(3, indent, 3, 0));
        p.add(left, BorderLayout.WEST);
        if (right != null) {
            p.add(right, BorderLayout.EAST);
        }
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    // creates the modal dialog every button uses
    public static JDialog newDialog(Component parent, String title) {
        Window owner = SwingUtilities.getWindowAncestor(parent);
        return new JDialog(owner, title, Dialog.ModalityType.APPLICATION_MODAL);
    }

    public static void showDialog(JDialog dialog, Component parent) {
        dialog.pack();
        dialog.setSize(new Dimension(Math.max(dialog.getWidth(), 420), dialog.getHeight()));
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    // PAYMENT FIELD: always "PHP <whole number>.<2 decimals>", editable, never empty, never negative
    public static void setupPaymentField(JTextField field) {
        field.setText("PHP 0.00");
        ((javax.swing.text.AbstractDocument) field.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String text, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                replace(fb, offset, 0, text, attr);
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws javax.swing.text.BadLocationException {
                replace(fb, offset, length, "", null);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
                final String prefix = "PHP ";
                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                int dotIdx = current.indexOf('.');
                String input = text == null ? "" : text;

                // typing "." jumps the cursor to the decimals
                if (input.equals(".")) {
                    field.setCaretPosition(Math.min(dotIdx + 1, current.length()));
                    return;
                }

                boolean fullAmount = input.contains(".") && input.length() > 1;   // pasted amount like 100.50
                String candidate;
                int caretE = 0;

                if (fullAmount) {
                    candidate = prefix + input.replace("PHP", "").replace(",", "").trim();
                } else {
                    String typed = input.replaceAll("[^0-9]", "");
                    int start = Math.max(offset, prefix.length());
                    int end = Math.max(offset + length, prefix.length());
                    String currentInt = current.substring(prefix.length(), dotIdx);

                    if (currentInt.equals("0") && !typed.isEmpty() && start <= dotIdx) {
                        start = prefix.length();
                        end = prefix.length() + 1;
                    }

                    boolean removesDot = start <= dotIdx && end > dotIdx;
                    candidate = current.substring(0, start) + typed + (removesDot ? "." : "")
                            + current.substring(Math.min(end, current.length()));
                    caretE = start + typed.length();
                }

                String body = candidate.substring(prefix.length());
                int dot = body.indexOf('.');
                String rawInt = (dot < 0 ? body : body.substring(0, dot)).replaceAll("[^0-9]", "");
                String rawDec = dot < 0 ? "" : body.substring(dot + 1).replaceAll("[^0-9]", "");
                String cleanInt = rawInt.replaceFirst("^0+(?!$)", "");

                if (cleanInt.isEmpty()) {
                    cleanInt = "0";
                }
                if (cleanInt.length() > 9) {
                    return;
                }

                String newText = prefix + cleanInt + "." + (rawDec + "00").substring(0, 2);
                int caret;
                if (fullAmount) {
                    caret = newText.length();
                } else if (rawInt.isEmpty()) {
                    caret = prefix.length() + 1;
                } else {
                    caret = Math.max(prefix.length(), caretE - (rawInt.length() - cleanInt.length()));
                }

                fb.replace(0, fb.getDocument().getLength(), newText, null);
                field.setCaretPosition(Math.min(caret, newText.length()));
            }
        });
    }
}