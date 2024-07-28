package io.github.miaow233.counterstrike.commands;

import io.github.miaow233.counterstrike.CounterStrike;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTeamSpawnCommand implements CommandExecutor {

    private final CounterStrike plugin;

    public SetTeamSpawnCommand(CounterStrike plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length != 1) {
                sender.sendMessage("Usage: /setteamspawn <team>");
                return false;
            }
            String team = args[0];
            plugin.getConfig().set("teams." + team + ".spawn", player.getLocation());
            plugin.saveConfig();
            player.sendMessage("Spawn location for team " + team + " set!");
            return true;
        }
        return false;
    }
}
