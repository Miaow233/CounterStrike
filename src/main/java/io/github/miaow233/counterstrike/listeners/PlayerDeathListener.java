package io.github.miaow233.counterstrike.listeners;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.GameState;
import io.github.miaow233.counterstrike.managers.DataManager;
import io.github.miaow233.counterstrike.managers.PlayerManager;
import io.github.miaow233.counterstrike.managers.RoundManager;
import io.github.miaow233.counterstrike.models.GamePlayer;
import io.github.miaow233.counterstrike.models.Team;
import io.github.miaow233.counterstrike.utils.PacketUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Map;


public class PlayerDeathListener implements Listener {

    private final CounterStrike plugin;

    public PlayerDeathListener(CounterStrike plugin) {
        this.plugin = plugin;

    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();

        // 将非游戏玩家传送到主世界避免出现问题
        if (CounterStrike.instance.getGameState() != GameState.IN_GAME) {
            victim.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());
        }

        GamePlayer gamePlayerVictim = PlayerManager.getInstance().getGamePlayer(victim);

        // 是否是对局内玩家
        if (gamePlayerVictim == null) return;

        // 防止因双方同时死亡重复判定
        if (plugin.getGameState() == GameState.ROUND_END) return;


        String deadPlayerName = (gamePlayerVictim.getTeam().equals(Team.COUNTER_TERRORISTS)) ? ChatColor.BLUE + victim.getName() : ChatColor.RED + victim.getName();
        victim.setHealth(victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        victim.setGameMode(GameMode.SPECTATOR);

        PacketUtils.sendTitleAndSubtitle(victim, ChatColor.RED + "你死了", ChatColor.YELLOW + "下一回合重生", 0, 3, 1);

        try {
            // 击杀者
            Player killer = victim.getKiller();

            GamePlayer gamePlayerKiller = PlayerManager.getInstance().getGamePlayer(killer);

            String killerName = (gamePlayerKiller.getTeam().equals(Team.COUNTER_TERRORISTS)) ? ChatColor.BLUE + killer.getName() : ChatColor.RED + killer.getName();
            gamePlayerKiller.addCoins(300);


            // 根据不同的武器获得的金币不同
            // 先按固定金额给
            killer.sendMessage(ChatColor.GREEN + "+ $300");

            gamePlayerKiller.addKills(1);
            gamePlayerVictim.addDeaths(1);

            // 设置玩家为旁观者模式并将其传送到击杀者附近
            lookKiller(victim, killer);

            // 3 秒后锁定视角到队友
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Player teammate = findTeammate(victim);
                if (teammate != null) {
                    victim.setSpectatorTarget(teammate);
                }
            }, 3 * 20L);

        } catch (NullPointerException e) {
            gamePlayerVictim.setDeaths(gamePlayerVictim.getDeaths() + 1);
        }

        // 检查胜利条件
        Team victimTeam = gamePlayerVictim.getTeam();

        if (PlayerManager.getInstance().getAliveCount(victimTeam) < 1) {

            String winnerTeamName = victimTeam.equals(Team.COUNTER_TERRORISTS) ? "T" : "CT";
            ChatColor winnerTeamColor = victimTeam.equals(Team.COUNTER_TERRORISTS) ? ChatColor.RED : ChatColor.BLUE;
            String winnerTeamDisplay = winnerTeamColor + winnerTeamName;

            PacketUtils.sendTitleAndSubtitleToInGame(winnerTeamDisplay + ChatColor.WHITE + "阵营获胜", "", 1, 4, 1);
            DataManager.addScore(winnerTeamName, "csmc.teamWins", 1);

            // 根据阵营不同给予不同的金币
            PlayerManager.getInstance().getPlayers().values().forEach(player -> {
                int rewardCoin = player.getTeam().equals(victimTeam) ? 1400 : 3250; // 假设获胜阵营奖励100金币，失败阵营奖励50金币
                player.addCoins(rewardCoin);
            });


            plugin.setGameState(GameState.ROUND_END);

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                RoundManager.getInstance().endRound(true);
            }, 4 * 20L);
        }
    }

    private void lockToTeammate(Player player) {
        // 找到队友
        Player teammate = findTeammate(player);
        if (teammate != null) {
            player.setSpectatorTarget(teammate);
        }
    }

    private Player findTeammate(Player player) {
        // 先判断玩家是哪个队伍
        Team team = PlayerManager.getInstance().getGamePlayer(player).getTeam();

        // 找到队友
        Map<Player, GamePlayer> players = PlayerManager.getInstance().getPlayers();
        for (Map.Entry<Player, GamePlayer> entry : players.entrySet()) {
            if (entry.getValue().getTeam().equals(team)
                    && entry.getKey().getGameMode() != GameMode.SPECTATOR) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void lookKiller(Player victim, Player killer) {
        Location victimLocation = victim.getLocation();
        Location killerLocation = killer.getLocation();

        float yaw = calculateYaw(victimLocation, killerLocation);
        float pitch = calculatePitch(victimLocation, killerLocation);

        victimLocation.setYaw(yaw);
        victimLocation.setPitch(pitch);

        victim.teleport(victimLocation);
    }

    private float calculateYaw(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        double yaw = Math.atan2(dz, dx);
        return (float) Math.toDegrees(yaw) - 90;
    }

    private float calculatePitch(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double dz = to.getZ() - from.getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);
        double pitch = Math.atan2(dy, distance);
        return (float) Math.toDegrees(pitch);
    }
}