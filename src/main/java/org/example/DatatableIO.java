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

import org.example.Datatable;

public class DatatableIO {
    
    public static void readCSV(String path, Datatable table) {
        table.setColumns(new String[] {"manufacturer", "screen size", "screen type", "touchscreen", "processor name", "processor cores", "clock speed", "ram", "disk size", "disk type", "graphics card name", "graphics card memory", "os", "disc reader"});
        try {
            Scanner scanner = new Scanner(new File(path));
            String line = scanner.nextLine();
            line = scanner.nextLine();
            while (scanner.hasNextLine()) {
                String[] row = line.split(";");
                table.addRow(row, false);
                line = scanner.nextLine();
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void saveCSV(String path, Datatable table) {
        try {
            File file = new File(path);
            FileWriter writer = new FileWriter(file);
            for (int i = 0; i < table.getModel().getColumnCount(); i++) {
                writer.write(table.getModel().getColumnName(i) + ",");
            }

            writer.write("\n");
            for (int i = 0; i < table.getModel().getRowCount(); i++) {
                for (int j = 0; j < table.getModel().getColumnCount(); j++) {
                    writer.write(table.getModel().getValueAt(i, j).toString() + ",");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static void readXML(String path, Datatable table) {
        table.setColumns(new String[] {"manufacturer", "screen size", "screen type", "touchscreen", "processor name", "processor cores", "clock speed", "ram", "disk size", "disk type", "graphics card name", "graphics card memory", "os", "disc reader"});

        try {
            File xmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

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

                    table.addRow(new String[] {manufacturer, screen_size, screen_type, screen_touchscreen, processor_name, processor_physical_cores, processor_clock_speed, ram, disc_storage, disc_type, graphic_card_name, graphic_card_memory, os, disc_reader}, false);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void saveXML(String path, Datatable table) {
        try {
            File file = new File(path);
            FileWriter writer = new FileWriter(file);
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<laptops>\n");
            // for each row in the table model write a laptop tag with the attributes of the row
            for (int i = 0; i < table.getModel().getRowCount(); i++) {
                writer.write("\t<laptop>\n");
                writer.write("\t\t<manufacturer>" + table.getModel().getValueAt(i, 0) + "</manufacturer>\n");
                writer.write("\t\t<screen>\n");
                writer.write("\t\t\t<size>" + table.getModel().getValueAt(i, 1) + "</size>\n");
                writer.write("\t\t\t<type>" + table.getModel().getValueAt(i, 2) + "</type>\n");
                writer.write("\t\t\t<touchscreen>" + table.getModel().getValueAt(i, 3) + "</touchscreen>\n");
                writer.write("\t\t</screen>\n");
                writer.write("\t\t<processor>\n");
                writer.write("\t\t\t<name>" + table.getModel().getValueAt(i, 4) + "</name>\n");
                writer.write("\t\t\t<physical_cores>" + table.getModel().getValueAt(i, 5) + "</physical_cores>\n");
                writer.write("\t\t\t<clock_speed>" + table.getModel().getValueAt(i, 6) + "</clock_speed>\n");
                writer.write("\t\t</processor>\n");
                writer.write("\t\t<ram>" + table.getModel().getValueAt(i, 7) + "</ram>\n");
                writer.write("\t\t<disc>\n");
                writer.write("\t\t\t<storage>" + table.getModel().getValueAt(i, 8) + "</storage>\n");
                writer.write("\t\t\t<type>" + table.getModel().getValueAt(i, 9) + "</type>\n");
                writer.write("\t\t</disc>\n");
                writer.write("\t\t<graphic_card>\n");
                writer.write("\t\t\t<name>" + table.getModel().getValueAt(i, 10) + "</name>\n");
                writer.write("\t\t\t<memory>" + table.getModel().getValueAt(i, 11) + "</memory>\n");
                writer.write("\t\t</graphic_card>\n");
                writer.write("\t\t<os>" + table.getModel().getValueAt(i, 12) + "</os>\n");
                writer.write("\t\t<disc_reader>" + table.getModel().getValueAt(i, 13) + "</disc_reader>\n");
                writer.write("\t</laptop>\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }



}
