package io.github.miaow233.counterstrike.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerViewListener implements Listener {

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        // 如果需要，你可以在这里添加更多逻辑
        event.setCancelled(true);
        event.getPlayer().sendMessage("Switching perspectives is not allowed!");
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        // 如果需要，你可以在这里添加更多逻辑
        event.setCancelled(true);
        event.getPlayer().sendMessage("Switching perspectives is not allowed!");
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase();
        if (message.startsWith("/tp") || message.startsWith("/gamemode")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Switching perspectives is not allowed!");
        }
    }
}
