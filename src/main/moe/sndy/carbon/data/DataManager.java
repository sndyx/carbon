package moe.sndy.carbon.data;

import moe.sndy.carbon.Carbon;
import moe.sndy.carbon.data.items.ItemLoader;
import moe.sndy.carbon.data.profiles.ProfileManager;

public class DataManager {

    private final ProfileManager profileManager;
    private final ItemLoader itemLoader;

    public DataManager() {
        profileManager = new ProfileManager();
        itemLoader = new ItemLoader();
    }

    public void end() {
        profileManager.save();
        Carbon.logger().info("Successfully saved data");
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public ItemLoader getItemLoader() {
        return itemLoader;
    }

}
