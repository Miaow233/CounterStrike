package io.github.miaow233.counterstrike;

import io.github.miaow233.counterstrike.commands.CounterStrikeCommand;
import io.github.miaow233.counterstrike.listeners.FlashingListener;
import io.github.miaow233.counterstrike.listeners.PlayerListener;
import io.github.miaow233.counterstrike.managers.EconomyManager;
import io.github.miaow233.counterstrike.managers.GameManager;
import io.github.miaow233.counterstrike.managers.PlayerManager;
import io.github.miaow233.counterstrike.managers.RoundManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class CounterStrike extends JavaPlugin {

    public static CounterStrike instance;

    @Override
    public void onEnable() {

        instance = this;

        // Register commands
        this.getCommand("csmc").setExecutor(new CounterStrikeCommand(this));


        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new FlashingListener(this), this);


        // Initialize managers
        GameManager.initialize(this);
        PlayerManager.initialize(this);
        EconomyManager.initialize(this);
    }

    @Override
    public void onDisable() {
        // Cleanup logic if necessary

        try {
            RoundManager.getInstance().stopRounds();
        } catch (Exception e) {
            this.getLogger().warning("Failed to stop rounds: " + e.getMessage());
        }
    }
}
