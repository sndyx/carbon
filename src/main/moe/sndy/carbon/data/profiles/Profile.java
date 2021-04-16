package moe.sndy.carbon.data.profiles;

import moe.sndy.carbon.Carbon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.util.Map;
import java.util.UUID;


public class Profile {

    // TODO: More needed features

    private final ProfileAdapter adapter;
    private final UUID uuid;
    private final int id;

    public Profile(UUID uuid, int id) {
        adapter = new ProfileAdapter(uuid, id);
        this.uuid = uuid;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // Could be an issue, suppressing for now because issues are for future me :)
    @SuppressWarnings("ConstantConditions")
    public void save() {
        Player player = Bukkit.getPlayer(uuid);
        Inventory inventory = player.getInventory();
        String serializedInventory;
        try {
            serializedInventory = serializeInventory(inventory);
        } catch (IOException e) {
            e.printStackTrace();
            Carbon.logger().warning("Inventory of player '" + uuid + " (" + player.getName() + ")' failed to save! Printing inventory contents:");
            for (int i = 0; i < inventory.getSize(); ++i) {
                System.out.println(inventory.getItem(i).getItemMeta().getDisplayName());
            }
            serializedInventory = "";
        }
        adapter.setAttribute(serializedInventory, "player", "inventory");
        adapter.setAttribute(player.getWorld().getName(), "player", "location", "world");
        adapter.setAttribute(player.getLocation().getX(), "player", "location", "x");
        adapter.setAttribute(player.getLocation().getY(), "player", "location", "y");
        adapter.setAttribute(player.getLocation().getZ(), "player", "location", "z");
        try {
            FileOutputStream fileStream = new FileOutputStream("carbon/data/players/" + uuid + "/Profile-" + id + ".dat");
            ObjectOutputStream outputStream = new ObjectOutputStream(fileStream);
            outputStream.writeObject(adapter);
            outputStream.close();
        } catch (IOException e) {
            Carbon.logger().warning("Inventory of player '" + uuid + " (" + player.getName() + ")' failed to save! Printing inventory contents:");
            for (int i = 0; i < inventory.getSize(); ++i) {
                System.out.println(inventory.getItem(i).getItemMeta().getDisplayName());
            }
        }
    }

    private String serializeInventory(Inventory inventory) throws IOException {
        ItemStack[] items = inventory.getContents();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
        dataOutput.writeInt(items.length);
        for (ItemStack item : items) {
            if (item != null) {
                dataOutput.writeObject(item.serialize());
            } else {
                dataOutput.writeObject(null);
            }
        }
        dataOutput.close();
        return Base64Coder.encodeLines(outputStream.toByteArray());
    }

    @SuppressWarnings("unchecked")
    private ItemStack[] deserializeInventory(String data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
        ItemStack[] items = new ItemStack[dataInput.readInt()];
        for (int Index = 0; Index < items.length; Index++) {
            Map<String, Object> stack = (Map<String, Object>) dataInput.readObject();
            if (stack != null) {
                items[Index] = ItemStack.deserialize(stack);
            } else {
                items[Index] = null;
            }
        }
        dataInput.close();
        return items;
    }

    @SuppressWarnings("ConstantConditions")
    public void syncWithPlayer() {
        try {
            Player player = Bukkit.getPlayer(uuid);
            World world = Bukkit.getWorld((String) adapter.getAttribute("player", "location", "world"));
            int x = (Integer) adapter.getAttribute("player", "location", "x");
            int y = (Integer) adapter.getAttribute("player", "location", "y");
            int z = (Integer) adapter.getAttribute("player", "location", "z");
            player.teleport(new Location(world, x, y, z));
            player.getInventory().setContents(deserializeInventory((String) adapter.getAttribute("player", "inventory")));
        } catch (NullPointerException | IOException | ClassNotFoundException e) {
            Carbon.logger().warning("failed to sync '" + uuid.toString() + "' with profile '" + id + "'");
        }
    }

}