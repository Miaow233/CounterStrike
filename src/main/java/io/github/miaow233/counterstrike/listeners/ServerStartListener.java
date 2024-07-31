package io.github.miaow233.counterstrike.listeners;


import io.github.miaow233.counterstrike.CounterStrike;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class ServerStartListener implements Listener {
    @EventHandler
    public void onServerStart(ServerLoadEvent event) {
        CounterStrike.instance.setup();
    }
}