package me.ender.highlands.exploration.conditions;

import me.ender.highlands.exploration.book.IQuestReward;
import me.ender.highlands.exploration.book.QuestReward;

import java.util.UUID;

public abstract class UnlockCondition implements IUnlockCondition{
    protected String identifier;
    protected UUID uuid;
    protected IQuestReward reward;


    @Override
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }


    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public IQuestReward getQuestReward() {
        return reward;
    }

    @Override
    public void setQuestReward(IQuestReward reward) {
        this.reward = reward;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof IUnlockCondition condition) {
            if(getIdentifier().equals(condition.getIdentifier()) &&
                    getUUID().equals(condition.getUUID()) &&
                    getEvent().equals(condition.getEvent()))
                return true;
        }
        return super.equals(obj);
    }

    @Override
    public boolean canEqual(Object obj) {
        if(obj instanceof IUnlockCondition condition) {
            if(getIdentifier().equals(condition.getIdentifier()) &&
                    getImplementation().equals(condition.getImplementation()) &&
                    getEvent().equals(condition.getEvent()))
                return true;
        }
        return false;
    }
}
