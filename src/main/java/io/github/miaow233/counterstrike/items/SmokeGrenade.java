package io.github.miaow233.counterstrike.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SmokeGrenade extends ItemStack {

    public SmokeGrenade() {
        super(Material.SNOWBALL);
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "烟雾弹");
        meta.setCustomModelData(1001);
        setItemMeta(meta);
    }

}