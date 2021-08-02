package me.ender.highlands.exploration.conditions;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

public class LocationUnlock extends UnlockCondition {
    public enum Events {
        BlockClick,
        EnterRegion,
    }
    private Events event;
    public LocationUnlock() {}
    public LocationUnlock(Events event, String identifier) {
        this.event = event;
        this.identifier = identifier;
    }
    public LocationUnlock(Events event, Block block) {
        this.event = event;
        var blockString = block.getWorld().getName() + "," + block.getBlockKey();
        this.identifier = blockString;
    }

    @Override
    public Enum getEvent() {
        return event;
    }

    @Override
    public void setEvent(Enum event) {
        if(event instanceof Events e)
            this.event = e;
    }

    @Override
    public String getImplementation() {
        return "location";
    }

}
