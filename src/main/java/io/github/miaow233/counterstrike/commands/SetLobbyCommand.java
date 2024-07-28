package io.github.miaow233.counterstrike.commands;


import io.github.miaow233.counterstrike.CounterStrike;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SetLobbyCommand implements CommandExecutor {

    private final CounterStrike plugin;

    public SetLobbyCommand(CounterStrike plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            plugin.getConfig().set("lobby.location", player.getLocation());
            plugin.saveConfig();
            player.sendMessage("Lobby location set!");
            return true;
        }
        return false;
    }
}
