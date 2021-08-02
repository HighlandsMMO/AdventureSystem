package me.ender.highlands.exploration.book;

public abstract class QuestReward implements IQuestReward{
    protected RewardType type;
    protected String data;

    public QuestReward() {}

    public QuestReward(RewardType type, String data) {
        this.type = type;
        this.data = data;
    }

    @Override
    public RewardType getRewardType() {
        return type;
    }

    @Override
    public void setRewardType(RewardType type) {
        this.type = type;
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Transform the data into something that can be used by the implementor
     */
    abstract void parseData();
}
