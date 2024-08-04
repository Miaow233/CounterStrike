package io.github.miaow233.counterstrike.commands;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.GameState;
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

        String action = args[1];

        if (action.equalsIgnoreCase("list")) {
            sender.sendMessage("地图列表:");
            for (MapConfig mapConfig : plugin.getMapManager().getMaps().values()) {
                sender.sendMessage(mapConfig.getName());
            }
            return true;
        }
        String name = args[2];
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can execute this command.");
            return false;
        }

        switch (action.toLowerCase()) {
            case "create":
            case "new":
                plugin.getMapManager().createMap(name);
                plugin.getMapManager().selectMap(name);
                player.sendMessage("成功创建名称为" + name + "的地图.");
                return true;

            case "remove":
            case "delete":
            case "del":
            case "rm":
                plugin.getMapManager().removeMap(name);
                player.sendMessage("成功删除名称为" + name + "的地图.");
                return true;

            case "select":
            case "sel":
                if (plugin.getGameState() == GameState.IN_GAME) {
                    player.sendMessage("对局正在进行中，你可以中途加入或者等待对局结束");
                    return true;
                }
                boolean selected = plugin.getMapManager().selectMap(name);
                if (!selected) {
                    player.sendMessage("地图" + name + "不存在.");
                    return true;
                }
                player.sendMessage("已选择" + name);
                return true;

            case "setspawn":
            case "sets":
            case "ss":
                Location location = player.getLocation();
                MapConfig mapConfig = mapManager.getSelectedMap();
                if (mapConfig == null) {
                    player.sendMessage("请先选择地图");
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
                return true;

            default:
                player.sendMessage("Invalid action. Use create, sel, or setspawn.");
                player.sendMessage("Usage: /csmc map <create|remove|select> <name>");
                return false;
        }
    }

}
