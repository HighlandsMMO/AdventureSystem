package me.ender.highlands.exploration.data;

import me.ender.highlands.exploration.conditions.IUnlockCondition;

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

    void setUnlocked(IUnlockCondition condition);

    void setLocked(IUnlockCondition condition);
}
