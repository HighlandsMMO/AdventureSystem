package me.ender.highlands.exploration.conditions;

import me.ender.highlands.exploration.book.IQuestReward;

import java.util.UUID;

public interface IUnlockCondition {

    void setUUID(UUID uuid);
    UUID getUUID();


    String getIdentifier();
    void setIdentifier(String identifier);

    Enum getEvent();
    void setEvent(Enum event);

    String getImplementation();

    boolean canEqual(Object obj);

    //can have a reward
    //on Complete
    IQuestReward getQuestReward();
    void setQuestReward(IQuestReward reward);




}
