package io.github.miaow233.counterstrike.commands;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.managers.PlayerManager;
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


        PlayerManager.getInstance().addPlayer(player);
        player.sendMessage("You joined the game!");

        // 如果房间已满，则开始游戏
        //if (GameManager.getInstance().isFull()){
        //    GameManager.getInstance().startGame();
        //}

        return true;
    }
}