package io.github.miaow233.counterstrike.managers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamManager {
    private final JavaPlugin plugin;
    private final Scoreboard scoreboard;

    public TeamManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    public void createTeam(String teamName, String displayName, String color) {
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.setDisplayName(displayName);
            team.setColor(org.bukkit.ChatColor.valueOf(color.toUpperCase()));
        }
    }

    public void addPlayerToTeam(String playerName, String teamName) {
        Team team = scoreboard.getTeam(teamName);
        if (team != null) {
            team.addEntry(playerName);
        }
    }

    public void removePlayerFromTeam(String playerName, String teamName) {
        Team team = scoreboard.getTeam(teamName);
        if (team != null) {
            team.removeEntry(playerName);
        }
    }

    public void deleteTeam(String teamName) {
        Team team = scoreboard.getTeam(teamName);
        if (team != null) {
            team.unregister();
        }
    }

    public boolean hasTeam(String teamName) {
        return scoreboard.getTeam(teamName) != null;
    }
}