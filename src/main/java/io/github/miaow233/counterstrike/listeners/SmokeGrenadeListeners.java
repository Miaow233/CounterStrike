package io.github.miaow233.counterstrike.listeners;

import io.github.miaow233.counterstrike.CounterStrike;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SmokeGrenadeListeners implements IThrowableListener {

    private final CounterStrike plugin;

    public SmokeGrenadeListeners(CounterStrike plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerThrow(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (item.getType() == Material.SNOWBALL
                    && item.hasItemMeta()
                    && item.getItemMeta().hasCustomModelData()
                    && item.getItemMeta().getCustomModelData() == 1001) {
                Snowball snowball = player.launchProjectile(Snowball.class);
                snowball.setCustomName("SmokeGrenade");
            }
        }
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getCustomName() != null
                && event.getEntity().getCustomName().equals("SmokeGrenade")) {
            event.getEntity().getWorld().spawnParticle(Particle.SMOKE_LARGE, event.getEntity().getLocation(), 100, 0.5, 0.5, 0.5, 0.1);
            event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, 1.0f);
        }
    }
}