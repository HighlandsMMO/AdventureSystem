package me.ender.highlands.exploration;

import me.ender.highlands.Core;
import me.ender.highlands.exploration.data.IQuestData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ExplorationHandler {
    private final Core plugin;
    private Map<UUID, QuestPlayer> questData;

    public ExplorationHandler(Core plugin) {
        this.plugin = plugin;
        questData = new HashMap<>();
    }

    public QuestPlayer getPlayer(Player player) {
        var data = questData.get(player.getUniqueId());
        if(data == null){
            data = new QuestPlayer(player);
            questData.put(player.getUniqueId(), data);
        }
        return data;
    }
    public IQuestData getQuestData(UUID uuid) {
        var player = questData.get(uuid);
        if(player == null) {
            plugin.getLogger().info("Error while getting player quest data, player does not exist");
            return null;
        }
        var data = player.getQuestData();
        if(data == null) {
                plugin.getLogger().info("Error while getting player quest data, data does not exist");
                return null;
        }
        return data;
    }
}
