import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

public class JPanelViewController extends JPanel{
    boolean fileLoaded = false;

    ArrayList<JLabel> labels = new ArrayList<JLabel>();
    ArrayList<JTextField> textFields = new ArrayList<JTextField>();
    static Map<String, String> settings = new HashMap<String, String>();

    public static enum PanelType {
        ServerSettings, Palsettings, Other
    }
    
    public JPanelViewController(PanelType type){
        setLayout(null);
        setUpPanel(type);

    }

    public void setUpPanel(PanelType type){
        switch(type){
            case ServerSettings:
            this.setLayout(new BorderLayout());

            //wrapper panel to throw into the scroll view
            JPanel wrapperPanel = new JPanel();
            wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
            
            //Add all the settings in one scroll pane
            for (Settings settings : App.settingsObjects) {
                //make a setting pannel
                JPanel settingPanel = new JPanel();
                settingPanel.setBounds(this.getWidth(), this.getHeight(), this.getWidth(), 100);
                //Add textPane setting description
                JTextPane textPane = new JTextPane();
                textPane.setBounds(0,0,350,40);
                textPane.setAlignmentX(RIGHT_ALIGNMENT);
                textPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                textPane.setText(settings.getSettingName());

                //Add the textField
                JTextField textField = new JTextField();
                textField.setText(settings.getSettingValue());
                textField.setAlignmentX(RIGHT_ALIGNMENT);
                
                //add the fields to the setting panel
                settingPanel.add(textPane);
                settingPanel.add(textField);

                //Set layout and ad it to the wrapper panel
                //TODO: change later to a different layout that makes sense so they are all aligned.
                settingPanel.setLayout(new FlowLayout());
                wrapperPanel.add(settingPanel);   
            }

            //Create a scroll panel with a child of wrapperPanel
            JScrollPane scrollPanel = new JScrollPane(wrapperPanel);
            //set scroll bar policy
            scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            //Add scroll bar
            add(scrollPanel);
            
                break;
            case Palsettings:

                break;
            case Other:
                break;
        }
    }
}
