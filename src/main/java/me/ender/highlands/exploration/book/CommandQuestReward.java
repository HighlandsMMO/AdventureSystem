package me.ender.highlands.exploration.book;

import me.ender.highlands.exploration.QuestPlayer;
import org.bukkit.Bukkit;

public class CommandQuestReward extends QuestReward{

    public CommandQuestReward(String data) {
        super(RewardType.Command, data);
    }

    @Override
    public boolean registerReward(QuestPlayer player) {
        //return false;
        //todo protect
        return Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), data); //very very dangerous
    }

    @Override
    void parseData() {
        //do not need to do anything because the data is the command, but the command needs to be executed through op
    }
}
