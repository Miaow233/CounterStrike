package io.github.miaow233.counterstrike.managers;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.models.Team;
import org.bukkit.Location;

public class GameManager {

    private static GameManager instance;
    private final CounterStrike plugin;

    private GameManager(CounterStrike plugin) {
        this.plugin = plugin;
    }

    public static void initialize(CounterStrike plugin) {
        if (instance == null) {
            instance = new GameManager(plugin);
        }
    }

    public static GameManager getInstance() {
        return instance;
    }

    public void startGame() {
        RoundManager.initialize(plugin, 180, 10); // Example: each round is 180 seconds, 10 rounds total
        // Prepare the game
        PlayerManager.getInstance().getPlayers().forEach((player, gamePlayer) -> {
            Location spawnLocation = getSpawnLocationForTeam(gamePlayer.getTeam());
            player.teleport(spawnLocation);
        });
        RoundManager.getInstance().startRounds();
    }

    public void endGame() {
        RoundManager.getInstance().stopRounds();
        // Additional logic to end the game
    }

    private Location getSpawnLocationForTeam(Team team) {
        if (team == Team.TERRORISTS) {
            return (Location) plugin.getConfig().get("teams.TERRORISTS.spawn");
        } else {
            return (Location) plugin.getConfig().get("teams.COUNTER_TERRORISTS.spawn");
        }
    }
}
