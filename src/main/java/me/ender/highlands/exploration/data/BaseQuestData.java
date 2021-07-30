package me.ender.highlands.exploration.data;

import me.ender.highlands.exploration.conditions.IUnlockCondition;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BaseQuestData implements IQuestData{

    private Set<UUID> conditionStore;

    public BaseQuestData() {
        conditionStore = new HashSet<>();
    }

    @Override
    public boolean getUnlocked(IUnlockCondition condition) {
        if(condition == null)
            return true; //if there is no condition then its always unlocked
        return conditionStore.contains(condition.getUUID());
    }

    @Override
    public void setUnlocked(IUnlockCondition condition) {
        if(condition != null)
            conditionStore.add(condition.getUUID());
    }

    @Override
    public void setLocked(IUnlockCondition condition) {
        if(condition != null)
            conditionStore.remove(condition.getUUID());
    }
}
