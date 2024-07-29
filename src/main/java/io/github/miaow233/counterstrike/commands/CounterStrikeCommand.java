package io.github.miaow233.counterstrike.commands;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.items.Flashing;
import io.github.miaow233.counterstrike.items.SmokeGrenade;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CounterStrikeCommand implements CommandExecutor {

    private final CounterStrike plugin;

    public CounterStrikeCommand(CounterStrike plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can execute this command.");
            return false;
        }

        // 没有参数，显示帮助信息
        if (args.length == 0) {
            sender.sendMessage("Usage: /cs <join>");
            return false;
        }

        // JoinCommand
        if (args[0].equalsIgnoreCase("join")) {


        }

        // SetLobbyCommand
        if (args[0].equalsIgnoreCase("setlobby")) {
            plugin.getConfig().set("lobby.location", player.getLocation());
            plugin.saveConfig();
            player.sendMessage("Lobby location set!");
            return true;
        }


        // SetTeamSpawnCommand
        if (args[0].equalsIgnoreCase("setteamspawn")) {
            if (args.length != 2) {
                sender.sendMessage("Usage: /setteamspawn <team>");
                return false;
            }
            String team = args[1];
            plugin.getConfig().set("teams." + team + ".spawn", player.getLocation());
            plugin.saveConfig();
            player.sendMessage("Spawn location for team " + team + " set!");
            return true;
        }

        // FlashingCommand
        if (args[0].equalsIgnoreCase("flashing")) {
            player.getInventory().addItem(new Flashing());
            return true;
        }

        // SmokeGrenadeCommand
        if (args[0].equalsIgnoreCase("smokegrenade")) {
            player.getInventory().addItem(new SmokeGrenade());
            return true;
        }

        player.sendMessage("Unknown command.");
        return false;
    }
}