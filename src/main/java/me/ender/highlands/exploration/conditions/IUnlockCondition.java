package me.ender.highlands.exploration.conditions;

import java.util.UUID;

public interface IUnlockCondition {
    enum Identifiers {
        STRING,
        GUID,
        INT,
        REGION,

    }
    void setUUID(UUID uuid);
    UUID getUUID();

    void setType(Identifiers type);
    Identifiers getType();

    String getIdentifier();
    void setIdentifier(String identifier);

    Enum getEvent();
    void setEvent(Enum event);

    String getImplementation();


}
