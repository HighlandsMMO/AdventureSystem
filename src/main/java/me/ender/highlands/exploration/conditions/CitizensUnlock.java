package me.ender.highlands.exploration.conditions;

public class CitizensUnlock extends UnlockCondition{

    public CitizensUnlock() {}
    public CitizensUnlock(Events event, String id) {
        this.event = event;
        this.identifier = id;
    }
    @Override
    public String getImplementation() {
        return "npc";
    }


    @Override
    public Enum getEvent() {
        return event;
    }

    @Override
    public void setEvent(Enum event) {
        this.event = (Events) event;
    }

    public enum Events {
        NPCChatEvent,
        NPCClickEvent,
        NPCCollisionEvent,
        NPCCombustByEntityEvent,
        NPCDamageByEntityEvent,
        NPCDeathEvent,
        NPCLeftClickEvent,
        NPCPushEvent,
        NPCRemoveEvent,
        NPCRightClickEvent,
        NPCSelectEvent,
    }
    private Events event;

}
