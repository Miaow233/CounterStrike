package io.github.miaow233.counterstrike.managers;

import io.github.miaow233.counterstrike.CounterStrike;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class RoundManager {

    private static RoundManager instance;
    private final CounterStrike plugin;
    private final int roundTime; // Time for each round in seconds
    private final int rounds; // Total number of rounds
    private int currentRound;
    private BukkitRunnable roundTask;

    private RoundManager(CounterStrike plugin, int roundTime, int rounds) {
        this.plugin = plugin;
        this.roundTime = roundTime;
        this.rounds = rounds;
        this.currentRound = 0;
    }

    public static void initialize(CounterStrike plugin, int roundTime, int rounds) {
        if (instance == null) {
            instance = new RoundManager(plugin, roundTime, rounds);
        }
    }

    public static RoundManager getInstance() {
        return instance;
    }

    public void startRounds() {
        currentRound = 1;
        startRound();
    }

    private void startRound() {
        if (currentRound > rounds) {
            endGame();
            return;
        }

        Bukkit.broadcastMessage("Round " + currentRound + " has started!");

        roundTask = new BukkitRunnable() {
            @Override
            public void run() {
                endRound();
            }
        };

        roundTask.runTaskLater(plugin, roundTime * 20L); // Convert seconds to ticks (20 ticks = 1 second)
    }

    private void endRound() {
        Bukkit.broadcastMessage("Round " + currentRound + " has ended!");
        // Logic to check winning conditions and update game state

        currentRound++;
        startRound();
    }

    public void endGame() {
        Bukkit.broadcastMessage("Game has ended!");
        // Logic to finalize the game, determine winner, and cleanup
    }

    public void stopRounds() {
        if (roundTask != null && !roundTask.isCancelled()) {
            roundTask.cancel();
        }
    }
}
