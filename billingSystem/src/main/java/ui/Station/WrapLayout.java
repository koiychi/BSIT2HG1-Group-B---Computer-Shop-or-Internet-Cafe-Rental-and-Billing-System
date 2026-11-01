/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.Station;

/**
 *
 * @author Gian Carlo
 */
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout; 
import java.awt.Insets;

public class WrapLayout extends FlowLayout {
      public WrapLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            int width = target.getWidth();

            if (width <= 0 && target.getParent() != null) {
                width = target.getParent().getWidth();
            }

            if (width <= 0) {
                width = 800;
            }

            Insets insets = target.getInsets();
            int maxWidth = width - insets.left - insets.right
                    - getHgap() * 2;

            int rowWidth = 0;
            int rowHeight = 0;
            int totalHeight = insets.top + insets.bottom
                    + getVgap() * 2;

            for (Component component : target.getComponents()) {
                if (!component.isVisible()) {
                    continue;
                }

                Dimension size = component.getPreferredSize();

                if (rowWidth > 0
                        && rowWidth + getHgap() + size.width > maxWidth) {
                    totalHeight += rowHeight + getVgap();
                    rowWidth = 0;
                    rowHeight = 0;
                }

                if (rowWidth > 0) {
                    rowWidth += getHgap();
                }

                rowWidth += size.width;
                rowHeight = Math.max(rowHeight, size.height);
            }

            totalHeight += rowHeight;

            return new Dimension(width, totalHeight);
        }
    }
}
