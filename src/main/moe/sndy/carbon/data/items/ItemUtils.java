package moe.sndy.carbon.data.items;

import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {

    public static String getAttribute(org.bukkit.inventory.ItemStack bukkitStack, String... path) {
        ItemStack stack = CraftItemStack.asNMSCopy(bukkitStack);
        NBTTagCompound nbt = stack.getOrCreateTag();
        for (int i = 0; i < path.length - 1; i++) {
            nbt = nbt.getCompound(path[i]);
        }
        return nbt.getString(path[path.length - 1]);
    }

    public static String getItemId(org.bukkit.inventory.ItemStack item) {
        return getAttribute(item, "carbon", "id");
    }

    public static void syncWithNbt(org.bukkit.inventory.ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        String textureId = getAttribute(item, "carbon", "texture-id");
        String displayName = getAttribute(item, "carbon", "name");
        String rarity = getAttribute(item, "carbon", "rarity");
        meta.setDisplayName("§" + ItemConstants.getRarityFormatting(rarity) + displayName);
        // TODO: Rest of the syncing process
        meta.setCustomModelData(textureId.hashCode());
        item.setItemMeta(meta);
    }



}
