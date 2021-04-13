package moe.sndy.carbon.data.profiles;

import moe.sndy.carbon.Carbon;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NetworkPlayer {

    private final Profile[] profiles;
    private final MetaAdapter global;
    private final UUID uuid;
    private int currentProfile = 0;

    public NetworkPlayer(UUID uuid) {
        this.uuid = uuid;
        File file = new File("Carbon/data/players/" + uuid.toString());
        if (!file.exists()) {
            if (!file.mkdir()) {
                Carbon.logger().warning("failed to create profile file for '" + uuid.toString() + "'");
            }
        }
        List<Integer> ids = new ArrayList<Integer>();
        for (String profile : file.list()) {
            if (profile.startsWith("profile-")) {
                ids.add(Integer.parseInt(profile.substring(8)));
            }
        }
        profiles = new Profile[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            profiles[i] = new Profile(uuid, ids.get(i));
        }
        global = new MetaAdapter(uuid);
    }

    public Profile getProfile() {
        return profiles[currentProfile];
    }

    public Profile[] getProfiles() {
        return profiles;
    }

    public MetaAdapter getMeta() {
        return global;
    }

    public boolean profileExists(int id) {
        for (Profile profile : profiles) {
            if (profile.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public void setProfile(int id) {
        getProfile().save();
        currentProfile = id;
        getProfile().syncWithPlayer();
    }

    public void save() {
        getProfile().save();
        try {
            FileOutputStream outputStream = new FileOutputStream(new File("Carbon/data/players/" + uuid.toString() + "/global.dat"));
            ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
            objectStream.writeObject(global);
            objectStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
