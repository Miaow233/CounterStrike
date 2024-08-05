package io.github.miaow233.counterstrike.managers;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.GameState;
import io.github.miaow233.counterstrike.MapConfig;
import io.github.miaow233.counterstrike.models.Team;
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
            selectedMap = plugin.getMapManager().getMaps().get("nuke");
        }

        Location tSpawn = selectedMap.getTSpawn();
        Location ctSpawn = selectedMap.getCTSpawn();

        PlayerManager.getInstance().getPlayers().forEach((player, gamePlayer) -> {
            if (gamePlayer.getTeam() == Team.TERRORISTS) {
                player.teleport(tSpawn);
                plugin.getLogger().info("已将 " + player.getName() + " 传送至 terrorists 队伍");
            } else {
                player.teleport(ctSpawn);
                plugin.getLogger().info("已将 " + player.getName() + " 传送至 counter-terrorists 队伍");
            }

            player.getInventory().clear();
        });

        RoundManager.getInstance().startRounds();
    }

    public void endGame() {
        if (CounterStrike.instance.getGameState() != GameState.IN_GAME) return;

        plugin.getLogger().info("游戏结束，执行后续清理步骤");
        RoundManager.getInstance().stopRounds();

        // 移除所有玩家
        PlayerManager.getInstance().getPlayers().forEach((player, gamePlayer) -> PlayerManager.getInstance().removePlayer(player));

        CounterStrike.instance.setGameState(GameState.END);
    }
}

