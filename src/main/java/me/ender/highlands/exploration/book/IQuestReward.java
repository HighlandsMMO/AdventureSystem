package me.ender.highlands.exploration.book;

import me.ender.highlands.exploration.QuestPlayer;
import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IQuestReward {
    enum RewardType {
        Item,
        Command,
    }

    RewardType getRewardType();
    void setRewardType(RewardType type);

    String getData();
    void setData(String data);

    boolean registerReward(QuestPlayer player);



}
