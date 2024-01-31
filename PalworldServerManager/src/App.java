import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;


public class App {

    static Scanner scnr = new Scanner(System.in);
    static File file;
    static JFrame frame;
    static ArrayList<String> settings = new ArrayList<String>();
    static ArrayList<Settings> settingsObjects = new ArrayList<Settings>();

    public static ArrayList<Settings> getSettingsObjects() {
        return settingsObjects;
    }

    public static ArrayList<String> getSettings() {
        return settings;
    }

    private static boolean fileLoaded = false;
    private static JTabbedPane tabbedPane;

    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to Palword Server Manager");
        setUpData();
        processObjects();
        setUpWindow();
    }

    private static void setUpData(){
        try {
            URL path = App.class.getResource("settings.txt");
            file = new File(path.getFile());
            Scanner fileReader = new Scanner(file);
            while(fileReader.hasNextLine()){
                settings.add(fileReader.nextLine());
            }
            fileReader.close();
            fileLoaded = true;
            settings.remove(0);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            fileLoaded = false;
        }
    }

    private static void setUpWindow(){
        frame = new JFrame("Palword Server Manager");
        frame.setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(0, 0, 480, 620);

        JPanelViewController serverPanel = new JPanelViewController(JPanelViewController.PanelType.ServerSettings);
        JPanelViewController palsPanel = new JPanelViewController(JPanelViewController.PanelType.Palsettings);

        JPanel saveButtonPanel = new JPanel();
        saveButtonPanel.setBounds(tabbedPane.getWidth(), tabbedPane.getHeight(), frame.getWidth(), 100);

        saveButtonPanel.setBackground(Color.RED);
        saveButtonPanel.setLayout(new FlowLayout());
        

        JButton saveButton = new JButton("Save");
        saveButtonPanel.add(saveButton);
        frame.add(saveButtonPanel, BorderLayout.SOUTH);
        
        saveButton.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e){
                saveFile();
            }
        });

        tabbedPane.add("Server Settings", serverPanel);
        tabbedPane.add("Pal Settings", palsPanel);
        
        
        frame.setSize(480,720);
        frame.add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.setBounds(0, 0, frame.getWidth(), frame.getHeight()-100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    private static void processObjects(){
        if(fileLoaded){
            for(String setting : settings){
                String[] settingArray = setting.split("=");
                
                
                if(settingArray[0].toLowerCase().contains("difficulty") || setting.toLowerCase().contains("maxplayers") || setting.toLowerCase().contains("rcon")|| setting.toLowerCase().contains("guild") || setting.toLowerCase().contains("serverame") || setting.toLowerCase().contains("public")|| setting.toLowerCase().contains("port") || setting.toLowerCase().contains("serverpassword")|| setting.toLowerCase().contains("admin")){
                    Settings newSetting = new Settings(settingArray[0], settingArray[1], Settings.SettingType.ServerSettings);
                    settingsObjects.add(newSetting);
                }

                if(settingArray[0].toLowerCase().contains("pal")){
                    Settings newSetting = new Settings(settingArray[0], settingArray[1], Settings.SettingType.PalSettings);
                    settingsObjects.add(newSetting);
                }

                else{
                    Settings newSetting = new Settings(settingArray[0], settingArray[1], Settings.SettingType.Other);
                    settingsObjects.add(newSetting);
                }

                System.out.println("Count : " + settingsObjects.size());
            }

            for(Settings settingObject : settingsObjects){
                System.out.println(settingObject.getSettingName() + " " + settingObject.getSettingValue() + " " + settingObject.getType());
            }
        }
    }

    private static void saveFile(){
        System.out.println("Saving file......");
    }
}
