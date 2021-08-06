package me.ender.highlands.exploration.book;

import me.ender.highlands.exploration.conditions.IUnlockCondition;
import me.ender.highlands.exploration.QuestPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.apache.commons.lang.Validate;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestPage {
    private List<QuestComponent> components;
    private IUnlockCondition condition;
    private List<IQuestReward> rewards;

    public QuestPage() {
        components = new ArrayList<>();
    }

    public QuestPage(QuestComponent[] components) {
        this.components = Arrays.stream(components).toList();
    }

    public List<QuestComponent> getRawPage() {
        return components;
    }

    public BaseComponent[] getRaw() {
        var ary = new BaseComponent[components.size()];
        components.stream().map(l -> l.getComponent()).toList().toArray(ary);
        return ary;
    }

    public void addComponent(QuestComponent component) {
        components.add(component);
    }

    public QuestComponent getLine(int index) {
        return components.get(index);
    }

    /**
     * Checks each component to see if its unlocked
     *
     * @param player
     * @return
     */
    public BaseComponent[] getPage(QuestPlayer player) {
        //if the page is unlocked is checked a level above
        var list = new ArrayList<BaseComponent>();
        for (var c : components) {
            if (c.getReward() != null) {
                if (!player.getUnlocked(c.getCondition())) //if condition is not unlocked
                    continue;
                var comp = c.getComponent().duplicate(); //dont edit the original
                if (player.rewardClaimed(c.getCondition())) { //condition is unlocked & reward is in unlclaimed rewards
                    comp.setColor(ChatColor.RED);
                    comp.setStrikethrough(true);
                } else {
                    comp.setColor(ChatColor.GREEN);
                }

                list.add(comp);
            } else if (player.getUnlocked(c.getCondition()))
                list.add(c.getComponent());
        }
        return list.toArray(new BaseComponent[0]);
    }

    public List<IQuestReward> getRewards() {
        if (rewards == null) {
            rewards = new ArrayList<>();
            for (var c : components) {
                if (c.getReward() != null)
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
