import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;


public class App {

    static Scanner scnr = new Scanner(System.in);
    static File file;
    static JFrame frame;
    static ArrayList<String> settings = new ArrayList<String>();
    private static boolean fileLoaded = false;
    private static JTabbedPane tabbedPane;

    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to Palword Server Manager");
        setUpWindow();
    }

    private static void setUpWindow(){
        frame = new JFrame("Palword Server Manager");
        frame.setLayout(null);

        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(0, 0, 480, 720);

        JPanelViewController serverPanel = new JPanelViewController(JPanelViewController.PanelType.ServerSettings);
        JPanelViewController palsPanel = new JPanelViewController(JPanelViewController.PanelType.Palsettings);


        serverPanel.addButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                System.out.println("Opening file chooser");
                try {
                    loadFile();
                    palsPanel.fileLoaded = true;
                    palsPanel.setUpPanel(JPanelViewController.PanelType.Palsettings);
                    
                } catch (NoSuchFileException noSuchFileException) {
                    System.out.println("No such file exception, check your locations..." + noSuchFileException.getMessage());
                }catch (FileNotFoundException fileNotFoundException){
                    System.out.println("File not found exception, check your locations..." + fileNotFoundException.getMessage());
                }catch (Exception exception){
                    System.out.println("An error occurred... " + exception.getMessage());
                }
            }
        });

        tabbedPane.add("Server Settings", serverPanel);
        tabbedPane.add("Pal Settings", palsPanel);
        
        
        frame.setSize(480,720);
        frame.add(tabbedPane);
        tabbedPane.setBounds(0, 0, frame.getWidth(), frame.getHeight()-100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void loadFile() throws NoSuchFileException, FileNotFoundException{
        System.out.println("Loading file");

        //Mark : Open up a filechooser window
        JFileChooser fileChooser = new JFileChooser();
        int response = fileChooser.showOpenDialog(null);
        fileChooser.setDialogTitle("Select the server ini file");

        //Mark: Save the absolute path of the file to the global file variable
        if(response == JFileChooser.APPROVE_OPTION){
            file = fileChooser.getSelectedFile();
            System.out.println("File loaded: " + file.getName());
            fileLoaded = true;
            System.out.println("Has file loaded been set? : " + fileLoaded);
            
        } else {
            throw new NoSuchFileException("No file selected");
        }
        
        tabbedPane.revalidate();
        tabbedPane.repaint();
        frame.revalidate();
        frame.repaint();
        scnr = new Scanner(file);
        
        String input= "";

        while(scnr.hasNextLine()){
            input += scnr.nextLine();
        }

        //Check to see if the user forgot to copy the necessary settings
        if(!input.contains("[/Script/Pal.PalGameWorldSettings]")){
            System.out.println("This file does not contain the necessary settings for a Palworld server");

            //On reading the file, if the file does not contain the necessary settings, display a warning message
            SwingUtilities.invokeLater(() -> {
                JFrame warningFrame = new JFrame("Warning");
                warningFrame.setSize(300, 150);
                JLabel warningLabel = new JLabel("<html>This file does not contain the necessary settings for a Palworld server, specifically the first line [/Script/Pal.PalGameWorldSettings]</html>");
                warningLabel.setHorizontalAlignment(SwingConstants.CENTER);
                warningFrame.add(warningLabel);
                warningFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                warningFrame.setVisible(true);
            });
        //If the file contains the necessary settings, continue with the program
        }else{
            System.out.println("This file contains the necessary settings for a Palworld server");
            //Start parsing file
            settingsParser(input);
        }
    }

    public static void settingsParser(String input){
        //Mark: Parse the file
        //Get the index of the first and last parenthesis
        int startIndex = input.indexOf("(");
        int endIndex = input.indexOf(")", startIndex);

        if(startIndex != -1 && endIndex != -1){
            //Mark: Get the substring of the file that contains the settings
            String settings = input.substring(startIndex + 1, endIndex);
            //System.out.println(settings);
            input = settings;
        }

        String[] values = input.split(",");

        for(String value : values){
            settings.add(value);
            System.out.println(settings.get(settings.indexOf(value)) + " INDEX: " + settings.indexOf(value));
        }
    }

    public static ArrayList<String> getSettings(){
        return settings;
    }
    public static boolean getFileLoaded(){
        return fileLoaded;
    }
}
