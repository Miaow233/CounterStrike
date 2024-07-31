package io.github.miaow233.counterstrike.managers;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.models.GamePlayer;
import io.github.miaow233.counterstrike.models.Team;
import org.bukkit.entity.Player;

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
        players.remove(player);
    }

    public GamePlayer getGamePlayer(Player player) {
        return players.get(player);
    }

    private void assignTeam(GamePlayer gamePlayer) {
        int terroristCount = (int) players.values().stream().filter(p -> p.getTeam() == Team.TERRORISTS).count();
        int counterTerroristCount = (int) players.values().stream().filter(p -> p.getTeam() == Team.COUNTER_TERRORISTS).count();

        if (terroristCount <= counterTerroristCount) {
            gamePlayer.setTeam(Team.TERRORISTS);
            gamePlayer.getPlayer().sendMessage("You have been assigned to the Terrorists team.");
        } else {
            gamePlayer.setTeam(Team.COUNTER_TERRORISTS);
            gamePlayer.getPlayer().sendMessage("You have been assigned to the Counter-Terrorists team.");
        }
    }

    public Map<Player, GamePlayer> getPlayers() {
        return players;
    }

    public void resetPlayerStats() {
        players.forEach((player, gamePlayer) -> {
            gamePlayer.setKills(0);
            gamePlayer.setDeaths(0);

            EconomyManager.getInstance().updatePlayerCoins(player, 0);
        });
    }
}
