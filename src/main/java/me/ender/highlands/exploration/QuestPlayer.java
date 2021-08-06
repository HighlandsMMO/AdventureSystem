package me.ender.highlands.exploration;

import co.aikar.util.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import me.ender.highlands.exploration.conditions.IUnlockCondition;
import me.ender.highlands.exploration.data.IQuestData;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.scripting.EventRegistrar;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class QuestPlayer implements IQuestData{
    private final UUID uuid;
    private transient Player player;
    private Set<UUID> conditionStore;
    private Map<UUID, Boolean> unlockedRewards;

    public QuestPlayer(UUID uuid, Set<UUID> conditionStore, Map<UUID, Boolean> unlockedRewards) {
        this.uuid = uuid;
        this.conditionStore = conditionStore;
        this.unlockedRewards = unlockedRewards;
    }
    public QuestPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        //questData = new BaseQuestData(); //just return false for everything
        conditionStore = new HashSet<>();
        unlockedRewards = new HashMap<>();
    }


    public Player getPlayer() {
        if(player == null) {
            player = Bukkit.getPlayer(uuid);
        }
        return player;
    }

    @Override
    public boolean getUnlocked(IUnlockCondition condition) {
        if(condition == null)
            return true; //if there is no condition then its always unlocked
        return conditionStore.contains(condition.getUUID());
    }
    @Override
    public void setUnlocked(IUnlockCondition condition) {
        if(condition != null && !conditionStore.contains(condition.getUUID())) {
            conditionStore.add(condition.getUUID());
            var reward = condition.getQuestReward();
            if(reward != null)
                unlockedRewards.putIfAbsent(condition.getUUID(), false);
            saveData();
        }
    }

    @Override
    public void setLocked(IUnlockCondition condition) {
        if(condition != null) {
            conditionStore.remove(condition.getUUID());
            saveData();
        }
    }

    @Override
    public void claimReward(IUnlockCondition condition) {
        unlockedRewards.replace(condition.getUUID(), true);
        saveData();
    }

    @Override
    public boolean rewardClaimed(IUnlockCondition condition) {
        return unlockedRewards.get(condition.getUUID());
    }

    @Override
    public void saveData() {
        var path = "plugins/HighlandsExploration/players/"+getPlayer().getUniqueId().toString();
        var str = ExplorationHandler.GSON.toJson(this);
        try {
            Files.writeString(Path.of(path), str, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
