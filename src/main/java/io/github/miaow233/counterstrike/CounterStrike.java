package io.github.miaow233.counterstrike;

import io.github.miaow233.counterstrike.commands.CounterStrikeCommand;
import io.github.miaow233.counterstrike.listeners.PlayerDeathListener;
import io.github.miaow233.counterstrike.listeners.PlayerListener;
import io.github.miaow233.counterstrike.managers.*;
import io.github.miaow233.counterstrike.utils.CommandUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.scoreboard.Objective;

import java.io.File;


@Dependency("Vault")
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
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

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

        teamManager.deleteTeam("TERRORISTS");
        teamManager.deleteTeam("COUNTER_TERRORISTS");

        this.getServer().getScoreboardManager().getMainScoreboard().getObjectives().forEach(Objective::unregister);
    }

    public void setup() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        this.mapManager = new MapManager(dataFolder);
        CommandUtils.runCommandAsConsole("lrhud hostile set COUNTER_TERRORISTS TERRORISTS");
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
