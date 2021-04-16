package moe.sndy.carbon.data.items;

import moe.sndy.carbon.Carbon;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemLoader {

    private final List<LootTable> tables = new ArrayList<>();
    private final List<ItemContainer> items = new ArrayList<>();

    public ItemLoader() {
        load();
    }

    public ItemStack[] getDrops(String id) {
        return getDrops(id, 1);
    }

    public ItemStack[] getDrops(String id, int amount) {
        LootTable table = getTable(id);
        ItemStack[] drops = table.getDrops(amount);
        for (ItemStack drop : drops) {
            ItemUtils.syncWithNbt(drop);
        }
        return drops;
    }

    public ItemStack getItem(String id) {
        return getItem(id, 1);
    }

    public ItemStack getItem(String id, int amount) {
        for (ItemContainer container : items) {
            if (container.getId().equals(id)) {
                ItemStack item = container.getItem();
                item.setAmount(amount);
                ItemUtils.syncWithNbt(item);
                return item;
            }
        }
        Carbon.logger().warning("Failed to get item from Id '" + id + "'");
        return null;
    }

    public LootTable getTable(String id) {
        for (LootTable table : tables) {
            if (table.getId().equals(id)) {
                return table;
            }
        }
        Carbon.logger().warning("Failed to get table from ID '" + id + "'");
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    private void load() {
        List<File> files = new ArrayList<>();
        File tablesFile = new File("carbon/resources/tables");
        files.addAll(Arrays.asList(tablesFile.listFiles()));
        File itemsFile = new File("carbon/resources/items");
        files.addAll(Arrays.asList(itemsFile.listFiles()));
        for (File file : files) {
            load0(file);
            Carbon.logger().info("Finished loading file '" + file.getPath() + "'.") ;
        }
    }

    @SuppressWarnings("unchecked")
    private void load0(File file) {
        Map<String, Object> yaml;
        try {
             yaml = new Yaml().load(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            Carbon.logger().warning("Failed to load file '" + file.getPath() + "'.");
            return;
        }
        switch (file.getParentFile().getName()) {
            case "tables":
                for (String element : yaml.keySet()) {
                    tables.add(new LootTable(element, (Map<String, Object>) yaml.get(element)));
                    Carbon.logger().info("Parsed and loaded loot table: '" + element + "', adding to game.");
                }
                break;
            case "items":
                for (String element : yaml.keySet()) {
                    try {
                        items.add(ItemContainer.parseItem(element, (Map<String, Object>) yaml.get(element)));
                        Carbon.logger().info("Parsed and loaded item: '" + element + "', adding to game.");
                    } catch (IllegalArgumentException e) {
                        Carbon.logger().warning("Cannot parse item: '" + element + "', skipping process. Caused by:");
                        Carbon.logger().warning(e.getMessage());
                    }
                }
        }
    }

    public void reload() {
        tables.clear();
        items.clear();
        load();
    }

}
