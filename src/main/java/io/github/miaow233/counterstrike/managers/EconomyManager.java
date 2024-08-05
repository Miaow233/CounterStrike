package io.github.miaow233.counterstrike.managers;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.utils.CommandUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.Map;

public class EconomyManager {

    private static EconomyManager instance;
    private final CounterStrike plugin;

    private final String SCOREBOARD_COIN_KEY = "csmc.coins";
    private final Map<Player, Integer> playerCoins;

    Economy vaultEconomy;

    private EconomyManager(CounterStrike plugin) {
        this.plugin = plugin;
        this.playerCoins = new HashMap<>();

        if (!setupEconomy()) {
            plugin.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", plugin.getDescription().getName()));
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().severe("Vault is not installed!");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        vaultEconomy = rsp.getProvider();
        return vaultEconomy != null;
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
        int newAmount = currentCoins + amount;
        DataManager.setScore(player.getName(), SCOREBOARD_COIN_KEY, newAmount);
        updatePlayerCoins(player, newAmount);
    }

    public void removeCoins(Player player, int amount) {
        int currentCoins = playerCoins.getOrDefault(player, 0);
        int newAmount = currentCoins - amount;
        DataManager.setScore(player.getName(), SCOREBOARD_COIN_KEY, newAmount);
        updatePlayerCoins(player, newAmount);
    }

    public void setCoins(Player player, int amount) {
        int currentCoins = playerCoins.getOrDefault(player, 0);
        if (currentCoins < amount) {
            addCoins(player, amount - currentCoins);
        } else if (currentCoins > amount) {
            removeCoins(player, currentCoins - amount);
        }
    }

    public int getCoins(Player player) {
        return playerCoins.getOrDefault(player, 0);
    }

    public void updatePlayerCoins(Player player, int newAmount) {
        playerCoins.put(player, newAmount);
        CommandUtils.runCommandAsConsole("lrhud coin set %s %d".formatted(player.getName(), newAmount));
    }
}
