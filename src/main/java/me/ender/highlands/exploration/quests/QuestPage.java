package me.ender.highlands.exploration.quests;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestPage {
    private List<QuestComponent> lines;
    public IUnlockCondition condition;

    public QuestPage() {
        lines = new ArrayList<>();
    }
    public QuestPage(QuestComponent[] components) {
        lines = Arrays.stream(components).toList();
    }

    public QuestComponent[] getPage() {
        var ary = new QuestComponent[lines.size()];
        lines.toArray(ary);
        return ary;
    }
    public BaseComponent[] getRaw() {
        var ary = new BaseComponent[lines.size()];
        lines.stream().map(l->l.getComponent()).toList().toArray(ary);

        return ary;
    }

    public void addComponent(QuestComponent component) {
        lines.add(component);
    }
}
