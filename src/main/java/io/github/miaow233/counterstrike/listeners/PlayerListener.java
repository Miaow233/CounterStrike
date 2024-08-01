package io.github.miaow233.counterstrike.listeners;


import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.GameState;
import io.github.miaow233.counterstrike.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final CounterStrike plugin;

    public PlayerListener(CounterStrike plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getMapManager() == null) {
            plugin.setup();
        }

        // 清除玩家队伍
        Player player = event.getPlayer();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/team leave " + player.getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        // 清除玩家队伍
        Player player = event.getPlayer();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/team leave " + player.getName());

        PlayerManager.getInstance().removePlayer(player);

    }

    // 在无敌时间内阻止攻击
    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (plugin.getGameState() == GameState.SHOP) {
            if (event.getDamager() instanceof Player attacker) {
                if (attacker.isInvulnerable()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent chatEvent) {

        Player player = chatEvent.getPlayer();

        // 如果玩家没有进行游戏
        if (PlayerManager.getInstance().getGamePlayer(player) == null) {
            return;
        }

        // 似人不能说话
        if (player.getGameMode() == GameMode.SPECTATOR) {
            // chat.callEvent();
            // chat.setMessage("");
            chatEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        // 如果玩家没有进行游戏
        if (PlayerManager.getInstance().getGamePlayer(player) == null) {
            return;
        }
        event.setCancelled(true);
    }
}
