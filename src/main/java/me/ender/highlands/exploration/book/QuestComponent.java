package me.ender.highlands.exploration.book;

import me.ender.highlands.exploration.conditions.IUnlockCondition;
import me.ender.highlands.exploration.conditions.IUnlockable;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

public class QuestComponent implements IUnlockable {
   private BaseComponent component;
   private IUnlockCondition condition;

   public QuestComponent() {}
   public QuestComponent(BaseComponent component) {
       this.component = component;
    }

    public QuestComponent(BaseComponent component, IUnlockCondition condition) {
       this.component = component;
       this.condition = condition;
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

    @Override
    public boolean isUnlocked(Player player) {
        return false;


    }
}

