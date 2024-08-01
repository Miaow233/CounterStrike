package io.github.miaow233.counterstrike.managers;

import io.github.miaow233.counterstrike.CounterStrike;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private FileConfiguration fileConfig;
    private final File file;

    public ConfigManager(String fileName) {
        this.file = new File(CounterStrike.instance.getDataFolder(), fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }

    public ConfigManager(File file) {
        this.file = file;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }


    public FileConfiguration getConfig() {
        return fileConfig;
    }

    public void saveConfig() {
        try {
            fileConfig.save(file);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + String.format("Could not save %s!", this.file.getName()));
        }
    }

    public void reloadConfig() {
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }
}
