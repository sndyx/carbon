package moe.sndy.carbon.data.items;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;
import net.minecraft.server.v1_16_R3.NBTTagString;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public class ItemContainer {

    private ItemStack item;
    private String id;

    @SuppressWarnings("unchecked")
    public static ItemContainer parseItem(String id, Map<String, Object> attributes) throws IllegalArgumentException {
        ItemContainer container = new ItemContainer();
        container.id = id;
        net.minecraft.server.v1_16_R3.ItemStack item;
        NBTTagCompound itemNbt;
        NBTTagCompound nbt = new NBTTagCompound();
        String type;
        try {
            type = attributes.get("type").toString();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Item '" + id + "' missing requisite field: [ " + id + "/type ].");
        }
        switch (type) {
            case "equipment":
            case "consumable":
                if (!attributes.containsKey("intent")) {
                    throw new IllegalArgumentException("Item '" + id + "' missing requisite field: [ " + id + "/intent ].");
                }
                Material material;
                String intent = attributes.get("intent").toString();
                switch (intent) {
                    case "sword":
                        material = Material.WOODEN_SWORD;
                        break;
                    case "helmet":
                        material = Material.LEATHER_HELMET;
                        break;
                    case "chestplate":
                        material = Material.LEATHER_CHESTPLATE;
                        break;
                    case "leggings":
                        material = Material.LEATHER_LEGGINGS;
                        break;
                    case "boots":
                        material = Material.LEATHER_BOOTS;
                        break;
                    case "food":
                        material = Material.APPLE; // TODO: Find suitable food
                        break;
                    case "potion":
                        material = Material.WATER; // TODO: Make sure this is actually a water bottle
                        break;
                    default:
                        throw new IllegalArgumentException("Illegal argument for field [ " + id + "/intent ]: '" + intent + "'. required: [ sword | helmet | chestplate | leggings | boots | food | potion ].");
                }
                item = CraftItemStack.asNMSCopy(new ItemStack(material));
                itemNbt = item.getOrCreateTag();
                String types = ItemConstants.STAT_TYPES;
                if (type.equals("consumable")) {
                    types = ItemConstants.CONSUMABLE_TYPES;
                }
                if (attributes.containsKey("stats")) {
                    List<Map<String, Object>> stats;
                    try {
                        stats = (List<Map<String, Object>>) attributes.get("stats");
                    } catch (ClassCastException e) {
                        throw new IllegalArgumentException("Illegal contents for node [ " + id + "/stats ].");
                    }
                    NBTTagList statList = new NBTTagList();
                    int index = 0;
                    for (Map<String, Object> stat : stats) {
                        NBTTagCompound statNbt = new NBTTagCompound();
                        try {
                            String statType = stat.get("type").toString();
                            if (!statType.matches(types)) {
                                throw new IllegalArgumentException("Illegal argument for field [ " + id + "/stats/" + index + "/type ]: '" + statType + "'. required: [ " + types.replace("|", " | ") + " ].");
                            }
                            statNbt.setString("type", statType);
                        } catch (NullPointerException e) {
                            throw new IllegalArgumentException("Item '" + id + "' missing requisite field: [ " + id + "/stats/" + index + "/type ].");
                        }
                        String value = "null";
                        try {
                            value = stat.get("value").toString();
                            statNbt.setDouble("value", Double.parseDouble(value));
                        } catch (NullPointerException e) {
                            throw new IllegalArgumentException("Item '" + id + "' missing requisite field: [ " + id + "/stats/" + index + "/value ].");
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Illegal argument for field [ " + id + "/stats/" + index + "/value ]: '" + value + "'. required: [ java.lang.Double ].");
                        }
                        if (type.equals("equipment")) {
                            if (stat.containsKey("chance")) {
                                String chance;
                                chance = stat.get("chance").toString();
                                if (!chance.matches("100%|[1-9][0-9]?(?:\\.[0-9]+)?%")) {
                                    throw new IllegalArgumentException("Illegal argument for field [ " + id + "/stats/" + index + "/chance ]: '" + value + "'. required: [ <java.lang.Double>% ].");
                                }
                                chance = chance.substring(0, chance.length() - 1);
                                statNbt.setDouble("chance", Double.parseDouble(chance));
                            } else {
                                statNbt.setDouble("chance", 100);
                            }
                        } else {
                            if (stat.containsKey("duration")) {
                                String duration = stat.get("duration").toString();
                                long time;
                                try {
                                    time = Date.parse(duration); // TODO: Deprecated method
                                } catch (IllegalArgumentException e) {
                                    throw new IllegalArgumentException("Illegal argument for field [ " + id + "/stats/" + index + "/duration ]: '" + value + "'. required: [ <HH:MM:SS> ].");
                                }
                                statNbt.setDouble("duration", time);
                            }
                        }
                    }
                    nbt.set("stats", statList);
                }
                break;
            case "resource":
                item = CraftItemStack.asNMSCopy(new ItemStack(Material.BRICK));
                itemNbt = item.getOrCreateTag();
                break;
            default:
                throw new IllegalArgumentException("Illegal argument for field [ " + id + "/type ]: '" + type + "'. required: [ equipment | resource | consumable ].");
        }
        try {
            nbt.setString("id", attributes.get("id").toString());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Item '" + id + "' missing requisite field: [ " + id + "/id ].");
        }
        try {
            nbt.setString("texture-id", attributes.get("texture-id").toString());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Item '" + id + "' missing requisite field: [ " + id + "/texture-id ].");
        }
        try {
            nbt.setString("name", attributes.get("name").toString());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Item '" + id + "' missing requisite field: [ " + id + "/name ].");
        }
        try {
            String rarity = attributes.get("rarity").toString();
            if (!(rarity.equals("common") || rarity.equals("uncommon") || rarity.equals("rare") || rarity.equals("epic") || rarity.equals("legendary") || rarity.equals("divine"))) {
                throw new IllegalArgumentException("Illegal argument for field [ " + id + "/intent ]: '" + rarity + "'. required: [ common | uncommon | rare | epic | legendary | divine ].");
            }
            nbt.setString("rarity", rarity);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Item '" + id + "' missing requisite field: [ " + id + "/rarity ].");
        }
        nbt.setString("type", type);
        if (attributes.containsKey("description")) {
            nbt.setString("description", (String) attributes.get("description"));
        }
        if (attributes.containsKey("flags")) {
            NBTTagList flagsList = new NBTTagList();
            List<String> flags;
            try {
                flags = (List<String>) attributes.get("flags");
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Illegal contents for node [ " + id + "/flags ].");
            }
            for (String flag : flags) {
                flagsList.add(NBTTagString.a(flag));
            }
            nbt.set("flags", flagsList);
        }
        itemNbt.set("carbon", nbt);
        container.item = CraftItemStack.asBukkitCopy(item);
        return container;
    }


    public ItemStack getItem() {
        return item;
    }

    public String getId() {
        return id;
    }

}
