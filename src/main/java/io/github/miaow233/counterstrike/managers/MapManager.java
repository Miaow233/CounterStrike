package io.github.miaow233.counterstrike.managers;

import io.github.miaow233.counterstrike.MapConfig;
import org.bukkit.Location;
import org.bukkit.configuration.MemorySection;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MapManager {

    private final Map<String, MapConfig> maps = new HashMap<>();
    private MapConfig selectedMap;
    private final ConfigManager configManager;

    public MapManager(File dataFolder) {
        configManager = new ConfigManager(new File(dataFolder, "maps.yml"));
        loadMaps();
    }
    public void createMap(String name) {
        if (maps.containsKey(name)) {
            return;
        }

        maps.put(name, new MapConfig(
                name,
                new Location(null, 0, 0, 0),
                new Location(null, 0, 0, 0)
        ));
        saveMaps();
    }

    public MapConfig getMap(String name) {
        return maps.get(name);
    }

    public boolean selectMap(String name) {
        if (!maps.containsKey(name)) {
            return false;
        }
        selectedMap = maps.get(name);
        return true;
    }

    public MapConfig getSelectedMap() {
        return selectedMap;
    }

    public void saveMaps() {

        maps.forEach((key, value) -> {
            configManager.getConfig().set("maps." + key + ".name", value.getName());
            configManager.getConfig().set("maps." + key + ".tSpawn", value.getTSpawn());
            configManager.getConfig().set("maps." + key + ".ctSpawn", value.getCTSpawn());
        });

        configManager.saveConfig();
    }

    public void loadMaps() {


        MemorySection section = (MemorySection) configManager.getConfig().get("maps");
        section.getKeys(false).forEach(key -> {
            String name = section.getString(key + ".name");
            Location tSpawn = section.getLocation(key + ".tSpawn");
            Location ctSpawn = section.getLocation(key + ".ctSpawn");
            if (tSpawn == null || ctSpawn == null) {
                tSpawn = new Location(null, 0, 0, 0);
                ctSpawn = new Location(null, 0, 0, 0);
            }
            maps.put(name, new MapConfig(name, tSpawn, ctSpawn));
        });
    }

    public void removeMap(String name) {
        maps.remove(name);
        saveMaps();
    }

    public Map<String, MapConfig> getMaps() {
        return maps;
    }
}
