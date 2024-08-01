package io.github.miaow233.counterstrike;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private FileConfiguration fileConfig;
    private final File file;
    private String fileName;

    public ConfigManager(String fileName) {
        file = new File(fileName);

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
            Bukkit.getServer().getLogger().severe(ChatColor.RED + String.format("Could not save %s!", fileName));
        }
    }

    public void reloadConfig() {
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }
}
