import java.awt.event.ActionEvent;
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
    static File file = null;
    static Scanner fileReader;
    static JFrame frame;
    static String rawString="";
    static ArrayList<String> settings = new ArrayList<String>();
    static ArrayList<Settings> settingsObjects = new ArrayList<Settings>();

    static JPanelViewController serverPanel;

    private static boolean fileLoaded = false;
    private static JTabbedPane tabbedPane;

    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to Palword Server Manager");
        setUpData();
        setUpWindow();
    }

    private static void setUpData() throws FileNotFoundException {
        //Mark: Try to load the default file resource to grab settings

        if(file == null){
            System.out.println("File is NULL!!!!!");
            try {
                URL path = App.class.getResource("settings.txt");
                file = new File(path.getFile());
                fileReader = new Scanner(file);


                //Catch file not found exception
            } catch (FileNotFoundException e) {
                System.out.println("File not found, seems like you dont have the right ini file... :(");
                fileLoaded = false;
            }
        }else{
            System.out.println("File is not NULL!!!!!");
            fileReader = new Scanner(file);
        }


        //Read every line and throw it into a string
        while (fileReader.hasNextLine()) {
            String setting = fileReader.nextLine();
            rawString += setting;
        }
        //TODO: Remove this test print out later
        System.out.println(rawString);

        //Get just the stuff we need to worry about, which is in between ()
        rawString = extractSubstringBetweenParentheses(rawString, '(', ')');

        //TODO: Remove this test print later, displays the raw string after removing the substring
        System.out.println("updated RAW STRING : "+rawString);

        fileReader = new Scanner(rawString).useDelimiter(",");
        while(fileReader.hasNext()){
            String string = fileReader.next();

            //TODO: remove temp test print out later once things are good
            System.out.println("File reader has parsed : " + string);
            settings.add(string);
        }
        //Mark: Create setting objects for each and every setting loaded into memory
        //TODO: Actually label them according to what kind of setting they are using the enum
        for (String string : settings) {
            //Split the string by "=" to get the setting name and value
            String[] values = string.split("=");
            //Create settingsObject
            Settings settingsObject = new Settings(values[0], values[1], Settings.SettingType.Other);
            //Add the new settingsObject to the settingsObjects ArrayList
            settingsObjects.add(settingsObject);
        }
            /*
            TODO: Remove test code to validate array construction later
            System.out.println("COUNT OF SETTINGS OBJECTS ARRAY : "+ settingsObjects.size());
            for (Settings objec : settingsObjects) {
                System.out.println("Key : " + objec.getSettingName() + "   value: " + objec.getSettingValue());
            }

            System.out.println("SETUPDATA METHOD : "+settings.size());
             */
        //Close file reader cause im a good boi
        fileReader.close();
        fileLoaded = true;
        settings.remove(0);
    }

    private static File getFile() throws NoSuchFileException, FileNotFoundException{
        System.out.println("Loading file from file explorer...");

        JFileChooser fileChooser = new JFileChooser();
        int response = fileChooser.showOpenDialog(null);
        fileChooser.setDialogTitle("Select the server ini file");

        if(response == JFileChooser.APPROVE_OPTION){
            file = fileChooser.getSelectedFile();
            System.out.println("File that was selected : " + file.getName() + " at : " + file.getAbsolutePath());
        }
        else{
            throw new NoSuchFileException("No file selected");
        }

        return file;
    }

    //Mark: Set up the window using JFrame
    private static void setUpWindow(){
        frame = new JFrame("Palword Server Manager");
        frame.setLayout(new BorderLayout());

        //TabbedPane to contain other child JPanels
        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(0, 0, 480, 620);

        //Create JPanel view controllers using JPanelViewController
        serverPanel = new JPanelViewController(JPanelViewController.PanelType.ServerSettings);
        JPanelViewController helpPanel = new JPanelViewController(JPanelViewController.PanelType.Palsettings);

        //Create save button panel
        JPanel saveButtonPanel = new JPanel();
        saveButtonPanel.setBounds(tabbedPane.getWidth(), tabbedPane.getHeight(), frame.getWidth(), 100);
        //Set background then set layout
        saveButtonPanel.setBackground(Color.GREEN);
        saveButtonPanel.setLayout(new FlowLayout());
        
        //Create save button
        JButton saveButton = new JButton("Save");
        //Add button to the save button panel
        saveButtonPanel.add(saveButton);

        //Create load button
        JButton loadButton = new JButton("Load ini file");
        saveButtonPanel.add(loadButton);


        //Add the save button panel to the parent frame
        frame.add(saveButtonPanel, BorderLayout.SOUTH);

        //Add action listener to the save button
        saveButton.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e){
                //TODO: Actually make it save to file in the saveFile() method
                saveFile();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: make a file explorer call here
                try {
                    file = getFile();
                    setUpData();
                    serverPanel.updateComponents();
                    serverPanel.invalidate();
                    serverPanel.revalidate();
                    serverPanel.repaint();

                } catch (NoSuchFileException ex) {
                    System.out.println("File does not exist... " + ex.getMessage());
                } catch (FileNotFoundException ex) {
                    System.out.println("File not found... " + ex.getMessage());
                }
            }
        });

        //Add the individual JPanelViewControllers to each tabbed pane
        tabbedPane.add("Server Settings", serverPanel);
        tabbedPane.add("Help", helpPanel);
        
        //Set size of application window
        frame.setSize(480,720);
        frame.add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.setBounds(0, 0, frame.getWidth(), frame.getHeight()-100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Actually make it visible cause thats nice to have ya know
        frame.setVisible(true);
    }
    /**
     * Returns a string extracted from another string between two chars
     * @param inputString : the string you want to get a substring from
     * @param index1 : the char you want to use to start the substring process from
     * @param index2 : the char you want to use to end the substring process from
     * @return a substring of the given string for inputString dicated by 2 and 3rd params
     */
    public static String extractSubstringBetweenParentheses(String inputString, char index1, char index2) {
        int startIndex = inputString.indexOf(index1);
        int endIndex = inputString.indexOf(index2);

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return inputString.substring(startIndex + 1, endIndex);
        } else {
            return null;
        }
    }

    //Mark: Save the file as an ini file
    //TODO: Actually write to file, adding later
    private static void saveFile(){
        System.out.println("Saving file......");
    }

    //Mark: Getters and setters for the arraylists needed
    public static ArrayList<Settings> getSettingsObjects() {
        return settingsObjects;
    }
    public static ArrayList<String> getSettings() {
        return settings;
    }
}
