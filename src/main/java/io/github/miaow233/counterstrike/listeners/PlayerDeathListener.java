package io.github.miaow233.counterstrike.listeners;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.managers.PlayerManager;
import io.github.miaow233.counterstrike.models.GamePlayer;
import io.github.miaow233.counterstrike.models.Team;
import io.github.miaow233.counterstrike.utils.PacketUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;


public class PlayerDeathListener implements Listener {

    private final CounterStrike plugin;

    public PlayerDeathListener(CounterStrike plugin) {
        this.plugin = plugin;

    }


    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event) {
        Player victim = event.getEntity();

        // 清除凋落物
        for (ItemStack it : event.getDrops()) {
            if (!it.getType().equals(Material.TNT)) {
                it.setType(Material.AIR);
            }
        }


        GamePlayer gamePlayerVictim = PlayerManager.getInstance().getGamePlayer(victim);

        if (gamePlayerVictim == null) {
            return;
        }
        victim.spigot().respawn();

        String deadPlayerName = (gamePlayerVictim.getTeam().equals(Team.COUNTER_TERRORISTS)) ? ChatColor.BLUE + victim.getName() : ChatColor.RED + victim.getName();
        victim.setHealth(victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        victim.setGameMode(GameMode.SPECTATOR);


        victim.sendMessage(ChatColor.RED + "Wait until next round for a respawn.");
        PacketUtils.sendTitleAndSubtitle(victim, ChatColor.RED + "You are dead.", ChatColor.YELLOW + "You will respawn in the next round.", 0, 3, 1);

        try {
            // 击杀者
            Player killer = victim.getKiller();
            GamePlayer gamePlayerKiller = PlayerManager.getInstance().getGamePlayer(killer);

            String killerName = (gamePlayerKiller.getTeam().equals(Team.COUNTER_TERRORISTS)) ? ChatColor.BLUE + killer.getName() : ChatColor.RED + killer.getName();
            gamePlayerKiller.addCoins(300);
            killer.sendMessage(ChatColor.GREEN + "+ $300");

            gamePlayerKiller.addKills(1);
            gamePlayerVictim.addDeaths(1);
            //gamePlayerKiller.settempMVP(csplayerKiller.gettempMVP() + 1);

            // 设置玩家为旁观者模式并将其传送到击杀者附近
            victim.teleport(killer.getLocation().add(0, 2, 0));

            //victim.setSpectatorTarget(killer);

            // 2秒后锁定视角到队友
            new BukkitRunnable() {
                @Override
                public void run() {
                    Player teammate = findTeammate(victim);
                    if (teammate != null) {
                        victim.setSpectatorTarget(teammate);
                    }
                }
            }.runTaskLater(CounterStrike.instance, 40L); // 2秒 = 40 ticks

            //event.setDeathMessage(ChatColor.valueOf(gamePlayerVictim.getColour()) + deadPlayerName + ChatColor.GRAY + " was killed by " + ChatColor.valueOf(gamePlayerKiller.getColour()) + killerName);

        } catch (NullPointerException e) {
            gamePlayerVictim.setDeaths(gamePlayerVictim.getDeaths() + 1);
            event.setDeathMessage(deadPlayerName + ChatColor.YELLOW + " was killed.");
        }

        // Check if every player on dead player team is dead
        //CSUtil.checkForDead();
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

        // 然后找到队友
        Map<Player, GamePlayer> players = PlayerManager.getInstance().getPlayers();
        for (Map.Entry<Player, GamePlayer> entry : players.entrySet()) {
            if (entry.getValue().getTeam().equals(team)
                    && entry.getKey().getGameMode() != GameMode.SPECTATOR) {
                return entry.getKey();
            }
        }
        return null;
    }
}