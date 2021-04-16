package moe.sndy.carbon.data.items;

import moe.sndy.carbon.Carbon;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class LootTable {

    private final Map<String, Object> table;
    private final String id;

    public LootTable(String id, Map<String, Object> table) {
        this.table = table;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public ItemStack[] getDrops() {
        return getDrops(1);
    }

    public ItemStack[] getDrops(int amount) {
        List<ItemStack> drops = new ArrayList<>();
        ItemLoader loader = Carbon.getPlugin().getDataManager().getItemLoader();
        for (int i = 0; i < amount; i++) {
            String[] ids = getDrops0(table);
            for (String id : ids) {
                String[] split = id.split(" ");
                int stackSize = Integer.parseInt(split[0].substring(0, split[0].length() - 1));
                if (split[1].startsWith("<")) {
                    ItemStack[] items = loader.getDrops(split[1].substring(1, split[1].length() - 1), stackSize);
                    drops.addAll(Arrays.asList(items));
                } else {
                    ItemStack item = loader.getItem(split[1], stackSize);
                    drops.add(item);
                }
            }
        }
        return drops.toArray(new ItemStack[drops.size()]);
    }

    private static final Random rand = new Random();

    @SuppressWarnings("unchecked")
    public String[] getDrops0(Map<String, Object> table) {
        List<String> drops = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        for (String key : table.keySet()) {
            keys.add(key);
        }
        keys.sort( (o1, o2) -> {
            if (o2.equals("or")) {
                return -1;
            }
            return 1;
        });
        boolean orCancel = false;
        for (String key : keys) {
            Object value = table.get(key);
            switch (key) {
                case "and":
                    if (value instanceof Map) {
                        drops.addAll(Arrays.asList(getDrops0((Map<String, Object>) value)));
                    } else {
                        drops.addAll((List<String>) value);
                    }
                    break;
                case "or":
                    if (!orCancel) {
                        if (value instanceof Map) {
                            drops.addAll(Arrays.asList(getDrops0((Map<String, Object>) value)));
                        } else {
                            drops.addAll((List<String>) value);
                        }
                    }
                    break;
                default:
                    int chance = Integer.valueOf(key.substring(0, key.length() - 1));
                    if (value instanceof Map && (((Map<String, Object>) value).containsKey("?1") || ((Map<String, Object>) value).containsKey("?1^"))) {
                        Map<String, Object> map = (Map<String, Object>) value;
                        for (String nKey : map.keySet()) {
                            if (rand.nextInt(100) + 1 < chance) {
                                drops.addAll((List<String>) map.get(nKey));
                                orCancel = true;
                                if (nKey.contains("^")) {
                                    break;
                                }
                            }
                        }
                    } else if (rand.nextInt(100) + 1 < chance) {
                        if (value instanceof Map) {
                            drops.addAll(Arrays.asList(getDrops0((Map<String, Object>) value)));
                        } else {
                            drops.addAll((List<String>) value);
                        }
                        orCancel = true;
                    }
            }
        }
        return drops.toArray(new String[drops.size()]);
    }

}
