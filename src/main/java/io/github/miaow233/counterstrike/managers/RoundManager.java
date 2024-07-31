package io.github.miaow233.counterstrike.managers;

import io.github.miaow233.counterstrike.CounterStrike;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

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

        AtomicInteger time = new AtomicInteger(120);
        // 设置计时器
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lrhud countdown set @a 120 false");

        // 计时器每秒减一
        int timerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            boolean blink = time.get() <= 30;

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lrhud countdown set @a " + time.getAndDecrement() + " " + blink);
        }, 0, 20);

        // 任务结束后取消计时器
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lrhud countdown set @a 0 true");
            Bukkit.getScheduler().cancelTask(timerId);
        }, 120 * 20L);

        //plugin.setGameState(GameState.IN_GAME);
        // 开始新回合的逻辑
        // 重置玩家状态等

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

        //plugin.setGameState(GameState.ROUND_END);
        // 处理回合结束的逻辑
        // 重生所有旁观者模式的玩家
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                respawnPlayer(player);
            }
        }
        currentRound++;
        // 延迟开始新回合
        Bukkit.getScheduler().runTaskLater(plugin, this::startRound, 100L); // 延迟5秒开始新回合
    }

    private void respawnPlayer(Player player) {
        player.setGameMode(GameMode.ADVENTURE);

        player.teleport(player.getBedSpawnLocation()); // 重生点，可以根据实际情况调整
        player.setInvulnerable(true);
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.setInvulnerable(false), 100L); // 5秒无敌时间
    }

    public void endGame() {
        Bukkit.broadcastMessage("Game has ended!");
        // Logic to finalize the game, determine winner, and cleanup
    }

    public void stopRounds() {
        if (instance != null && roundTask != null && !roundTask.isCancelled()) {
            roundTask.cancel();
        }
    }
}
