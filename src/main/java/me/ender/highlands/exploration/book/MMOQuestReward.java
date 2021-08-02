package me.ender.highlands.exploration.book;

import me.ender.highlands.exploration.QuestPlayer;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class MMOQuestReward extends QuestReward {
    private Type itemType;
    private String id;
    private MMOItem item;

    public MMOQuestReward(String type, String id) {
        this(type + " " + id);
    }
    public MMOQuestReward(String data) {
        super(RewardType.Item, data);
        var split = data.split(" "); //split SWORD TEST
        //if(split.length != 2)
        itemType = Type.get(split[0]);
        id = split[1].toUpperCase(Locale.ROOT);
        //check if valid?
        //MMOItems.plugin.getTypes().get()
    }

    @Override
    void parseData() {
        item = MMOItems.plugin.getMMOItem(itemType,id);
    }


    @Override
    public boolean registerReward(QuestPlayer player) {
        return false;
    }
}
