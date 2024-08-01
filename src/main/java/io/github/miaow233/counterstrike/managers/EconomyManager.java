package io.github.miaow233.counterstrike.managers;

import io.github.miaow233.counterstrike.CounterStrike;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;
import java.util.Map;

public class EconomyManager {

    private static EconomyManager instance;
    private final CounterStrike plugin;
    private final Map<Player, Integer> playerCoins;

    private final ScoreboardManager scoreboardManager;

    private Objective objective;

    private EconomyManager(CounterStrike plugin) {
        this.plugin = plugin;
        this.playerCoins = new HashMap<>();

        // 使用计分板管理玩家金币
        scoreboardManager = plugin.getServer().getScoreboardManager();

        //scoreboardManager.getMainScoreboard().getObjective("lrhud.score").unregister();

        objective = scoreboardManager.getMainScoreboard().getObjective("lrhud.score");

        if (objective == null) {
            // 注册计分板
            objective = scoreboardManager.getMainScoreboard().registerNewObjective("lrhud.score", Criteria.DUMMY, "金币");
        }
    }

    public static void initialize(CounterStrike plugin) {
        if (instance == null) {
            instance = new EconomyManager(plugin);
        }
    }

    public static EconomyManager getInstance() {
        return instance;
    }

    public void addCoins(Player player, int amount) {
        int currentCoins = playerCoins.getOrDefault(player, 0);
        playerCoins.put(player, currentCoins + amount);
        objective.getScore(player.getName()).setScore(currentCoins + amount);
        updatePlayerCoins(player, currentCoins + amount);
    }

    public void removeCoins(Player player, int amount) {
        int currentCoins = playerCoins.getOrDefault(player, 0);
        playerCoins.put(player, currentCoins - amount);
        objective.getScore(player.getName()).setScore(currentCoins - amount);
        updatePlayerCoins(player, currentCoins - amount);
    }

    public int getCoins(Player player) {
        return playerCoins.getOrDefault(player, 0);
    }

    public void updatePlayerCoins(Player player, int newAmount) {
        playerCoins.put(player, newAmount);
        objective.getScore(player.getName()).setScore(newAmount);
    }
}
