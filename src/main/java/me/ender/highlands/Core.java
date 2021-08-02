package me.ender.highlands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.ender.highlands.exploration.conditions.CitizensUnlock;
import me.ender.highlands.exploration.ExplorationHandler;
import me.ender.highlands.exploration.conditions.IUnlockCondition;
import me.ender.highlands.exploration.book.QuestBook;
import me.ender.highlands.exploration.book.QuestComponent;
import me.ender.highlands.exploration.book.QuestPage;
import me.ender.highlands.exploration.book.QuestBookSerializer;
import me.ender.highlands.exploration.conditions.LocationUnlock;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Core extends JavaPlugin {

    private ExplorationHandler explorationHandler;
    public ExplorationHandler getExplorationHandler() {
        return explorationHandler;
    }

    @Override
    public void onEnable() {
        explorationHandler = new ExplorationHandler(this);
        if(!getDataFolder().exists()) {
            try {
                Files.createDirectory(getDataFolder().toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var p = (Player)sender;
        ItemStack book = null;//spigotBook();
        if(args.length > 0) {
            switch(args[0]) {
                case "location2":
                    var b = new ComponentBuilder("H").color(ChatColor.LIGHT_PURPLE).append("el").color(ChatColor.RED).append("lo").color(ChatColor.GOLD).bold(true).strikethrough(true).create();
                    var t = BaseComponent.toPlainText(b);
                    p.sendMessage(b.length + "");
                    String s = "";
                    for(var c : b) {
                        s += c.toPlainText();
                    }
                    p.sendMessage(s);
                    p.sendMessage(t);
                    System.out.println(s);
                    book = spigotBook2();

                    p.sendMessage(ComponentSerializer.toString(book));


                    break;
                case "location3": {
                    try {
                        var json = Files.readString(Path.of("plugins/HighlandsExploration/books", "test.json"));
                        var g = new Gson();
                        QuestBook qb = g.fromJson(json, QuestBook.class);
                        book = qb.getBook(explorationHandler.getPlayer(p));


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }break;
            }
        } else{
            if(book==null) book = spigotBook(p.getWorld());
            p.getInventory().addItem(book);
        }


        p.openBook(book);


        return true;
    }


    private ItemStack spigotBook(World w) {
        var block = w.getBlockAt(-267, 65, 11);
        QuestBook qb = new QuestBook("Explore", "Server");
        var comp1 = new QuestComponent(new ComponentBuilder("Chapter 1").bold(true).color(ChatColor.BLACK).create()[0]);
        var comp2 = new QuestComponent(new ComponentBuilder("\n\nLocation 1").color(ChatColor.RED).strikethrough(true).create()[0]);
        var comp3 = new QuestComponent(
                new ComponentBuilder("\nLocation 2").color(ChatColor.GREEN).strikethrough(false).event(
                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ecore location2")).create()[0],
                new CitizensUnlock(CitizensUnlock.Events.NPCRightClickEvent, "0")); //new LocationUnlock(LocationUnlock.Events.BlockClick, IUnlockCondition.Identifiers.STRING, block)
        var page = new QuestPage(new QuestComponent[]{comp1,comp2, comp3});
        qb.addPage(page);
        var g = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(QuestBook.class, new QuestBookSerializer()).create();
        var json = g.toJson(qb);
        try {
            Files.write(Path.of("plugins/HighlandsExploration/books", "test.json"), json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return qb.getRawBook();
    }
    private ItemStack spigotBook2() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();

//create a page

        BaseComponent[] page = new ComponentBuilder(
                "High Above The Ground \nWhere Massive Mountains Meet\nThere is a treasure to seek\n")
                .color(ChatColor.BLACK).append( new ComponentBuilder("Rewards\n").append("* kkkkkkkkkk\n* kkkkkkkkkk").obfuscated(true).create()).create();

//add the page to the meta
        bookMeta.spigot().addPage(page);
        bookMeta.spigot().addPage(page);

//set the title and author of this book
        bookMeta.setTitle("Interactive Book");
        bookMeta.setAuthor("gigosaurus");

//update the ItemStack with this new meta
        book.setItemMeta(bookMeta);

        return book;
    }
}
