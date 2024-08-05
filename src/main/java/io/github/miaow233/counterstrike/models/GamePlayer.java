package io.github.miaow233.counterstrike.models;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.managers.DataManager;
import io.github.miaow233.counterstrike.managers.EconomyManager;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class GamePlayer {

    private final Player player;
    private int coins;
    private int kills;
    private int deaths;
    Scoreboard scoreboard;
    private Team team;
    private int mvp;
    private int currentRoundKills;

    public GamePlayer(Player player) {
        this.player = player;
        this.coins = 0;
        this.kills = 0;
        this.deaths = 0;
        this.mvp = 0;
        this.currentRoundKills = 0;
        this.team = null; // 玩家初始时没有阵营

        scoreboard = player.getScoreboard();
    }

    public Player getPlayer() {
        return player;
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int amount) {
        this.coins += amount;
        EconomyManager.getInstance().setCoins(player, this.coins); // 确保同步更新
    }

    public void removeCoins(int amount) {
        this.coins -= amount;
        EconomyManager.getInstance().setCoins(player, this.coins); // 确保同步更新
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
        DataManager.setScore(player.getName(), "csmc.kills", kills);

    }
    public void addKills(int kills) {
        this.kills += kills;
        DataManager.setScore(player.getName(), "csmc.kills", this.kills);
    }
    public int getDeaths() {
        return deaths;
    }
    public void setDeaths(int deaths) {
        this.deaths = deaths;
        DataManager.setScore(player.getName(), "csmc.deaths", deaths);
    }
    public void addDeaths(int deaths) {
        this.deaths += deaths;
        DataManager.setScore(player.getName(), "csmc.deaths", this.deaths);
    }

    public int getMvp() {
        return mvp;
    }

    public void setMvp(int mvp) {
        this.mvp = mvp;
        DataManager.setScore(player.getName(), "csmc.mvp", mvp);
    }

    public void addMvp(int mvp) {
        this.mvp += mvp;
        DataManager.setScore(player.getName(), "csmc.mvp", this.mvp);
    }

    public void reset() {
        this.coins = 0;
        this.kills = 0;
        this.deaths = 0;
        this.mvp = 0;

        DataManager.setScore(player.getName(), "csmc.kills", this.kills);
        DataManager.setScore(player.getName(), "csmc.deaths", this.deaths);
        DataManager.setScore(player.getName(), "csmc.mvp", this.mvp);
    }

    public int getCurrentRoundKills() {
        return currentRoundKills;
    }

    public void setCurrentRoundKills(int currentRoundKills) {
        this.currentRoundKills = currentRoundKills;
    }

    public void addCurrentRoundKills(int currentRoundKills) {
        this.currentRoundKills += currentRoundKills;
    }
}
