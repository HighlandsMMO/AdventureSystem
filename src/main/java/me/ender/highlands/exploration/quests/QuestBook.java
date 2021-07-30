package me.ender.highlands.exploration.quests;

import com.google.gson.annotations.JsonAdapter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonAdapter(QuestBookSerializer.class)
public class QuestBook {
    public String title;
    public String author;
    public List<QuestPage> pages;

    private ItemStack book;

    public QuestBook(String title, String author) {
        this.title = title;
        this.author = author;
        book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setTitle(title);
        bookMeta.setAuthor(author);
        book.setItemMeta(bookMeta);
        pages = new ArrayList<>();

    }

    public void addPage(QuestPage page) {
        pages.add(page);
        var meta = (BookMeta)book.getItemMeta();
        meta.spigot().addPage(page.getRaw());
        book.setItemMeta(meta);

    }
    public void addPage(QuestComponent[]... components) {
        for(var c : components) {
            var page = new QuestPage(c);
            addPage(page);
        }
    }

    public ItemStack getBook(){
        return book;
    }
    private QuestBook() {
    }
}
