package io.github.miaow233.counterstrike.managers;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.MapConfig;
import io.github.miaow233.counterstrike.models.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class GameManager {

    private static GameManager instance;
    private final CounterStrike plugin;

    private GameManager(CounterStrike plugin) {
        this.plugin = plugin;
    }

    public static void initialize(CounterStrike plugin) {
        if (instance == null) {
            instance = new GameManager(plugin);
        }
    }

    public static GameManager getInstance() {
        return instance;
    }

    public void startGame() {
        // 13 回合，每回合 2 分钟
        RoundManager.initialize(plugin, 120, 13);

        MapConfig selectedMap = plugin.getMapManager().getSelectedMap();
        if (selectedMap == null) {
            plugin.getLogger().warning("未选择地图，将使用默认地图");
            selectedMap = plugin.getMapManager().getMaps().get("de_dust2");
        }

        Location tSpawn = selectedMap.getTSpawn();
        Location ctSpawn = selectedMap.getCTSpawn();

        PlayerManager.getInstance().getPlayers().forEach((player, gamePlayer) -> {
            if (gamePlayer.getTeam() == Team.TERRORISTS) {
                player.teleport(tSpawn);
                plugin.getLogger().info("已将 " + player.getName() + " 传送至terrorists 队伍");
            } else {
                player.teleport(ctSpawn);
                plugin.getLogger().info("已将 " + player.getName() + " 传送至counter-terrorists 队伍");
            }
        });


        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lrhud hostile COUNTER_TERRORISTS TERRORISTS");

        RoundManager.getInstance().startRounds();
    }

    public void endGame() {
        RoundManager.getInstance().stopRounds();
        // Additional logic to end the game
    }

    private Location getSpawnLocationForTeam(Team team) {
        if (team == Team.TERRORISTS) {
            return (Location) plugin.getConfig().get("teams.TERRORISTS.spawn");
        } else {
            return (Location) plugin.getConfig().get("teams.COUNTER_TERRORISTS.spawn");
        }
    }
}

