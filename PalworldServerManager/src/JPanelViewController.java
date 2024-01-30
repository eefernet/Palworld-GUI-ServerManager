import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

public class JPanelViewController extends JPanel{
    JButton loadIniButton;
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
            JLabel panelInfo = new JLabel("All settings regarding difficulty, Day night rate, etc. will be here.");
            panelInfo.setBounds(0, 10, 500, 30);
            add(panelInfo);

            //Load ini Button
            JLabel loadIniLabel = new JLabel("Load ini file : ");
            loadIniButton = new JButton("Load");
            loadIniLabel.setBounds(10, 50, 100, 30);
            loadIniButton.setBounds(loadIniLabel.getX() + loadIniLabel.getWidth(), loadIniLabel.getY(), 100, 30);
            
            //Add action listener to load ini button
            loadIniButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    loadFile();
                    fileLoaded = true;
                    
                    JLabel fileLoaded = new JLabel("Ini file loaded");
                    fileLoaded.setBounds(loadIniButton.getX() + loadIniButton.getWidth() + 10, loadIniButton.getY(), 100, 30);
                    add(fileLoaded);
                    getValues();
                    revalidate();
                    repaint();
                    setUpPanel(type);
                }
            });
            add(loadIniLabel);
            add(loadIniButton);

            if(fileLoaded){
                int offest = 50;
                for(int i = 0; i < 3; i++){
                    String[] values = App.getSettings().get(i).split("=");
                    JLabel label = new JLabel("<html>" + values[0] + "</html>");
                    label.setHorizontalAlignment(SwingConstants.LEFT);
                    label.setBounds(5, loadIniLabel.getY()+offest, 300, 30);
                    offest += 50;

                    JTextField textField = new JTextField();
                    textField.setText(values[1]);
                    textField.setBounds(label.getWidth() + 50, label.getY(), 100, 30);

                    labels.add(label);
                    textFields.add(textField);
                    add(label);
                    add(textField);
                }
                
                
            }
                break;
            case Palsettings:

            if(fileLoaded){
                System.out.println("Pal tab loaded");
                JLabel desc = new JLabel("Pal settings will be here.");
                desc.setBounds(5, 10, 300, 30);
                int offest = 50;
                add(desc);

                for(String key : settings.keySet()){
                    //System.out.println("Key: " + key + " Value: " + settings.get(key));
                    
                        System.out.println("Key: " + key + " Value: " + settings.get(key) + "PAL WAS DEDTECTED");
                        JLabel label = new JLabel("<html>" + key + "</html>");
                        label.setHorizontalAlignment(SwingConstants.LEFT);
                        label.setBounds(5, desc.getY()+offest, 300, 30);
                        offest += 50;

                        JTextField textField = new JTextField();
                        textField.setText(settings.get(key));
                        textField.setBounds(label.getWidth() + 50, label.getY(), 100, 30);

                        labels.add(label);
                        textFields.add(textField);
                        add(label);
                        add(textField);
                    
                }

                for (Map.Entry<String, String> entry : settings.entrySet()) {

                    String key = entry.getKey();
                    String value = entry.getValue();
                    
                }
                /* 
                for(int i = 4; i < 8; i++){
                    String[] values = App.getSettings().get(i).split("=");
                    JLabel label = new JLabel("<html>" + values[0] + "</html>");
                    label.setHorizontalAlignment(SwingConstants.LEFT);
                    label.setBounds(5, desc.getY()+offest, 300, 30);
                    offest += 50;

                    JTextField textField = new JTextField();
                    textField.setText(values[1]);
                    textField.setBounds(label.getWidth() + 50, label.getY(), 100, 30);

                    labels.add(label);
                    textFields.add(textField);
                    add(label);
                    add(textField);
                }

                for(int i = 14; i < 18; i++){
                    String[] values = App.getSettings().get(i).split("=");
                    JLabel label = new JLabel("<html>" + values[0] + "</html>");
                    label.setHorizontalAlignment(SwingConstants.LEFT);
                    label.setBounds(5, desc.getY()+offest, 300, 30);
                    offest += 50;

                    JTextField textField = new JTextField();
                    textField.setText(values[1]);
                    textField.setBounds(label.getWidth() + 50, label.getY(), 100, 30);

                    labels.add(label);
                    textFields.add(textField);
                    add(label);
                    add(textField);
                }
                */
                
            }
                break;
            case Other:
                break;
        }
    }
    private static String getValue(int index){
        String[] values = App.settings.get(index).split("=");
        return values[1];
    }

    private static void getValues(){
        System.out.println("RUNNING GET VALUES");
        
        for(int i = 0; i < App.getSettings().size(); i++){
            String[] value = App.getSettings().get(i).split("=");
            String key = value[0];
            String val = value[1];
            settings.put(key, val);
        }
    }

    public void addButtonListener(ActionListener listener){
        //Add action listener to the load button
        loadIniButton.addActionListener(listener);
    }

    public static void loadFile(){
        System.out.println("Loading file......");
    }
}
