package me.ender.highlands.exploration.data;

import me.ender.highlands.exploration.conditions.IUnlockCondition;

import java.util.UUID;

public interface IQuestData {
    //how do i want to store the components
    //attached to player
    //in order to unlock the component , i have to have a way to detect the unlocks
    //quest data should probably have to monitor events

    /**
     * Checks to see if the particular condition has been met
     * @param condition
     * @return
     */
    boolean getUnlocked(IUnlockCondition condition);
    boolean getUnlocked(UUID uuid);

    void setUnlocked(IUnlockCondition condition);
    void setUnlocked(UUID uuid);

    void setLocked(IUnlockCondition condition);
    void setLocked(UUID uuid);

    void saveData();
}
