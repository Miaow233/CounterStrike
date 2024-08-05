package io.github.miaow233.counterstrike.managers;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class DataManager {
    private static final Scoreboard scoreboard;

    static {
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    public static int getScore(String name, String objectiveName) {
        Objective objective = scoreboard.getObjective(objectiveName);
        if (objective == null) {
            return 0;
        }
        return objective.getScore(name).getScore();
    }

    public static void setScore(String name, String objectiveName, int score) {
        Objective objective = scoreboard.getObjective(objectiveName);
        if (objective == null) {
            scoreboard.registerNewObjective(objectiveName, Criteria.DUMMY, objectiveName);
            objective = scoreboard.getObjective(objectiveName);
        }
        objective.getScore(name).setScore(score);
    }

    public static void addScore(String name, String objectiveName, int score) {
        Objective objective = scoreboard.getObjective(objectiveName);
        if (objective == null) {
            scoreboard.registerNewObjective(objectiveName, Criteria.DUMMY, objectiveName);
            objective = scoreboard.getObjective(objectiveName);
        }
        objective.getScore(name).setScore(objective.getScore(name).getScore() + score);
    }

    public static void unregisterObjective(String objectiveName) {
        scoreboard.getObjective(objectiveName).unregister();
    }

    public static void registerObjective(String objectiveName, String displayName) {
        scoreboard.registerNewObjective(objectiveName, Criteria.DUMMY, displayName);
    }

}
