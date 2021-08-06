package me.ender.highlands.exploration.conditions;

import me.ender.highlands.Core;
import me.ender.highlands.exploration.ExplorationHandler;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.*;

public class LocationUnlockHandler implements Listener {
    private final Core plugin;
    private final ExplorationHandler explorationHandler;


    private Map<Block, List<IUnlockCondition>> blockClickEventMap;

    public LocationUnlockHandler(ExplorationHandler handler) {
        this.explorationHandler = handler;
        this.plugin = handler.getPlugin();
        blockClickEventMap = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        //how to have events
    }
    private Block conditionStringToBlock(String identifier) {
        var info = identifier.split(",");
        if(info.length != 2)
            return null;
        var world = info[0];
        //Integer.parseInt(info[1]), Integer.parseInt(info[2]), Integer.parseInt(info[3])
        return Bukkit.getServer().getWorld(world).getBlockAtKey(Long.valueOf(info[1]));
    }

    public boolean registerEvent(IUnlockCondition event) {
        switch((LocationUnlock.Events)event.getEvent()) {
            case BlockClick -> {
                var block = conditionStringToBlock(event.getIdentifier());
                if(block == null)
                    return false;
                if(blockClickEventMap.containsKey(block)) {
                    var list = blockClickEventMap.get(block);
                    if(list.contains(event)) {
                        return false;
                    }
                    list.add(event);
                } else {
                    var list = new ArrayList<IUnlockCondition>();
                    list.add(event);
                    blockClickEventMap.put(block, list);
                }
            }
        }
        return true;
    }
    @EventHandler
    private void onBlockClick(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getHand() != EquipmentSlot.HAND)
            return;
        var block = e.getClickedBlock();
        if(block == null) {
            return;
        }
        System.out.println(block);
        var list = blockClickEventMap.get(block);
        if(list == null)
            return;
        var data = explorationHandler.getPlayer(e.getPlayer()); //data should never be null;
        for(var condition : list) {
            data.setUnlocked(condition);
        }
    }
}
