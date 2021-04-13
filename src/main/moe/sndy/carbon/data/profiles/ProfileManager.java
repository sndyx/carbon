package moe.sndy.carbon.data.profiles;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileManager {

    private final Map<UUID, NetworkPlayer> players = new HashMap<UUID, NetworkPlayer>();

    public void save() {
        for (NetworkPlayer player : players.values()) {
            player.save();
        }
    }

}
