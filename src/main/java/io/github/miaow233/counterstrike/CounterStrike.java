package io.github.miaow233.counterstrike;

import io.github.miaow233.counterstrike.commands.JoinCommand;
import io.github.miaow233.counterstrike.commands.SetLobbyCommand;
import io.github.miaow233.counterstrike.commands.SetTeamSpawnCommand;
import io.github.miaow233.counterstrike.listeners.PlayerListener;
import io.github.miaow233.counterstrike.listeners.PlayerViewListener;
import io.github.miaow233.counterstrike.managers.EconomyManager;
import io.github.miaow233.counterstrike.managers.GameManager;
import io.github.miaow233.counterstrike.managers.PlayerManager;
import io.github.miaow233.counterstrike.managers.RoundManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class CounterStrike extends JavaPlugin {


    @Override
    public void onEnable() {
        // Register commands
        this.getCommand("setlobby").setExecutor(new SetLobbyCommand(this));
        this.getCommand("setteamspawn").setExecutor(new SetTeamSpawnCommand(this));
        this.getCommand("join").setExecutor(new JoinCommand(this));


        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerViewListener(), this);

        // Initialize managers
        GameManager.initialize(this);
        PlayerManager.initialize(this);
        EconomyManager.initialize(this);
    }

    @Override
    public void onDisable() {
        // Cleanup logic if necessary

        RoundManager.getInstance().stopRounds();
    }
}
