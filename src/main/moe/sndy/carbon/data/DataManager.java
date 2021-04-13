package moe.sndy.carbon.data;

import moe.sndy.carbon.data.profiles.ProfileManager;

public class DataManager {

    private final ProfileManager profileManager;

    public DataManager() {
        profileManager = new ProfileManager();
    }

    public void end() {
        profileManager.save();
    }

}
