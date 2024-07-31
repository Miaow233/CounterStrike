package io.github.miaow233.counterstrike.commands;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.MapConfig;
import io.github.miaow233.counterstrike.managers.MapManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MapCommand implements CommandExecutor {

    private final CounterStrike plugin;
    private final MapManager mapManager;

    public MapCommand(CounterStrike plugin) {
        this.plugin = plugin;
        this.mapManager = plugin.getMapManager();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can execute this command.");
            return false;
        }

        if (args.length != 3) {
            sender.sendMessage("Usage: /csmc map <create|remove|select> <name>");
            return false;
        }

        String action = args[1];
        String name = args[2];

        switch (action.toLowerCase()) {
            case "create":
                plugin.getMapManager().createMap(name);
                plugin.getMapManager().selectMap(name);
                player.sendMessage("成功创建名称为" + name + "的地图.");
                break;
            case "remove":
            case "delete":
            case "del":
            case "rm":
                plugin.getMapManager().removeMap(name);
                player.sendMessage("成功删除名称为" + name + "的地图.");
                break;
            case "select":
            case "sel":
                plugin.getMapManager().selectMap(name);
                player.sendMessage("已选择" + name);
                break;
            case "setspawn":
            case "sets":
                Location location = player.getLocation();
                MapConfig mapConfig = mapManager.getSelectedMap();
                if (mapConfig == null) {
                    player.sendMessage("No map selected.");
                }
                if (name.equalsIgnoreCase("T")) {
                    mapConfig.setTSpawn(location);
                    player.sendMessage("T spawn set.");
                } else if (name.equalsIgnoreCase("CT")) {
                    mapConfig.setCTSpawn(location);
                    player.sendMessage("CT spawn set.");
                } else {
                    player.sendMessage("Invalid team. Use T or CT.");
                }
                mapManager.saveMaps(); // 保存地图配置
                break;
            case "list":
                player.sendMessage("地图列表:");
                for (String mapName : plugin.getMapManager().getMaps().keySet()) {
                    player.sendMessage(mapName);
                }
                break;
            default:
                player.sendMessage("Invalid action. Use create, sel, or setspawn.");
                break;
        }

        return true;
    }

}
