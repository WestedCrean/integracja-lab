package org.example;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    static Connection conn = null;

//    DatabaseConnection() {
//        initializeConnection();
//    }
    static public void  initializeConnection() {
        String url = "jdbc:sqlite:integracja.db";
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS laptops (\n"
                + "	manufacturer text NOT NULL,\n"
                + "	screen_size real NOT NULL,\n"
                + "	screen_type text NOT NULL,\n"
                + "	touchscreen text NOT NULL,\n"
                + "	processor_name text NOT NULL,\n"
                + "	processor_cores integer NOT NULL,\n"
                + "	clock_speed integer NOT NULL,\n"
                + "	ram integer NOT NULL,\n"
                + "	disk_size integer NOT NULL,\n"
                + "	disk_type text NOT NULL,\n"
                + "	graphics_card_name text NOT NULL,\n"
                + "	graphics_card_memory integer NOT NULL,\n"
                + "	os text NOT NULL,\n"
                + "	disc_reader text NOT NULL\n"
                + ");";

        try (Statement stmt = this.conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<String[]> getAllLaptops() {
        String sql = "SELECT * FROM laptops";
        List<String[]> laptops = new ArrayList<>();
        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String[] laptop = new String[14];
                laptop[0] = rs.getString("manufacturer");
                laptop[1] = rs.getString("screen_size");
                laptop[2] = rs.getString("screen_type");
                laptop[3] = rs.getString("touchscreen");
                laptop[4] = rs.getString("processor_name");
                laptop[5] = rs.getString("processor_cores");
                laptop[6] = rs.getString("clock_speed");
                laptop[7] = rs.getString("ram");
                laptop[8] = rs.getString("disk_size");
                laptop[9] = rs.getString("disk_type");
                laptop[10] = rs.getString("graphics_card_name");
                laptop[11] = rs.getString("graphics_card_memory");
                laptop[12] = rs.getString("os");
                laptop[13] = rs.getString("disc_reader");
                laptops.add(laptop);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            createTable();
        }
        return laptops;
    }

    public void dropTable() {
        String sql = "DROP TABLE laptops";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        this.createTable();
    }

    public void insertLaptops(Datatable dataTable) {
        this.dropTable();
        DefaultTableModel model = dataTable.getModel();
        String sql = "INSERT INTO laptops(manufacturer, screen_size, screen_type, touchscreen, processor_name, processor_cores, clock_speed, ram, disk_size, disk_type, graphics_card_name, graphics_card_memory, os, disc_reader) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        // insert all rows from model to database
        for (int i = 0; i < model.getRowCount(); i++) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, model.getValueAt(i, 0).toString());
                pstmt.setString(2, model.getValueAt(i, 1).toString());
                pstmt.setString(3, model.getValueAt(i, 2).toString());
                pstmt.setString(4, model.getValueAt(i, 3).toString());
                pstmt.setString(5, model.getValueAt(i, 4).toString());
                pstmt.setString(6, model.getValueAt(i, 5).toString());
                pstmt.setString(7, model.getValueAt(i, 6).toString());
                pstmt.setString(8, model.getValueAt(i, 7).toString());
                pstmt.setString(9, model.getValueAt(i, 8).toString());
                pstmt.setString(10, model.getValueAt(i, 9).toString());
                pstmt.setString(11, model.getValueAt(i, 10).toString());
                pstmt.setString(12, model.getValueAt(i, 11).toString());
                pstmt.setString(13, model.getValueAt(i, 12).toString());
                pstmt.setString(14, model.getValueAt(i, 13).toString());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
