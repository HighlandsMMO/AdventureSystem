package me.ender.highlands.exploration.book;

import me.ender.highlands.exploration.conditions.IUnlockCondition;
import me.ender.highlands.exploration.QuestPlayer;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestPage {
    private List<QuestComponent> components;
    public IUnlockCondition condition;

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
        var data = player.getQuestData();
        //if the page is unlocked is checked a level above
        var list = new ArrayList<BaseComponent>();
        for(var c : components) {
            if(data.getUnlocked(c.getCondition()))
                list.add(c.getComponent());
        }
        return list.toArray(new BaseComponent[0]);
    }

    public IUnlockCondition getCondition() {
        return condition;
    }
}
