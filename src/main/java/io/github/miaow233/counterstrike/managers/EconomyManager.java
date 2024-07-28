package io.github.miaow233.counterstrike.managers;

import io.github.miaow233.counterstrike.CounterStrike;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class EconomyManager {

    private static EconomyManager instance;
    private final CounterStrike plugin;
    private final Map<Player, Integer> playerCoins;

    private EconomyManager(CounterStrike plugin) {
        this.plugin = plugin;
        this.playerCoins = new HashMap<>();
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
        updatePlayerCoins(player, currentCoins + amount);
    }

    public void removeCoins(Player player, int amount) {
        int currentCoins = playerCoins.getOrDefault(player, 0);
        playerCoins.put(player, currentCoins - amount);
        updatePlayerCoins(player, currentCoins - amount);
    }

    public int getCoins(Player player) {
        return playerCoins.getOrDefault(player, 0);
    }

    public void updatePlayerCoins(Player player, int newAmount) {
        playerCoins.put(player, newAmount);
        // 这里可以添加更多的全局逻辑，比如更新数据库或通知其他系统
    }
}
