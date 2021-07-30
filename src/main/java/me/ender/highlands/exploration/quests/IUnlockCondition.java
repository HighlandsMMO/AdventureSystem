package me.ender.highlands.exploration.quests;

public interface IUnlockCondition {
    enum Identifiers {
        STRING,
        GUID,
        INT,
        REGION,

    }

    void setType(Identifiers type);
    Identifiers getType();

    String getIdentifier();
    void setIdentifier(String identifier);

    Enum getEvent();
    void setEvent(Enum event);

    String getImplementation();


}
