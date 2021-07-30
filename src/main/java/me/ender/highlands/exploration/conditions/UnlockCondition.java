package me.ender.highlands.exploration.conditions;

import java.util.UUID;

public abstract class UnlockCondition implements IUnlockCondition{
    protected String identifier;
    protected Identifiers type;
    protected UUID uuid;

    @Override
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public void setType(Identifiers type) {
        this.type = type;
    }

    @Override
    public Identifiers getType() {
        return type;
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
    public boolean equals(Object obj) {
        if(obj instanceof IUnlockCondition condition) {
            if(getIdentifier().equals(condition.getIdentifier()) &&
                    getType().equals(condition.getType()) &&
                    getUUID().equals(condition.getUUID()) &&
                    getEvent().equals(condition.getEvent()))
                return true;
        }
        return super.equals(obj);
    }
}
