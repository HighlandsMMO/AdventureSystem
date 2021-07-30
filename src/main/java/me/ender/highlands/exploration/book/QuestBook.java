package me.ender.highlands.exploration.book;

import com.google.gson.annotations.JsonAdapter;
import me.ender.highlands.exploration.QuestPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

@JsonAdapter(QuestBookSerializer.class)
public class QuestBook {
    public String title;
    public String author;
    public List<QuestPage> pages;


    public QuestBook(String title, String author) {
        this.title = title;
        this.author = author;
        pages = new ArrayList<>();

    }
    ///page is added to book does the book item need to exist
    public void addPage(QuestPage page) {
        pages.add(page);
//        var meta = (BookMeta)book.getItemMeta();
//        meta.spigot().addPage(page.getRaw());
//        book.setItemMeta(meta);

    }
    public void addPage(QuestComponent[]... components) {
        for(var c : components) {
            var page = new QuestPage(c);
            addPage(page);
        }
    }

    /***
     * Gets a the book with no condition checks
     * @return
     */
    public ItemStack getRawBook(){
        var book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setTitle(title);
        bookMeta.setAuthor(author);
        var spigot = bookMeta.spigot();
        for(var p : pages) {
            spigot.addPage(p.getRaw());
        }

        book.setItemMeta(bookMeta);

        return book;
    }

    /***
     * Checks to make sure the player can view
     * @param player
     * @return
     */
    public ItemStack getBook(QuestPlayer player) {
        //what does it do when it unlocks
        var book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setTitle(title);
        bookMeta.setAuthor(author);
        var spigot = bookMeta.spigot();
        for(var p : pages) {
            if(player.getQuestData().getUnlocked(p.getCondition()))
                spigot.addPage(p.getPage(player));
        }
        book.setItemMeta(bookMeta);

        return book;
    }
    private QuestBook() {
    }

}
