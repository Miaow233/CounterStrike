package io.github.miaow233.counterstrike.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandUtils {
    public static boolean runCommandAsConsole(String command) {
        boolean result = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        if (!result) {
            Bukkit.getLogger().warning("Failed to run command: " + command);
        }
        return result;
    }

    public static boolean runCommandAsPlayer(String command, Player player) {
        boolean result = Bukkit.dispatchCommand(player, command);
        if (!result) {
            Bukkit.getLogger().warning("Failed to run command: " + command);
        }
        return result;
    }
}
