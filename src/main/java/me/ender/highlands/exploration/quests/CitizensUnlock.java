package me.ender.highlands.exploration.quests;

import net.citizensnpcs.api.CitizensAPI;

public class CitizensUnlock extends UnlockCondition{


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
        EntityTargetNPCEvent,
        NPCChatEvent,
        NPCClickEvent,
        NPCCollisionEvent,
        NPCCombustByBlockEvent,
        NPCCombustByEntityEvent,
        NPCCombustEvent,
        NPCDamageByBlockEvent,
        NPCDamageByEntityEvent,
        NPCDamageEvent,
        NPCDeathEvent,
        NPCDespawnEvent,
        NPCEvent,
        NPCLeftClickEvent,
        NPCPushEvent,
        NPCRemoveEvent,
        NPCRightClickEvent,
        NPCSelectEvent,
        NPCSpawnEvent,
        PlayerCreateNPCEvent,
    }
    public Events event;

}
