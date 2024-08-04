package io.github.miaow233.counterstrike.commands;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.GameState;
import io.github.miaow233.counterstrike.managers.EconomyManager;
import io.github.miaow233.counterstrike.managers.PlayerManager;
import io.github.miaow233.counterstrike.managers.RoundManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {

    private final CounterStrike plugin;

    public JoinCommand(CounterStrike plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can execute this command!");
            return false;
        }

        if (PlayerManager.getInstance().getGamePlayer(player) != null) {
            player.sendMessage(ChatColor.RED + "你已经在游戏中了");
            return true;
        }

        PlayerManager.getInstance().addPlayer(player);
        player.sendMessage(ChatColor.GOLD + "你加入了游戏");

        // 中途加入
        if (plugin.getGameState() == GameState.IN_GAME) {
            RoundManager.getInstance().respawnPlayer(player);
        }

        EconomyManager.getInstance().setCoins(player, 0);

        return true;
    }
}