package io.github.miaow233.counterstrike.listeners;


import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.GameState;
import io.github.miaow233.counterstrike.managers.PlayerManager;
import io.github.miaow233.counterstrike.models.GamePlayer;
import io.github.miaow233.counterstrike.models.Team;
import io.github.miaow233.counterstrike.utils.CommandUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

import java.util.Date;

public class PlayerListener implements Listener {

    private final CounterStrike plugin;
    private Date lastMoveCheck = new Date();

    public PlayerListener(CounterStrike plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // 当有玩家连接时初始化
        // 防止因地图未完全加载报错
        if (plugin.getMapManager() == null) {
            plugin.setup();
        }

        // 清除玩家队伍
        Player player = event.getPlayer();
        CommandUtils.runCommandAsConsole("team leave " + player.getName());

        //
        player.setGameMode(GameMode.ADVENTURE);

        // 更改玩家权限
        //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user %s permission set csmc.play true".formatted(player.getName()));

        // 传送至主世界
        //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvtp %s world".formatted(player.getName()));
        player.teleport(plugin.getServer().getWorld("world").getSpawnLocation());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

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
        if (PlayerManager.getInstance().getGamePlayer(player) == null) return;
        // 似人不能说话
        if (player.getGameMode() == GameMode.SPECTATOR) {
            chatEvent.setCancelled(true);
        }
    }

    // 锁定玩家饥饿值
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        // 如果玩家没有进行游戏
        if (PlayerManager.getInstance().getGamePlayer(player) == null) return;
        event.setCancelled(true);
    }

    // 阻止玩家购买物品
    @EventHandler
    public void onPlayerOpenShop(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (event.getMessage().equalsIgnoreCase("/csmc shop")) {
            // 如果玩家没有进行游戏
            // 在游戏外为打开背包
            if (PlayerManager.getInstance().getGamePlayer(player) == null) {
                event.setMessage("/dm open swm_home");
                return;
            }

            // 如果在商店状态，则可以正常购买
            if (plugin.getGameState() == GameState.SHOP) {
                event.setMessage("/dm open demomenu");
                return;
            }

            // 阻止玩家打开商店
            event.setCancelled(true);
        }
    }

    // 禁止玩家移动
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // 防抖
        if (new Date().getTime() - lastMoveCheck.getTime() < 500) return;
        lastMoveCheck = new Date();

        Player player = event.getPlayer();
        // 如果玩家没有进行游戏
        GamePlayer gamePlayer = PlayerManager.getInstance().getGamePlayer(player);
        if (gamePlayer == null) return;
        // 如果在商店状态
        if (plugin.getGameState() == GameState.SHOP) {
            Location spawnLocation;
            if (gamePlayer.getTeam() == Team.TERRORISTS) {
                spawnLocation = plugin.getMapManager().getSelectedMap().getTSpawn();
            } else {
                spawnLocation = plugin.getMapManager().getSelectedMap().getCTSpawn();
            }
            Location playerLocation = event.getTo();
            spawnLocation.setYaw(playerLocation.getYaw());
            spawnLocation.setPitch(playerLocation.getPitch());
            player.teleport(spawnLocation);
            //event.setCancelled(true);
        }
    }

    // 禁止玩家破坏
    @EventHandler
    public void onPlayerDestroyBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("csmc.destroy")) {
            event.setCancelled(true);
        }
    }
}
