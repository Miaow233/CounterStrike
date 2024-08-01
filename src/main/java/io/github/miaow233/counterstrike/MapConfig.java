package io.github.miaow233.counterstrike;

import org.bukkit.Location;


public class MapConfig {
    private final String name;
    private Location tSpawn;
    private Location ctSpawn;

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

    public void setTSpawn(Location location) {
        this.tSpawn = location;
    }

    public void setCTSpawn(Location location) {
        this.ctSpawn = location;
    }
}
