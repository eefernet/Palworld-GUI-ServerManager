public class Settings {
    
    private SettingType type;
    private String settingName, settingValue;

    enum SettingType {
        ServerSettings, PalSettings, PlayerSettings, Other
    }

    public Settings(String settingName, String settingValue, SettingType type){
        this.type = type;
        this.settingName = settingName;
        this.settingValue = settingValue;
    }

    public SettingType getType() {
        return type;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    

}
