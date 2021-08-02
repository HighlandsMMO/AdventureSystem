package me.ender.highlands.exploration;

import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import me.ender.highlands.Core;
import me.ender.highlands.exploration.book.QuestBook;
import me.ender.highlands.exploration.book.QuestBookSerializer;
import me.ender.highlands.exploration.book.QuestReward;
import me.ender.highlands.exploration.conditions.*;
import me.ender.highlands.exploration.data.IQuestData;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ExplorationHandler implements Listener {
    private final Core plugin;
    private Map<UUID, QuestPlayer> questData;
    private Set<QuestBook> books;
    private final Path bookLocation;
    private final Path playerDataLocation;
    private final LocationUnlockHandler locationUnlockHandler;
    private CitizensUnlockHandler citizensUnlockHandler;
    private Map<UUID, QuestReward> availableRewards;


    public ExplorationHandler(Core plugin) {
        this.plugin = plugin;
        questData = new HashMap<>();
        books = new HashSet<>();
        bookLocation = plugin.getDataFolder().toPath().resolve("books");
        playerDataLocation = plugin.getDataFolder().toPath().resolve("players");
        locationUnlockHandler = new LocationUnlockHandler(this);
        availableRewards = new HashMap<>();
        //if detected
        var citizens = Bukkit.getPluginManager().getPlugin("Citizens");
        if(citizens != null) {
            citizensUnlockHandler = new CitizensUnlockHandler(this);
        }
        registerQuestBooks();
        checkDataDirectory();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public Core getPlugin() {
        return plugin;
    }

    public Set<QuestBook> getBooks() {
        return books;
    }

    public void checkDataDirectory() {
        try {
            var file = playerDataLocation.toFile();
            if (!file.exists()) {
                Files.createDirectory(playerDataLocation);
            }
        } catch(Exception e) {
            plugin.getLogger().info(e.toString());
        }
    }

    public boolean loadQuestData(UUID uuid) {
        try {
            var file = playerDataLocation.resolve(uuid.toString());
            if(!file.toFile().exists())
                Files.writeString(file, "[]");
            var type = new TypeToken<HashSet<UUID>>() {
            }.getType();
                Set<UUID> list = GsonComponentSerializer.gson().serializer().fromJson(Files.readString(file), type);
                questData.put(uuid, new QuestPlayer(uuid, list));
                return true;
        } catch (Exception e) {
            plugin.getLogger().info(e.toString());
            return false;
        }
    }

    public void registerQuestBooks() {
        try {
            var file = bookLocation.toFile();
            if (!file.exists()) {
                Files.createDirectory(bookLocation);
            }
            for(var entry : file.listFiles()) {
                addBook(QuestBookSerializer.deserializeQuestBook(new JsonParser().parse(Files.readString(entry.toPath()))));
            }
        } catch(Exception e) {
            plugin.getLogger().info(e.toString());
        }
    }


    public void addBook(QuestBook book) {
        var conditions = book.getUnlockConditions().values();
        for(var condition : conditions) {
            if(condition instanceof LocationUnlock location)
                locationUnlockHandler.registerEvent(location);
            else if(condition instanceof CitizensUnlock citizensUnlock && citizensUnlockHandler != null)
                citizensUnlockHandler.registerEvent(citizensUnlock);
            else
                plugin.getLogger().info("Condition not recognized");
        }
        books.add(book);
    }
    public QuestPlayer getPlayer(Player player) {
        var data = questData.get(player.getUniqueId());
        if(data == null){
            plugin.getLogger().info("Somehow there is missing player");
        }
        return data;
    }
    public IQuestData getQuestData(UUID uuid) {
        var player = questData.get(uuid);
        if(player == null) {
            plugin.getLogger().info("Somehow there is missing player");
        }
        return player;
    }

    public IUnlockCondition getUnlockCondition(UUID uuid) {
        for(var b : books) {
            var condition = b.getUnlockConditions().get(uuid);
            if(condition != null)
                return condition;
        }
        return null;
    }

    public void registerReward(Player player, QuestReward reward) {
        availableRewards.put(player.getUniqueId(), reward);
    }
    public boolean getRewardUnlocked(Player player, QuestReward reward) {
        if(availableRewards.containsKey(player.getUniqueId())) {
            availableRewards.remove(player.getUniqueId());

            return true;
        }
        return false;
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        questData.remove(event.getPlayer().getUniqueId());
    }
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        loadQuestData(event.getPlayer().getUniqueId());
    }

}
