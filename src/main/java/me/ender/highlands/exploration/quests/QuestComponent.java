package me.ender.highlands.exploration.quests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class QuestComponent {
   private BaseComponent component;
   private IUnlockCondition condition;

   public QuestComponent(BaseComponent component) {
       this.component = component;
    }

    public BaseComponent getComponent() {
        return component;
    }

    public void setComponent(BaseComponent component) {
        this.component = component;
    }

    public IUnlockCondition getCondition() {
        return condition;
    }

    public void setCondition(IUnlockCondition condition) {
        this.condition = condition;
    }
}

