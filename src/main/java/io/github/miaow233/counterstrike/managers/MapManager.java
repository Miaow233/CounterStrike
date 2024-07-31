package io.github.miaow233.counterstrike.managers;

import io.github.miaow233.counterstrike.MapConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapManager {
    private final Map<String, MapConfig> maps = new HashMap<>();
    private final File mapsFile;
    private final FileConfiguration mapsConfig;
    private MapConfig selectedMap;

    public MapManager(File dataFolder) {
        mapsFile = new File(dataFolder, "maps.yml");
        mapsConfig = YamlConfiguration.loadConfiguration(mapsFile);
        loadMaps();
    }

    public void createMap(String name) {
        maps.put(name, new MapConfig(name));
        saveMaps();
    }

    public MapConfig getMap(String name) {
        return maps.get(name);
    }

    public void selectMap(String name) {
        selectedMap = maps.get(name);
    }

    public MapConfig getSelectedMap() {
        return selectedMap;
    }

    public void saveMaps() {
        for (Map.Entry<String, MapConfig> entry : maps.entrySet()) {
            mapsConfig.set("maps." + entry.getKey(), entry.getValue());
        }
        try {
            mapsConfig.save(mapsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMaps() {
        ConfigurationSection section = mapsConfig.getConfigurationSection("maps");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                ConfigurationSection mapSection = section.getConfigurationSection(key);
                if (mapSection != null) {
                    Map<String, Object> mapData = mapSection.getValues(false);
                    MapConfig mapConfig = MapConfig.deserialize(mapData);
                    maps.put(key, mapConfig);
                }
            }
        }
    }

    public void removeMap(String name) {
        maps.remove(name);
        saveMaps();
    }

    public Map<String, MapConfig> getMaps() {
        for (String name : maps.keySet()) {
            if (!mapsConfig.contains("maps." + name)) {
                maps.remove(name);
            }
        }
        return maps;
    }
}
