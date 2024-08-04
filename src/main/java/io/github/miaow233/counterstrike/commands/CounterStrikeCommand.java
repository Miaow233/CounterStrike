package io.github.miaow233.counterstrike.commands;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.GameState;
import io.github.miaow233.counterstrike.managers.GameManager;
import io.github.miaow233.counterstrike.managers.PlayerManager;
import io.github.miaow233.counterstrike.utils.PacketUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CounterStrikeCommand implements CommandExecutor {

    private final CounterStrike plugin;
    private Map<String, CommandExecutor> handlers = new HashMap<>();


    public CounterStrikeCommand(CounterStrike plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // 没有参数，显示帮助信息
        // <prefix>
        if (args.length == 0) {
            sender.sendMessage("Usage: /cs <join>");
            return false;
        }

        String action = args[0];

        registerHandler("join", new JoinCommand(plugin));
        registerHandler("map", new MapCommand(plugin));
        registerHandler("coin", new CoinCommand(plugin));


        // <prefix> start
        if (action.equalsIgnoreCase("start")) {

            if (plugin.getGameState() == GameState.IN_GAME) {
                sender.sendMessage(ChatColor.RED + "游戏已经开始");
                return true;
            }

            if (plugin.getMapManager().getSelectedMap() == null) {
                sender.sendMessage(ChatColor.RED + "请先选择地图");
                return true;
            }

            AtomicInteger remainingTime = new AtomicInteger(5);

            // 5s 后开始游戏
            int timerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                PacketUtils.sendTitleAndSubtitleToInGame("游戏即将开始", ChatColor.RED + String.valueOf(remainingTime.getAndDecrement()), 0, 1, 0);
            }, 0, 20);

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                Bukkit.getScheduler().cancelTask(timerId);
                GameManager.getInstance().startGame();
            }, 20 * 5);

            plugin.setGameState(GameState.IN_GAME);

            return true;
        }

        // <prefix> end
        if (action.equalsIgnoreCase("end")) {
            if (CounterStrike.instance.getGameState() != GameState.IN_GAME) {
                sender.sendMessage(ChatColor.RED + "游戏尚未开始");
                return true;
            }
            sender.sendMessage("结束游戏");
            GameManager.getInstance().endGame();
            return true;
        }

        if (handlers.containsKey(action)) {
            return handle(action, sender, command, label, args);
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can execute this command.");
            return false;
        }

        // <prefix> exit
        if (action.equalsIgnoreCase("exit")) {
            player.sendMessage("退出游戏");
            PlayerManager.getInstance().removePlayer(player);
            return true;
        }

        sender.sendMessage("Unknown command.");
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