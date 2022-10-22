package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.FileWriter;
import java.sql.*;
import java.util.Scanner;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import org.javatuples.Pair;

public class Main {
    public static Pair<JTable, DefaultTableModel> readCSV(String path) {
        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel();

        model.setColumnIdentifiers(new String[] {"manufacturer", "screen size", "screen type", "touchscreen", "processor name", "processor cores", "clock speed", "ram", "disk size", "disk type", "graphics card name", "graphics card memory", "os", "disc reader"});
        table.setModel(model);
        
        try {
            Scanner scanner = new Scanner(new File(path));
            String line = scanner.nextLine();

            line = scanner.nextLine();

            while (scanner.hasNextLine()) {
                String[] rows = line.split(";");
                model.addRow(rows);
                line = scanner.nextLine();
            }
            scanner.close();
            table.setModel(model);
        } catch (Exception e) {
            System.out.println(e);
        }

        return new Pair<JTable, DefaultTableModel>(table, model);
    }

    public static void saveCSV(String path, DefaultTableModel model) {
        try {
            File file = new File(path);
            FileWriter writer = new FileWriter(file);
            for (int i = 0; i < model.getColumnCount(); i++) {
                writer.write(model.getColumnName(i) + ",");
            }

            writer.write("\n");
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    writer.write(model.getValueAt(i, j).toString() + ",");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static Pair<JTable, DefaultTableModel> readXML(String path) {
        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel();

        try {
            File xmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();
            model.setColumnIdentifiers(new String[] {"manufacturer", "screen size", "screen type", "touchscreen", "processor name", "processor cores", "clock speed", "ram", "disk size", "disk type", "graphics card name", "graphics card memory", "os", "disc reader"});

            NodeList laptops = doc.getElementsByTagName("laptop");

            for (int i = 0; i < laptops.getLength(); i++) {
                Node laptop = laptops.item(i);
                if (laptop.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) laptop;

                    // non nested attributes
                    String manufacturer = eElement.getElementsByTagName("manufacturer").item(0).getTextContent();
                    String ram = eElement.getElementsByTagName("ram").item(0).getTextContent();
                    String os = eElement.getElementsByTagName("os").item(0).getTextContent();
                    String disc_reader = eElement.getElementsByTagName("disc_reader").item(0).getTextContent();

                    // nested attributes
                    String screen_size = eElement.getElementsByTagName("screen").item(0).getChildNodes().item(1).getTextContent();
                    String screen_type = eElement.getElementsByTagName("screen").item(0).getChildNodes().item(3).getTextContent();
                    String screen_touchscreen = eElement.getElementsByTagName("screen").item(0).getChildNodes().item(5).getTextContent();

                    String processor_name = eElement.getElementsByTagName("processor").item(0).getChildNodes().item(1).getTextContent();
                    String processor_physical_cores = eElement.getElementsByTagName("processor").item(0).getChildNodes().item(3).getTextContent();
                    String processor_clock_speed = eElement.getElementsByTagName("processor").item(0).getChildNodes().item(5).getTextContent();

                    String disc_storage = eElement.getElementsByTagName("disc").item(0).getChildNodes().item(1).getTextContent();
                    String disc_type = eElement.getElementsByTagName("disc").item(0).getChildNodes().item(3).getTextContent();

                    String graphic_card_name = eElement.getElementsByTagName("graphic_card").item(0).getChildNodes().item(1).getTextContent();
                    String graphic_card_memory = eElement.getElementsByTagName("graphic_card").item(0).getChildNodes().item(3).getTextContent();

                    model.addRow(new String[] {manufacturer, screen_size, screen_type, screen_touchscreen, processor_name, processor_physical_cores, processor_clock_speed, ram, disc_storage, disc_type, graphic_card_name, graphic_card_memory, os, disc_reader});
                }
            }
            table.setModel(model);
        } catch (Exception e) {
            System.out.println(e);
        }
        return new Pair<JTable, DefaultTableModel>(table, model);
    }

    public static void saveXML(String path, DefaultTableModel model) {
        try {
            File file = new File(path);
            FileWriter writer = new FileWriter(file);
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<laptops>\n");
            // for each row in the table model write a laptop tag with the attributes of the row
            for (int i = 0; i < model.getRowCount(); i++) {
                writer.write("\t<laptop>\n");
                writer.write("\t\t<manufacturer>" + model.getValueAt(i, 0) + "</manufacturer>\n");
                writer.write("\t\t<screen>\n");
                writer.write("\t\t\t<size>" + model.getValueAt(i, 1) + "</size>\n");
                writer.write("\t\t\t<type>" + model.getValueAt(i, 2) + "</type>\n");
                writer.write("\t\t\t<touchscreen>" + model.getValueAt(i, 3) + "</touchscreen>\n");
                writer.write("\t\t</screen>\n");
                writer.write("\t\t<processor>\n");
                writer.write("\t\t\t<name>" + model.getValueAt(i, 4) + "</name>\n");
                writer.write("\t\t\t<physical_cores>" + model.getValueAt(i, 5) + "</physical_cores>\n");
                writer.write("\t\t\t<clock_speed>" + model.getValueAt(i, 6) + "</clock_speed>\n");
                writer.write("\t\t</processor>\n");
                writer.write("\t\t<ram>" + model.getValueAt(i, 7) + "</ram>\n");
                writer.write("\t\t<disc>\n");
                writer.write("\t\t\t<storage>" + model.getValueAt(i, 8) + "</storage>\n");
                writer.write("\t\t\t<type>" + model.getValueAt(i, 9) + "</type>\n");
                writer.write("\t\t</disc>\n");
                writer.write("\t\t<graphic_card>\n");
                writer.write("\t\t\t<name>" + model.getValueAt(i, 10) + "</name>\n");
                writer.write("\t\t\t<memory>" + model.getValueAt(i, 11) + "</memory>\n");
                writer.write("\t\t</graphic_card>\n");
                writer.write("\t\t<os>" + model.getValueAt(i, 12) + "</os>\n");
                writer.write("\t\t<disc_reader>" + model.getValueAt(i, 13) + "</disc_reader>\n");
                writer.write("\t</laptop>\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:/Users/wflis/Desktop/integracja.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Integracja systemów - Lab1 Wiktor Flis");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);


        final DefaultTableModel[] model = {new DefaultTableModel()};
        model[0].setColumnIdentifiers(new String[] {"manufacturer", "screen size", "screen type", "touchscreen", "processor name", "processor cores", "clock speed", "ram", "disk size", "disk type", "graphics card name", "graphics card memory", "os", "disc reader"});
        String[] columnNames = new String[model[0].getColumnCount()];

        //Connection conn = connect();

        JPanel formPanel = new JPanel();
        JPanel panel = new JPanel();

        // empty contents
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        final JTable table = new JTable(model[0]);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);


        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.removeAll();
                // empty contents
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                final JTable table = new JTable(model[0]);
                table.setPreferredScrollableViewportSize(new Dimension(500, 70));
                JScrollPane scrollPane = new JScrollPane(table);
                panel.add(scrollPane);

                panel.revalidate();
                panel.repaint();
                model[0] = new DefaultTableModel();
                model[0].setColumnIdentifiers(new String[] {"manufacturer", "screen size", "screen type", "touchscreen", "processor name", "processor cores", "clock speed", "ram", "disk size", "disk type", "graphics card name", "graphics card memory", "os", "disc reader"});
                formPanel.revalidate();
                formPanel.repaint();
            }
        });

        /*
        JButton loadButton = new JButton("Load from database");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JTable table = new JTable();
                    model[0] = new DefaultTableModel();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM komputery");
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnsNumber = rsmd.getColumnCount();
                    String[] columnNames = new String[columnsNumber];
                    for (int i = 0; i < columnsNumber; i++) {
                        columnNames[i] = rsmd.getColumnName(i + 1);
                    }
                    model[0].setColumnIdentifiers(columnNames);
                    while (rs.next()) {
                        Object[] row = new Object[columnsNumber];
                        for (int i = 0; i < columnsNumber; i++) {
                            row[i] = rs.getObject(i + 1);
                        }
                        model[0].addRow(row);
                    }
                    table.setModel(model[0]);
                    panel.add(table);
                    panel.revalidate();
                    panel.repaint();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });
        */
        JButton saveCSVButton = new JButton("Save as CSV");
        saveCSVButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
                fileChooser.setDialogTitle("Specify a file to save");
                int userSelection = fileChooser.showSaveDialog(frame);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
                    // save the table in csv file using saveCSV function
                    saveCSV(fileToSave.getAbsolutePath(), model[0]);
                }
            }
        });

        JButton saveXMLButton = new JButton("Save as XML");
        saveXMLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("XML files", "xml"));
                fileChooser.setDialogTitle("Specify a file to save");
                int userSelection = fileChooser.showSaveDialog(frame);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
                    saveXML(fileToSave.getAbsolutePath(), model[0]);
                }
            }
        });

        JButton csvButton = new JButton("Read CSV File");
        csvButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearButton.doClick();
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    Pair<JTable, DefaultTableModel> csvResult = readCSV(selectedFile.getAbsolutePath());
                    model[0] = csvResult.getValue1();
                    panel.add(new JScrollPane(csvResult.getValue0()));
                    panel.revalidate();

                }
            }
        });

        JButton xmlButton = new JButton("Read XML File");
        xmlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearButton.doClick();
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    Pair<JTable, DefaultTableModel> xmlResult = readXML(selectedFile.getAbsolutePath());
                    model[0] = xmlResult.getValue1();
                    panel.add(new JScrollPane(xmlResult.getValue0()));
                    panel.revalidate();
                }
            }
        });

        // panel for adding new rows

        JTextField[] textFields = new JTextField[model[0].getColumnCount()];
        for (int i = 0; i < model[0].getColumnCount(); i++) {
            columnNames[i] = model[0].getColumnName(i);
            JLabel label = new JLabel(columnNames[i]);
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
            formPanel.add(label);
            textFields[i] = new JTextField();
            formPanel.add(textFields[i]);
        }
        JButton addButton = new JButton("Add new row");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] rowValues = new String[model[0].getColumnCount()];
                for (int i = 0; i < model[0].getColumnCount(); i++) {
                    rowValues[i] = textFields[i].getText();
                }
                model[0].addRow(rowValues);
                for (int i = 0; i < model[0].getColumnCount(); i++) {
                    textFields[i].setText("");
                }
                panel.revalidate();
                panel.repaint();
            }
        });
        formPanel.add(addButton);

        formPanel.setLayout(new GridLayout(0, 2));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(Color.LIGHT_GRAY);
        JPanel ribbon = new JPanel();
        ribbon.add(csvButton);
        ribbon.add(xmlButton);
        ribbon.add(clearButton);
        ribbon.add(saveCSVButton);
        ribbon.add(saveXMLButton);
        frame.add(ribbon, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(formPanel, BorderLayout.SOUTH);

        formPanel.setVisible(true);
        ribbon.setVisible(true);
        panel.setVisible(true);
        frame.setVisible(true);
    }
}