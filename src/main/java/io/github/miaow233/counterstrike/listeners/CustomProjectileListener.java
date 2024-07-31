package io.github.miaow233.counterstrike.listeners;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.events.PlayerReleaseRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class CustomProjectileListener implements Listener {

    private static final Map<Player, Long> chargeStartTimes = new HashMap<>();
    private static final Map<Player, BukkitRunnable> chargingTasks = new HashMap<>();
    private final CounterStrike plugin;

    public CustomProjectileListener(CounterStrike plugin) {
        this.plugin = plugin;
    }

    public static void startCharging(Player player) {
        if (chargingTasks.containsKey(player)) {
            return;
        }

        chargeStartTimes.put(player, System.currentTimeMillis());

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isHandRaised()) {
                    long chargeStartTime = chargeStartTimes.getOrDefault(player, System.currentTimeMillis());
                    long chargeTime = System.currentTimeMillis() - chargeStartTime;

                    PlayerReleaseRightClickEvent event = new PlayerReleaseRightClickEvent(player, chargeTime);
                    Bukkit.getPluginManager().callEvent(event);

                    chargingTasks.remove(player);
                    this.cancel();
                }
            }
        };
        task.runTaskTimer(CounterStrike.instance, 0, 1);
        chargingTasks.put(player, task);
    }

    public static void stopCharging(Player player) {
        if (chargingTasks.containsKey(player)) {
            chargingTasks.get(player).cancel();
            chargingTasks.remove(player);
            chargeStartTimes.remove(player);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getInventory().getItemInMainHand().getType() == Material.SNOWBALL) {
                startCharging(player);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Snowball snowball) {
            if (event.getHitBlock() != null) {
                Vector velocity = snowball.getVelocity();
                Location hitLocation = snowball.getLocation();
                Vector normal = event.getHitBlockFace().getDirection();

                Vector reflection = velocity.subtract(normal.multiply(2 * velocity.dot(normal)));
                snowball.setVelocity(reflection);
            }
        }
    }

    @EventHandler
    public void onPlayerReleaseRightClick(PlayerReleaseRightClickEvent event) {
        Player player = event.getPlayer();
        long chargeTime = event.getChargeTime();

        Snowball snowball = player.launchProjectile(Snowball.class);
        Vector velocity = player.getLocation().getDirection().multiply(calculateCharge(chargeTime));
        snowball.setVelocity(velocity);

        stopCharging(player);
    }

    // 计算蓄力效果
    private double calculateCharge(long chargeTime) {
        double minChargeTime = 500.0;
        double maxChargeTime = 2000.0;
        double minChargeMultiplier = 1.0;
        double maxChargeMultiplier = 2.0;

        double chargeMultiplier = minChargeMultiplier + ((double) chargeTime - minChargeTime) / (maxChargeTime - minChargeTime) * (maxChargeMultiplier - minChargeMultiplier);
        return Math.min(Math.max(chargeMultiplier, minChargeMultiplier), maxChargeMultiplier);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        stopCharging(event.getPlayer());
    }
}
