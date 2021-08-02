package me.ender.highlands.exploration.book;

import me.ender.highlands.exploration.conditions.IUnlockCondition;
import me.ender.highlands.exploration.conditions.IUnlockable;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

import java.util.List;

public class QuestComponent {
   private BaseComponent component;
   private IUnlockCondition condition;
   private QuestReward reward;

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

    public QuestReward getReward() {
       return reward;
    }
    public void setReward(QuestReward reward) {
       this.reward = reward;
    }

}

