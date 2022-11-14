package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class Datatable {
    public JPanel panel;
    public JTable table;
    public DefaultTableModel model;

    public List<Boolean> duplicates;
    public List<Boolean> modified;
    public Datatable() {
        this.table = new JTable();
        this.model = new DefaultTableModel();
        this.panel = new JPanel();
        this.setColumns(new String[] {"manufacturer", "screen size", "screen type", "touchscreen", "processor name", "processor cores", "clock speed", "ram", "disk size", "disk type", "graphics card name", "graphics card memory", "os", "disc reader"});
        this.duplicates = new ArrayList<>();
        this.modified = new ArrayList<>();

        this.model.addTableModelListener(new CustomTableChangeListener(this));
    }

    public JPanel getPanel() {
        return this.panel;
    }

    public DefaultTableModel getModel() {
        return this.model;
    }

    // setColumns() sets the columns of the table
    public void setColumns(String[] columns) {
        this.model.setColumnIdentifiers(columns);
        this.table.setModel(this.model);
    }

    // get column names
    public String[] getColumns() {
        String[] columns = new String[this.model.getColumnCount()];
        for (int i = 0; i < this.model.getColumnCount(); i++) {
            columns[i] = this.model.getColumnName(i);
        }
        return columns;
    }

    // get column count
    public int getColumnCount() {
        return this.model.getColumnCount();
    }

    // get row count
    public int getRowCount() {
        return this.model.getRowCount();
    }

    // repaint table
    public void repaintTable() {
        this.panel.removeAll();
        // empty contents
        this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
        this.panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.table = new JTable(this.model);
        this.table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        this.table.setDefaultRenderer(Object.class, new CustomTableCellRenderer(this));

        JScrollPane scrollPane = new JScrollPane(this.table);
        this.panel.add(scrollPane);
        this.panel.revalidate();
        this.panel.repaint();
    }

    

    // clear model
    public void clear() {
        this.model = new DefaultTableModel();
        this.model.addTableModelListener(new CustomTableChangeListener(this));
        this.duplicates = new ArrayList<>();
        this.modified = new ArrayList<>();
        this.setColumns(new String[] {"manufacturer", "screen size", "screen type", "touchscreen", "processor name", "processor cores", "clock speed", "ram", "disk size", "disk type", "graphics card name", "graphics card memory", "os", "disc reader"});
        repaintTable();
    }

    // set model
    public void setModel(DefaultTableModel model) {
        this.model = model;
        this.model.addTableModelListener(new CustomTableChangeListener(this));
        repaintTable();
    }

    // get Duplicates
    public List<Boolean> getDuplicates() {
        return this.duplicates;
    }

    // check if ith element is duplicate
    public boolean isDuplicate(int i) {
        // check if i is in range of duplicates
        if (i < 0 || i < this.duplicates.size()) {
            return this.duplicates.get(i);
        }
        return false;
    }

    // set ith element as duplicate
    public void setDuplicate(int i) {
        // check if i is in range of duplicates
        if (i < 0 || i < this.duplicates.size()) {
            this.duplicates.set(i, true);
        }
    }

    // check if ith element is modified
    public boolean isModified(int i) {
        if (i < 0 || i < this.modified.size()) {
            return this.modified.get(i);
        }
        return false;
    }

    // set ith element as modified
    public void setModified(int i) {
        if (i < 0 || i < this.modified.size()) {
            this.modified.set(i, true);
        }
    }

    public boolean compareRows(String[] row1, String[] row2) {
        // check if both rows have the same number of columns
        if (row1.length != row2.length) {
            return false;
        }
        // for each column, check if any of them are different, if so, return false
        for (int i = 0; i < row1.length; i++) {
            if (!row1[i].equals(row2[i])) {
                return false;
            }
        }
        return true;
    }

    // check for duplicate - function that takes a String[] row and checks if exactly the same row exists in the table. if no, return null, if yes return row index
    public Integer checkForDuplicate(String[] row) {
        // for each row in the table, compare if it matches with the row passed as parameter
        for (int i = 0; i < this.model.getRowCount(); i++) {
            // get row
            String[] row2 = new String[this.model.getColumnCount()];
            for (int j = 0; j < this.model.getColumnCount(); j++) {
                row2[j] = (String) this.model.getValueAt(i, j);
            }
            // compare row
            if (compareRows(row, row2)) {
                return i;
            }
        }
        return null;
    }
    

    // colour row - function that takes a row index and adds red background to given row index
    public void colourRow(int row) {
        this.table.setRowSelectionAllowed(true);
        this.table.setRowSelectionInterval(row, row);
        this.table.setSelectionBackground(Color.RED);
    }

    public void addRow(String[] row) {
        this.addRow(row, true);
    }

    public void addRow(String[] row, boolean check_if_duplicate) {
        System.out.println("Called addRow with check_if_duplicate: " + check_if_duplicate);
        Integer i = checkForDuplicate(row);
        if (i != null && check_if_duplicate) {
            this.setDuplicate(i);
            colourRow(i);
        } else {
            this.duplicates.add(false);
            this.modified.add(false);
            this.model.addRow(row);
        }
    }
}
