package io.github.miaow233.counterstrike;

import org.bukkit.Location;
import org.bukkit.configuration.MemorySection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestConfig {

    Map<String, MapConfig> maps = new HashMap<>();
    MapConfig selectedMap;

    ConfigManager configManager;

    public TestConfig() throws IOException {
        configManager = new ConfigManager("config.yml");
    }

    public static void main(String[] args) throws IOException {
        TestConfig instance = new TestConfig();
        instance.loadMaps();
        instance.createMap("de_dust2");
        instance.createMap("dust_2");
        instance.saveMaps();
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

    public void selectMap(String name) {
        selectedMap = maps.get(name);
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

    public static class MapConfig {
        private final String name;
        private final Location tSpawn;
        private final Location ctSpawn;

        public MapConfig(String name, Location tSpawn, Location ctSpawn) {
            this.name = name;
            this.tSpawn = tSpawn;
            this.ctSpawn = ctSpawn;
        }

        public String getName() {
            return name;
        }

        public Location getTSpawn() {
            return tSpawn;
        }

        public Location getCTSpawn() {
            return ctSpawn;
        }
    }
}
