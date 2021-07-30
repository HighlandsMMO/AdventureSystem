package me.ender.highlands.exploration;

import me.ender.highlands.exploration.data.BaseQuestData;
import me.ender.highlands.exploration.data.IQuestData;
import org.bukkit.entity.Player;

public class QuestPlayer {
    private final Player player;
    private IQuestData questData;

    public QuestPlayer(Player player) {
        this.player = player;
        questData = new BaseQuestData(); //just return false for everything
    }


    public Player getPlayer() {
        return player;
    }

    public IQuestData getQuestData() {
        return questData;
    }
}
