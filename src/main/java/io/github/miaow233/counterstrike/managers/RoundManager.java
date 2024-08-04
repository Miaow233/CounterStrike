package io.github.miaow233.counterstrike.managers;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.GameState;
import io.github.miaow233.counterstrike.models.GamePlayer;
import io.github.miaow233.counterstrike.models.Team;
import io.github.miaow233.counterstrike.utils.PacketUtils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class RoundManager {

    private static RoundManager instance;
    private final CounterStrike plugin;
    private final int roundTime;
    private final int rounds;
    private int currentRound;
    private BukkitRunnable roundTask;

    private int timerId;

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

        //
        if (roundTask != null && !roundTask.isCancelled()) roundTask.cancel();

        if (currentRound > rounds) {
            stopRounds();
            return;
        }

        for (Player player : PlayerManager.getInstance().getPlayers().keySet()) {
            respawnPlayer(player);
            EconomyManager.getInstance().addCoins(player, 500);
        }

        PacketUtils.sendTitleAndSubtitleToInGame("第 %s %s回合".formatted(ChatColor.RED.toString() + currentRound, ChatColor.RESET), "", 1, 3, 1);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            PacketUtils.sendTitleAndSubtitleToInGame("你有" + ChatColor.YELLOW + "20" + ChatColor.RESET + "秒时间来购买物资", "", 1, 3, 0);
        }, 5 * 20L);


        plugin.setGameState(GameState.SHOP);

        plugin.setGameState(GameState.IN_GAME);

        startTimer();

        roundTask = new BukkitRunnable() {
            @Override
            public void run() {
                endRound(false);
            }
        };

        roundTask.runTaskLater(plugin, roundTime * 20L);
    }

    private void startTimer() {
        AtomicInteger remainTime = new AtomicInteger(roundTime);
        if (Bukkit.getScheduler().isCurrentlyRunning(timerId)) {
            Bukkit.getScheduler().cancelTask(timerId);
        }
        // 计时器每秒减一
        timerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            boolean blink = remainTime.get() <= 30;
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("lrhud countdown set @a %d %s", remainTime.getAndDecrement(), blink));
        }, 0, 20L);
    }

    public void endRound(boolean clearLastTimer) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lrhud countdown set @a 0 true");
        Bukkit.getScheduler().cancelTask(timerId);

        Player mvpPlayer = PlayerManager.getInstance().getMvp();
        if (mvpPlayer != null) {
            PacketUtils.sendTitleAndSubtitleToInGame("MVP", mvpPlayer.getName(), 0, 3, 0);
        }

        Bukkit.broadcastMessage(String.format("第 %d 回合结束，5秒后开始下一回合", currentRound));

        currentRound++;

        if (clearLastTimer) {
            roundTask.cancel();
        }

        // 延迟5秒开始新回合
        Bukkit.getScheduler().runTaskLater(plugin, this::startRound, 5 * 20L);
    }

    // 复位玩家
    public void respawnPlayer(Player player) {
        player.setFoodLevel(8);
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        plugin.getLogger().info("设置玩家 " + player.getName() + " 的生命值为 " + maxHealth);
        player.setHealth(maxHealth);
        // 速度效果
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        player.setGameMode(GameMode.ADVENTURE);
        GamePlayer gamePlayer = PlayerManager.getInstance().getGamePlayer(player);
        Location location = gamePlayer.getTeam() == Team.TERRORISTS ? CounterStrike.instance.getMapManager().getSelectedMap().getTSpawn() : CounterStrike.instance.getMapManager().getSelectedMap().getCTSpawn();
        player.teleport(location);
        player.setInvulnerable(true);
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.setInvulnerable(false), 5 * 20L); // 5秒无敌时间

        ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        player.getInventory().setBoots(boots);

        ItemStack leggings = new ItemStack(Material.NETHERITE_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        player.getInventory().setLeggings(leggings);

        ItemStack chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        player.getInventory().setChestplate(chestplate);
    }

    public void stopRounds() {
        PacketUtils.sendTitleAndSubtitleToInGame("游戏结束", "", 1, 3, 1);
        // 清除玩家效果
        for (Player player : PlayerManager.getInstance().getPlayers().keySet()) {
            player.getInventory().clear();
            player.setGameMode(GameMode.ADVENTURE);
            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            player.removePotionEffect(PotionEffectType.SPEED);
        }

        Bukkit.getScheduler().cancelTask(timerId);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lrhud countdown set @a 0 false");

        if (instance != null && roundTask != null && !roundTask.isCancelled()) {
            roundTask.cancel();
            roundTask = null;
        }
    }
}
