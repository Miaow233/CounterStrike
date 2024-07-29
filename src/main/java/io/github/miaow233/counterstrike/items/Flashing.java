package io.github.miaow233.counterstrike.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Flashing extends ItemStack {

    public Flashing() {
        super(Material.SNOWBALL);
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "闪光弹");
        meta.setCustomModelData(1000);
        setItemMeta(meta);
    }

}