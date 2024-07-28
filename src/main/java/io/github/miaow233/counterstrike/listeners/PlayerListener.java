package io.github.miaow233.counterstrike.listeners;


import io.github.miaow233.counterstrike.CounterStrike;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final CounterStrike plugin;

    public PlayerListener(CounterStrike plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Handle player join logic
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Handle player quit logic
    }
}
