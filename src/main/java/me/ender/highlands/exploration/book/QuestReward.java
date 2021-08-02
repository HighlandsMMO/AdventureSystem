package me.ender.highlands.exploration.book;

import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.inventory.ItemStack;

public abstract class QuestReward {
    ItemStack reward;
    String type;
    String id;

    public void setReward(ItemStack reward) {
        this.reward = reward;
    }
    public abstract ItemStack getReward();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
