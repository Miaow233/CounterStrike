package io.github.miaow233.counterstrike.commands;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.managers.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinCommand implements CommandExecutor {

    public CoinCommand(CounterStrike plugin) {

    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        // <prefix> coin <add|remove> <player> <amount>
        // <prefix> coin clear <player>
        if (args.length == 0) {
            sender.sendMessage("§cUsage: /cs coin <add|remove> <player> <amount>");
            return true;
        }

        String action = args[1];
        String playerName = args[2];
        Player target = Bukkit.getPlayer(playerName);

        if (action.equalsIgnoreCase("clear")) {
            EconomyManager.getInstance().updatePlayerCoins(target, 0);
            return true;
        }

        int amount = Integer.parseInt(args[3]);
        switch (action) {
            case "add":
                EconomyManager.getInstance().addCoins(target, amount);
                return true;
            case "remove":
                EconomyManager.getInstance().removeCoins(target, amount);
                return true;
            default:
                sender.sendMessage("§cUsage: /cs coin <add|remove> <player> <amount>");
                return false;
        }
    }
}
