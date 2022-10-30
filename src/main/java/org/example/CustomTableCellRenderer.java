package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    private Datatable datatable;

    public CustomTableCellRenderer(Datatable datatable) {
        this.datatable = datatable;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {
        Component comp = super.getTableCellRendererComponent(table,
                value, isSelected, hasFocus, row, column);
        if (this.datatable.isDuplicate(row)) {
            comp.setBackground(Color.RED);
        } else if (this.datatable.isModified(row)) {
            comp.setBackground(Color.WHITE);
        } else {
            comp.setBackground(Color.GRAY);
        }
        return comp;
    }
}