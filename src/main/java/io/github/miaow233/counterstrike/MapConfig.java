package io.github.miaow233.counterstrike;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("MapConfig")
public class MapConfig implements ConfigurationSerializable {
    private final String name;
    private Location tSpawn;
    private Location ctSpawn;

    public MapConfig(String name) {
        this.name = name;
    }

    public static MapConfig deserialize(Map<String, Object> map) {
        String name = (String) map.get("name");
        MapConfig mapConfig = new MapConfig(name);
        mapConfig.setTSpawn((Location) map.get("tSpawn"));
        mapConfig.setCTSpawn((Location) map.get("ctSpawn"));
        return mapConfig;
    }

    public String getName() {
        return name;
    }

    public Location getTSpawn() {
        return tSpawn;
    }

    public void setTSpawn(Location tSpawn) {
        this.tSpawn = tSpawn;
    }

    public Location getCTSpawn() {
        return ctSpawn;
    }

    public void setCTSpawn(Location ctSpawn) {
        this.ctSpawn = ctSpawn;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("tSpawn", tSpawn);
        map.put("ctSpawn", ctSpawn);
        return map;
    }
}
