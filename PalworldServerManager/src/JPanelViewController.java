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
            this.setLayout(new FlowLayout());
            for(int i = 0; i < settings.size(); i++){
                
            }

                break;
            case Palsettings:

                break;
            case Other:
                break;
        }
    }
}
