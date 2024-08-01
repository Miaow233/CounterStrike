package io.github.miaow233.counterstrike;

import io.github.miaow233.counterstrike.commands.CounterStrikeCommand;
import io.github.miaow233.counterstrike.listeners.*;
import io.github.miaow233.counterstrike.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;


public final class CounterStrike extends JavaPlugin {

    public static CounterStrike instance;

    private MapManager mapManager;
    private TeamManager teamManager;
    private GameState gameState;

    @Override
    public void onEnable() {

        instance = this;
        teamManager = new TeamManager(this);

        // Register commands
        this.getCommand("csmc").setExecutor(new CounterStrikeCommand(this));


        // Register listeners
        getServer().getPluginManager().registerEvents(new CustomProjectileListener(this), this);
        getServer().getPluginManager().registerEvents(new FlashingListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new SmokeGrenadeListeners(this), this);

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

    public void setup() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        this.mapManager = new MapManager(dataFolder);
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
