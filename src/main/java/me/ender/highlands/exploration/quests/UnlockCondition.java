package me.ender.highlands.exploration.quests;

public abstract class UnlockCondition implements IUnlockCondition{
    protected String identifier;
    protected Identifiers type;

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
}
