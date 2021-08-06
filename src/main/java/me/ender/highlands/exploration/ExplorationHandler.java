package me.ender.highlands.exploration;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import me.ender.highlands.Core;
import me.ender.highlands.exploration.book.QuestBook;
import me.ender.highlands.exploration.book.QuestBookSerializer;
import me.ender.highlands.exploration.book.IQuestReward;
import me.ender.highlands.exploration.commands.EQuest;
import me.ender.highlands.exploration.conditions.*;
import me.ender.highlands.exploration.data.IQuestData;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ExplorationHandler implements Listener {
    public static final NamespacedKey questBookKey = NamespacedKey.fromString("highlandsexploration:quest-book");
    public static final Gson GSON = new Gson();
    private final Core plugin;
    private Map<UUID, QuestPlayer> questData;
    private Map<String, QuestBook> books;
    private final Path bookLocation;
    private final Path playerDataLocation;
    private final LocationUnlockHandler locationUnlockHandler;
    private CitizensUnlockHandler citizensUnlockHandler;

    //commands
    private EQuest equest;


    public ExplorationHandler(Core plugin) {
        this.plugin = plugin;
        questData = new HashMap<>();
        books = new HashMap<>();
        bookLocation = plugin.getDataFolder().toPath().resolve("books");
        playerDataLocation = plugin.getDataFolder().toPath().resolve("players");
        equest = new EQuest(this);
        locationUnlockHandler = new LocationUnlockHandler(this);
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

    public Collection<QuestBook> getBooks() {
        return books.values();
    }
    public QuestBook getBook(String title) {
        return books.get(title);
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

    public boolean loadQuestData(Player player) {
        try {
            var uuid = player.getUniqueId();
            var file = playerDataLocation.resolve(uuid.toString());
            if(!file.toFile().exists()) {
                var p = new QuestPlayer(player);
                questData.put(uuid, p);
                return true;
            }
            else {
                var list = GSON.fromJson(Files.readString(file), QuestPlayer.class);
                questData.put(uuid, list);
                return true;
            }
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
        books.put(book.title, book);
    }
    public QuestPlayer getPlayer(Player player) {
        var data = questData.get(player.getUniqueId());
        if(data == null){
            loadQuestData(player);
            data = questData.get(player.getUniqueId());
        }
        return data;
    }




    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        questData.remove(event.getPlayer().getUniqueId());
    }
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        loadQuestData(event.getPlayer());
    }
    @EventHandler
    public void onBookOpenEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        var item = p.getInventory().getItemInMainHand();
        if(item.getType() != Material.WRITTEN_BOOK)
            return;
        var bookKey = item.getItemMeta().getPersistentDataContainer().get(questBookKey, PersistentDataType.STRING);
        if(bookKey == null)
            return;
        e.setCancelled(true);
        var book = getBook(bookKey).getBook(getPlayer(p));
        if(book != null)
            p.openBook(book);

    }

}
