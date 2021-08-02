package me.ender.highlands.exploration.conditions;

import me.ender.highlands.Core;
import me.ender.highlands.exploration.ExplorationHandler;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class CitizensUnlockHandler implements Listener {
    private final Core plugin;
    private final ExplorationHandler explorationHandler;


    private Map<Integer, List<UUID>> npcRightClickMap;

    public CitizensUnlockHandler(ExplorationHandler handler) {
        this.explorationHandler = handler;
        this.plugin = handler.getPlugin();
        npcRightClickMap = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        //how to have events
    }
    private NPC idToNPC(String identifier) {
        var id = Integer.parseInt(identifier);
        return CitizensAPI.getNamedNPCRegistry("citizens").getById(id);
    }

    public boolean registerEvent(IUnlockCondition event) {
        switch((CitizensUnlock.Events)event.getEvent()) {
            case NPCRightClickEvent -> {
                var id = Integer.parseInt(event.getIdentifier());
                if(npcRightClickMap.containsKey(id)) {
                    var list = npcRightClickMap.get(id);
                    if(list.contains(event.getUUID())) { //return if false
                        return false;
                    }
                    list.add(event.getUUID());
                } else {
                    var list = new ArrayList<UUID>();
                    list.add(event.getUUID());
                    npcRightClickMap.put(id, list);
                    System.out.println(id);
                }
            }
        }
        return true;
    }
    @EventHandler
    private void onBlockClick(NPCRightClickEvent e) {
        var npc =e.getNPC();
        System.out.println(e.getNPC());
        if(npcRightClickMap.containsKey(e.getNPC().getId())) {
            var list = npcRightClickMap.get(e.getNPC().getId());
            var player = e.getClicker().getUniqueId();
            for(var entry : list) {
                explorationHandler.getQuestData(player).setUnlocked(entry);
            }
        }
    }
}
