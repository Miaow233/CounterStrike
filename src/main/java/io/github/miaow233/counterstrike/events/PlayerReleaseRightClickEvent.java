package io.github.miaow233.counterstrike.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerReleaseRightClickEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final long chargeTime;

    public PlayerReleaseRightClickEvent(Player player, long chargeTime) {
        this.player = player;
        this.chargeTime = chargeTime;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

    public long getChargeTime() {
        return chargeTime;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
