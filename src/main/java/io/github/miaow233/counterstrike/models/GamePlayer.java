package io.github.miaow233.counterstrike.models;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.managers.EconomyManager;
import org.bukkit.entity.Player;

public class GamePlayer {

    private final Player player;
    private int coins;
    private int kills;
    private int deaths;
    private Team team;

    public GamePlayer(Player player) {
        this.player = player;
        this.coins = 0;
        this.team = null; // 玩家初始时没有阵营
    }

    public Player getPlayer() {
        return player;
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int amount) {
        this.coins += amount;
        EconomyManager.getInstance().updatePlayerCoins(player, this.coins); // 确保同步更新
    }

    public void removeCoins(int amount) {
        this.coins -= amount;
        EconomyManager.getInstance().updatePlayerCoins(player, this.coins); // 确保同步更新
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        CounterStrike.instance.getLogger().info("玩家 " + player.getName() + " 已被设置为 " + team.name() + " 阵营");
        CounterStrike.instance.getTeamManager().addPlayerToTeam(player.getName(), team.name());
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void addKills(int kills) {
        this.kills += kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void addDeaths(int deaths) {
        this.deaths += deaths;
    }
}
