package me.ender.highlands.exploration.book;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.manager.ItemManager;
import org.bukkit.inventory.ItemStack;

public class MMOQuestReward extends QuestReward{
    private String type;
    private String id;

    public MMOQuestReward(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public ItemStack getReward() {
        if(reward == null) {
            reward = MMOItems.plugin.getItem(type, id);
        }
        return reward;
    }


    public void setReward(String type, String id) {
        this.type = type;
        this.id = id;
        reward = null;
    }


}
