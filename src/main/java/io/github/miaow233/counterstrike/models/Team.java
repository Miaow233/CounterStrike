package io.github.miaow233.counterstrike.models;

import io.github.miaow233.counterstrike.CounterStrike;
import io.github.miaow233.counterstrike.managers.TeamManager;

public enum Team {
    TERRORISTS, COUNTER_TERRORISTS;

    static {
        // 检查队伍是否存在，不存在则创建
        TeamManager teamManager = CounterStrike.instance.getTeamManager();

        teamManager.createTeam("TERRORISTS", "Terrorists", "RED");
        teamManager.createTeam("COUNTER_TERRORISTS", "Counter-Terrorists", "BLUE");
    }
}
