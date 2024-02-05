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
    ArrayList<JTextField> textFields = new ArrayList<JTextField>();
    //TODO: Use this already existing hashmap
    static Map<String, String> settings = new HashMap<String, String>();

    /*TODO: Delete this later? Figuring having all the settings on one page is fine
            however it can get messy.
     */
    public static enum PanelType {
        ServerSettings, Palsettings, Other
    }
    //Mark: Default constructor for JPanel
    public JPanelViewController(PanelType type){
        setLayout(null);
        setUpPanel(type);
    }

    /**
     * Set up the JPanel according to its type
     * @param type : what type the panel will be (PanelType: ServerSettings, PalSettings, Other)
     */
    public void setUpPanel(PanelType type){
        //Typical switch statement
        switch(type){
            case ServerSettings:
            this.setLayout(new BorderLayout());

            //wrapper panel to throw into the scroll view
            JPanel wrapperPanel = new JPanel();
            wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));

            //Add all the settings in one scroll pane
            for (Settings settings : App.settingsObjects) {
                //make a setting panel
                JPanel settingPanel = new JPanel();
                settingPanel.setBounds(this.getWidth(), this.getHeight(), this.getWidth(), 100);
                //Add textPane setting description
                JTextPane textPane = new JTextPane();
                textPane.setBounds(0,0,500,60);
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

                textFields.add(textField);
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

    public void upDateObjects(){
        for (int i = 0; i < textFields.size(); i++){

            System.out.println("Old value : " + App.getSettingsObjects().get(i).getSettingValue() + " new value : " + textFields.get(i).getText());

            App.getSettingsObjects().get(i).setSettingValue(textFields.get(i).getText());
        }
    }

    /*/
    TODO: Make this better. Have all this use a hash map then this atrocious for loop. Will need to adjust the
          other classes for this change as well. ArrayList to Hashmap for storing keyvalue pairs.
          This works for now, until the developer adds more complex settings. Also leaves it for more errors.
     */
    public void updateComponents(){
        int size = textFields.size();
        for (int i= 0; i< textFields.size(); i++){

            int adjustedIndex = (i + size - 1) % size;
            JTextField text = textFields.get(i);
            String oldText = text.getText();
            text.setText(App.getSettingsObjects().get(adjustedIndex).getSettingValue());

            //TODO: DEBUG PRINT DELETE LATER
            if (!oldText.trim().equals(text.getText().trim())) {
                System.out.println("TEXT FIELD UPDATED!!!!!!!!!! NEW STRING > ::::: " + text.getText() + "\nOLD TEXT >:::: " + oldText);
            }else{
                System.out.println("TEXT FIELD NOT UPDATED??????????? ::::: "+ text.getText()+ "\nOLD TEXT >:::: " + oldText);
            }
        }
    }
}
