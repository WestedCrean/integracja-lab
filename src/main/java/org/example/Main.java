package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.FileWriter;
import java.sql.*;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import org.example.Datatable;
import org.example.DatatableIO;
import org.example.DatabaseConnection;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Integracja systemów - Lab1 Wiktor Flis");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        Datatable mainTable = new Datatable();
        DatabaseConnection dc = new DatabaseConnection();

        DatabaseConnection.initializeConnection();
        JPanel formPanel = new JPanel();

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainTable.clear();
                mainTable.repaintTable();
                formPanel.revalidate();
                formPanel.repaint();
            }
        });

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
                    DatatableIO.saveCSV(fileToSave.getAbsolutePath(), mainTable);
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
                    DatatableIO.saveXML(fileToSave.getAbsolutePath(), mainTable);
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
                fileChooser.setCurrentDirectory(new File("C:\\Users\\wflis\\IdeaProjects\\integracja-lab"));
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    DatatableIO.readCSV(selectedFile.getAbsolutePath(), mainTable);
                    mainTable.repaintTable();
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
                    DatatableIO.readXML(selectedFile.getAbsolutePath(), mainTable);
                    mainTable.repaintTable();
                }
            }
        });

        // panel for adding new rows

        JTextField[] textFields = new JTextField[mainTable.getColumnCount()];
        for (int i = 0; i < mainTable.getColumnCount(); i++) {
            JLabel label = new JLabel(mainTable.getModel().getColumnName(i));
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
            formPanel.add(label);
            textFields[i] = new JTextField();
            formPanel.add(textFields[i]);
        }

        JButton addButton = new JButton("Add new row");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] rowValues = new String[mainTable.getColumnCount()];
                for (int i = 0; i < mainTable.getColumnCount(); i++) {
                    rowValues[i] = textFields[i].getText();
                }
                mainTable.addRow(rowValues);
                for (int i = 0; i < mainTable.getColumnCount(); i++) {
                    textFields[i].setText("");
                }
                mainTable.repaintTable();
            }
        });

        JButton readDBButton = new JButton("Read from database");
        readDBButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                List<String[]> laptops = dc.getAllLaptops();

                // for String[] laptop in latops add row to mainTable
                for (Object laptop : laptops) {
                    String[] rowValues = (String[]) laptop;
                    mainTable.addRow(rowValues);
                }
                mainTable.repaintTable();
                System.out.println("Duplicates: " + mainTable.getDuplicates());

                // count number of elements in mainTable.getDuplicates()
                int duplicates = 0;
                for (int i = 0; i < mainTable.getDuplicates().size(); i++) {
                    if (mainTable.getDuplicates().get(i) != null) {
                        duplicates++;
                    }
                }
                System.out.println("Row count: " + mainTable.getRowCount());
                System.out.println("Duplicates state count: " + duplicates);
            }
        });

        JButton saveDBButton = new JButton("Save to database");
        saveDBButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dc.insertLaptops(mainTable);
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

        ribbon.add(readDBButton);
        ribbon.add(saveDBButton);
        frame.add(ribbon, BorderLayout.NORTH);
        frame.add(mainTable.getPanel(), BorderLayout.CENTER);
        frame.add(formPanel, BorderLayout.SOUTH);

        formPanel.setVisible(true);
        ribbon.setVisible(true);
        mainTable.getPanel().setVisible(true);
        frame.setVisible(true);
    }
}