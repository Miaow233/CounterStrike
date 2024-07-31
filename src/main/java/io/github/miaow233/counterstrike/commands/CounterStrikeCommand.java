package io.github.miaow233.counterstrike.commands;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.items.Flashing;
import io.github.miaow233.counterstrike.items.SmokeGrenade;
import io.github.miaow233.counterstrike.managers.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CounterStrikeCommand implements CommandExecutor {

    private final CounterStrike plugin;
    private Map<String, CommandExecutor> handlers = new HashMap<>();


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
        // <prefix>
        if (args.length == 0) {
            sender.sendMessage("Usage: /cs <join>");
            return false;
        }

        String action = args[0];

        registerHandler("join", new JoinCommand(plugin));
        registerHandler("map", new MapCommand(plugin));

        // FlashingCommand
        if (action.equalsIgnoreCase("flashing")) {
            player.getInventory().addItem(new Flashing());
            return true;
        }

        // SmokeGrenadeCommand
        if (action.equalsIgnoreCase("smokegrenade")) {
            player.getInventory().addItem(new SmokeGrenade());
            return true;
        }

        // <prefix> start
        if (action.equalsIgnoreCase("start")) {
            player.sendMessage("开始游戏");
            GameManager.getInstance().startGame();
            return true;
        }

        // <prefix> exit
        if (action.equalsIgnoreCase("exit")) {

        }

        if (handlers.containsKey(action)) {
            return handle(action, sender, command, label, args);
        }

        player.sendMessage("Unknown command.");
        return false;
    }

    private void registerHandler(String action, CommandExecutor handler) {
        this.handlers.put(action, handler);
    }

    public Map<String, CommandExecutor> getHandlers() {
        return this.handlers;
    }

    public void setHandlers(Map<String, CommandExecutor> handlers) {
        this.handlers = handlers;
    }

    public boolean handle(String action,
                          CommandSender sender,
                          Command command,
                          String label,
                          String[] args) {
        CommandExecutor handler = this.handlers.get(action);
        if (handler == null) {
            return false;
        }
        return handler.onCommand(sender, command, label, args);
    }
}