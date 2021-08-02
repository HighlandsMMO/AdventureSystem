package me.ender.highlands.exploration.book;

import com.google.gson.annotations.JsonAdapter;
import me.ender.highlands.exploration.QuestPlayer;
import me.ender.highlands.exploration.conditions.IUnlockCondition;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.*;

@JsonAdapter(QuestBookSerializer.class)
public class QuestBook {
    public String title;
    public String author;
    public List<QuestPage> pages;
    private Map<UUID, IUnlockCondition> unlockConditions;


    public QuestBook(String title, String author) {
        this.title = title;
        this.author = author;
        pages = new ArrayList<>();
        unlockConditions = new HashMap<>();

    }
    public Map<UUID, IUnlockCondition> getUnlockConditions() {
        return unlockConditions;
    }
    ///page is added to book does the book item need to exist
    public void addPage(QuestPage page) {
        if(pages.contains(page)) {
            return;
        }
        var pageCondition = page.getCondition();
        if(pageCondition != null)
            unlockConditions.putIfAbsent(pageCondition.getUUID(), pageCondition);

        for(var lines : page.getRawPage()) {
            var condition = lines.getCondition();
            if(condition != null)
                unlockConditions.putIfAbsent(condition.getUUID(), condition);
        }
        pages.add(page);
//        var meta = (BookMeta)book.getItemMeta();
//        meta.spigot().addPage(page.getRaw());
//        book.setItemMeta(meta);

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
            if(player.getUnlocked(p.getCondition()))
                spigot.addPage(p.getPage(player));
        }
        book.setItemMeta(bookMeta);

        return book;
    }
    public QuestPage getPage(int index) {
        return pages.get(index);
    }

    public ItemStack getAdminBook() {
        //change the conditions in minecraft
        return null;
    }
    private QuestBook() {
    }

}
