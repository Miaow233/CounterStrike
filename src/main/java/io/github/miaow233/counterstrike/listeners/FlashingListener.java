package io.github.miaow233.counterstrike.listeners;

import io.github.miaow233.counterstrike.CounterStrike;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class FlashingListener implements IThrowableListener {


    private final CounterStrike plugin;

    public FlashingListener(CounterStrike plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerThrow(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null && event.getItem().getType() == Material.SNOWBALL) {
                ItemStack item = event.getItem();


                if (item.hasItemMeta()
                        && item.getItemMeta().hasCustomModelData()
                        && item.getItemMeta().getCustomModelData() == 1000) {
                    event.setCancelled(true);
                    Player player = event.getPlayer();
                    Snowball snowball = player.launchProjectile(Snowball.class);
                    //snowball.setBounce(true);
                    snowball.setCustomName("闪光弹");
                    snowball.setVelocity(player.getLocation().getDirection().multiply(1.5));
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        Location location = snowball.getLocation();
                        location.getWorld().spawnParticle(Particle.FLASH, location, 1);
                        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);

                        for (Player nearbyPlayer : location.getWorld().getPlayers()) {
                            if (nearbyPlayer.getLocation().distance(location) <= 15) {
                                if (isLineOfSightClear(location, nearbyPlayer.getEyeLocation())) {
                                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1));
                                }
                            }
                        }
                    }, 20L); // 20 ticks = 1 second
                }
            }
        }
    }

    // 监听投掷物撞击事件
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        // 检查投掷物是否是我们自定义的反弹投掷物
        if (projectile.getCustomName() != null && projectile.getCustomName().equals("闪光弹")) {
            Location hitLocation = event.getEntity().getLocation();


            // 反弹逻辑
            if (event.getHitBlock() != null) {
                Vector currentVelocity = projectile.getVelocity();
                Vector normal = getHitBlockNormal(event.getHitBlockFace());
                Vector newVelocity = currentVelocity.subtract(normal.multiply(2 * currentVelocity.dot(normal)));
                projectile.setVelocity(newVelocity);
                // 只在第一次撞击时反弹
            }
        }
    }

    private Vector getHitBlockNormal(org.bukkit.block.BlockFace face) {
        switch (face) {
            case UP:
                return new Vector(0, 1, 0);
            case DOWN:
                return new Vector(0, -1, 0);
            case NORTH:
                return new Vector(0, 0, -1);
            case SOUTH:
                return new Vector(0, 0, 1);
            case WEST:
                return new Vector(-1, 0, 0);
            case EAST:
                return new Vector(1, 0, 0);
            default:
                return new Vector(0, 0, 0);
        }
    }


}