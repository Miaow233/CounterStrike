package io.github.miaow233.counterstrike.listeners;

import io.github.miaow233.counterstrike.CounterStrike;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public interface IThrowableListener extends Listener {

    void onPlayerThrow(PlayerInteractEvent event);

    void onProjectileHit(ProjectileHitEvent event);

    default Vector getSurfaceNormal(ProjectileHitEvent event) {
        // 这里假设撞击表面的法线方向是撞击位置的Y轴方向
        // 你可以根据实际情况调整这个方法
        return new Vector(0, 1, 0);
    }


    default boolean isLineOfSightClear(Location from, Location to) {
        RayTraceResult result = from.getWorld().rayTraceBlocks(from, to.toVector().subtract(from.toVector()), from.distance(to));
        return result == null || result.getHitBlock() == null;
    }

    private boolean isPathClear(Location start, Location end) {
        double distance = start.distance(end);
        double step = 0.1;
        for (double i = 0; i <= distance; i += step) {
            double x = start.getX() + (end.getX() - start.getX()) * (i / distance);
            double y = start.getY() + (end.getY() - start.getY()) * (i / distance);
            double z = start.getZ() + (end.getZ() - start.getZ()) * (i / distance);
            Location checkLocation = new Location(start.getWorld(), x, y, z);
            if (checkLocation.getBlock().getType() != Material.AIR) {
                CounterStrike.instance.getLogger().info("Path is not clear");
                return false;
            }
        }
        return true;
    }
}