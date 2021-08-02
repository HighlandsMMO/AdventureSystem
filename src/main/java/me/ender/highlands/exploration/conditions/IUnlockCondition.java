package me.ender.highlands.exploration.conditions;

import java.util.UUID;

public interface IUnlockCondition {

    void setUUID(UUID uuid);
    UUID getUUID();


    String getIdentifier();
    void setIdentifier(String identifier);

    Enum getEvent();
    void setEvent(Enum event);

    String getImplementation();


}
