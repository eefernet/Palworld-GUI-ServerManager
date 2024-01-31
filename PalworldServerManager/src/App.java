import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;


public class App {

    static Scanner scnr = new Scanner(System.in);
    static File file;
    static JFrame frame;
    static String rawString="";
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
            

            while (fileReader.hasNextLine()) {
                String setting = fileReader.nextLine();
                rawString+= setting;
                
            }
            System.out.println(rawString);

            rawString = extractSubstringBetweenParentheses(rawString, "(", ")");
            System.out.println("updated RAW STRING : "+rawString);
            
            fileReader = new Scanner(rawString).useDelimiter(",");
            while(fileReader.hasNext()){
                String string = fileReader.next();
                System.out.println("File reader has parsed : " + string);
                settings.add(string);
            }

            for (String string : settings) {
                String[] values = string.split("=");
                Settings settingsObject = new Settings(values[0], values[1], Settings.SettingType.Other);
                settingsObjects.add(settingsObject);
            }

            System.out.println("COUNT OF SETTINGS OBJECTS ARRAY : "+ settingsObjects.size());
            for (Settings objec : settingsObjects) {
                System.out.println("Key : " + objec.getSettingName() + "   value: " + objec.getSettingValue());
            }



            System.out.println("SETUPDATA METHOD : "+settings.size());
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
        JPanelViewController helpPanel = new JPanelViewController(JPanelViewController.PanelType.Palsettings);

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
        tabbedPane.add("Help", helpPanel);
        
        
        frame.setSize(480,720);
        frame.add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.setBounds(0, 0, frame.getWidth(), frame.getHeight()-100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    private static void processObjects(){
        if(fileLoaded){
            for(String setting : settings){
                System.out.println(setting);
                String[] settingArray = setting.trim().split("=");
                
                Settings newSetting = new Settings(settingArray[0], settingArray[1], Settings.SettingType.ServerSettings);
                settingsObjects.add(newSetting);
                System.out.println(settingsObjects.size());
            }

            for (Settings setting: settingsObjects) {
                System.out.println(setting.getSettingName() + " ::: " + setting.getSettingValue());                
            }
            
        }
    }

    public static String extractSubstringBetweenParentheses(String inputString, String index1, String index2) {
        int startIndex = inputString.indexOf(index1);
        int endIndex = inputString.indexOf(index2);

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return inputString.substring(startIndex + 1, endIndex);
        } else {
            return null;
        }
    }

    private static void saveFile(){
        System.out.println("Saving file......");
    }
}
