package io.github.miaow233.counterstrike.managers;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.models.GamePlayer;
import io.github.miaow233.counterstrike.models.Team;
import io.github.miaow233.counterstrike.utils.CommandUtils;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager instance;
    private final CounterStrike plugin;
    private final Map<Player, GamePlayer> players;

    private PlayerManager(CounterStrike plugin) {
        this.plugin = plugin;
        this.players = new HashMap<>();
    }

    public static void initialize(CounterStrike plugin) {
        if (instance == null) {
            instance = new PlayerManager(plugin);
        }
    }

    public static PlayerManager getInstance() {
        return instance;
    }

    public void addPlayer(Player player) {
        GamePlayer gamePlayer = new GamePlayer(player);
        players.put(player, gamePlayer);
        assignTeam(gamePlayer);
    }

    public void addPlayer(Player player, Team team) {
        GamePlayer gamePlayer = new GamePlayer(player);

        // TODO: 检查阵营是否已满员
        gamePlayer.setTeam(team);
        players.put(player, gamePlayer);
    }

    public void removePlayer(Player player) {

        // 传送至主世界
        player.teleport(plugin.getServer().getWorld("world").getSpawnLocation());

        // 清除游戏物品
        player.getInventory().clear();

        // 清除药水效果
        player.removePotionEffect(PotionEffectType.SPEED);
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        player.setGameMode(GameMode.ADVENTURE);

        CommandUtils.runCommandAsConsole("lrhud countdown set %s 0 false".formatted(player.getName()));
        CommandUtils.runCommandAsConsole("team leave %s".formatted(player.getName()));
        EconomyManager.getInstance().setCoins(player, 0);

        GamePlayer gamePlayer = players.get(player);
        if (gamePlayer == null) return;
        gamePlayer.reset();
        players.remove(player);

        if (players.isEmpty()) {
            GameManager.getInstance().endGame();
        }
    }

    public GamePlayer getGamePlayer(Player player) {
        return players.get(player);
    }

    private void assignTeam(GamePlayer gamePlayer) {
        int terroristCount = (int) players.values().stream().filter(p -> p.getTeam() == Team.TERRORISTS).count();
        int counterTerroristCount = (int) players.values().stream().filter(p -> p.getTeam() == Team.COUNTER_TERRORISTS).count();

        if (terroristCount <= counterTerroristCount) {
            gamePlayer.setTeam(Team.TERRORISTS);
            gamePlayer.getPlayer().sendMessage("你已被分配为T阵营");
        } else {
            gamePlayer.setTeam(Team.COUNTER_TERRORISTS);
            gamePlayer.getPlayer().sendMessage("你已被分配为CT阵营");
        }
    }


    public Map<Player, GamePlayer> getPlayers() {
        return players;
    }

    public Map<Team, Integer> getTeamCount() {
        Map<Team, Integer> teamCount = new HashMap<>();
        teamCount.put(Team.TERRORISTS, (int) players.values().stream().filter(p -> p.getTeam() == Team.TERRORISTS).count());
        teamCount.put(Team.COUNTER_TERRORISTS, (int) players.values().stream().filter(p -> p.getTeam() == Team.COUNTER_TERRORISTS).count());
        return teamCount;
    }

    public int getAliveCount(Team team) {
        int getAliveCount = (int) players.values().stream()
                .filter(p -> p.getTeam() == team)
                .filter(p -> p.getPlayer().getGameMode() != GameMode.SPECTATOR)
                .count();
        plugin.getLogger().warning("队伍 %s 剩余 %d 人".formatted(team.name(), getAliveCount));
        return getAliveCount;
    }

    public Player getMvp() {
        return players
                .values()
                .stream()
                .max(Comparator.comparingInt(GamePlayer::getCurrentRoundKills))
                .map(GamePlayer::getPlayer)
                .orElse(null);
    }
}
