package me.ender.highlands.exploration.book;

import me.ender.highlands.exploration.QuestPlayer;
import me.ender.highlands.exploration.conditions.IUnlockCondition;
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

    /**
     * Do not call this method directly, please see {@link IUnlockCondition#registerReward(QuestPlayer)}
     * @param player
     * @return Whether or not giving the player the reward was successful
     */
    boolean registerReward(QuestPlayer player);



}
