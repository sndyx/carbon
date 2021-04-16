package moe.sndy.carbon.data.profiles;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileManager implements Listener {

    private final Map<UUID, NetworkPlayer> players = new HashMap<>();

    public void save() {
        for (NetworkPlayer player : players.values()) {
            player.save();
        }
    }

    public NetworkPlayer getPlayer(UUID uuid) {
        if (players.containsKey(uuid)) {
            return players.get(uuid);
        } else {
            return NetworkPlayer.getOfflinePlayer(uuid);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        players.put(event.getPlayer().getUniqueId(), new NetworkPlayer(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        players.get(event.getPlayer().getUniqueId()).save();
        players.remove(event.getPlayer().getUniqueId());
    }

}
