package me.ender.highlands.exploration.book;

import io.lumine.mythic.lib.api.item.NBTCompound;
import me.ender.highlands.exploration.conditions.IUnlockCondition;
import me.ender.highlands.exploration.QuestPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestPage {
    private List<QuestComponent> components;
    private IUnlockCondition condition;
    private List<QuestReward> rewards;

    public QuestPage() {
        components = new ArrayList<>();
    }
    public QuestPage(QuestComponent[] components) {
        this.components = Arrays.stream(components).toList();
    }

    public QuestComponent[] getRawPage() {
        var ary = new QuestComponent[components.size()];
        components.toArray(ary);
        return ary;
    }
    public BaseComponent[] getRaw() {
        var ary = new BaseComponent[components.size()];
        components.stream().map(l->l.getComponent()).toList().toArray(ary);
        return ary;
    }

    public void addComponent(QuestComponent component) {
        components.add(component);
    }

    /**
     * Checks each component to see if its unlocked
     * @param player
     * @return
     */
    public BaseComponent[] getPage(QuestPlayer player) {
        //if the page is unlocked is checked a level above
        var list = new ArrayList<BaseComponent>();
        for(var c : components) {
            if(c.getReward() != null) {
                var comp = c.getComponent();
                if(player.getUnlocked(c.getCondition().getUUID())){
                    comp.setClickEvent(null); //remove click event;
                    comp.setColor(ChatColor.GRAY); //set to gray to show locked
                }
                list.add(comp);
            }
            else if(player.getUnlocked(c.getCondition()))
                list.add(c.getComponent());
        }
        return list.toArray(new BaseComponent[0]);
    }

    public List<QuestReward> getRewards() {
        if(rewards == null) {
            rewards = new ArrayList<>();
            for(var c : components) {
                if(c.getReward() != null)
                    rewards.add(c.getReward());
            }
        }
        return rewards;
    }
    public IUnlockCondition getCondition() {
        return condition;
    }
    public void setCondition(IUnlockCondition condition) {
        this.condition = condition;
    }

}
