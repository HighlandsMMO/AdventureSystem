package me.ender.highlands.exploration.book;

import com.google.gson.annotations.JsonAdapter;
import me.ender.highlands.exploration.ExplorationHandler;
import me.ender.highlands.exploration.QuestPlayer;
import me.ender.highlands.exploration.conditions.IUnlockCondition;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@JsonAdapter(QuestBookSerializer.class)
public class QuestBook {
    public String title;
    public String author;
    public List<QuestPage> pages;
    private Map<UUID, IUnlockCondition> unlockConditions;
    private IUnlockCondition condition;


    public QuestBook(String title, String author) {
        this.title = title;
        this.author = author;
        pages = new ArrayList<>();
        unlockConditions = new HashMap<>();

    }
    public  IUnlockCondition getViewCondition() {
        return condition;
    }
    public void setViewCondition(IUnlockCondition condition) {
        this.condition = condition;
    }
    public Map<UUID, IUnlockCondition> getUnlockConditions() {
        return unlockConditions;
    }
    //IUnlockCondition getSimiliarUnlockCondition(String impl,)
    ///page is added to book does the book item need to exist
    public void addPage(QuestPage page) {
        if(pages.contains(page)) {
            return;
        }
        var pageCondition = page.getCondition();
        if(pageCondition != null)
            unlockConditions.putIfAbsent(pageCondition.getUUID(), pageCondition);

        var lines = page.getRawPage();
        for(int i =0; i< lines.size(); i++) {
            var line = lines.get(i);
            var condition = line.getCondition();
            var reward = line.getReward();
            if(condition != null)
                unlockConditions.putIfAbsent(condition.getUUID(), condition);
            if(reward != null) {
                handleReward(line);
            }
        }
        pages.add(page);

    }
    public IQuestReward getReward(UUID uuid) {
        var condition = unlockConditions.get(uuid);
        if(condition != null)
            if(condition.getQuestReward() != null)
                return condition.getQuestReward();
        return null;
    }
    private void handleReward(QuestComponent qc) {
        var reward = qc.getReward();
        var comp = qc.getComponent();
        if(comp.getClickEvent() != null) {
            //need to overwrite it each time regardless
        }
        //string builder
        var sb = new StringBuilder();
        comp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/equest reward " + title +" " +qc.getCondition().getUUID()));
    }
    @Nullable
    public IQuestReward getReward(int page, int line) {
        var ln  = pages.get(page).getLine(line);
        //because getReward already checks if null
        var reward = ln.getReward();
        return reward;
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
    @Nullable
    public ItemStack getEmptyBook() {
        var book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setTitle(title);
        bookMeta.setAuthor(author);
        bookMeta.getPersistentDataContainer().set(ExplorationHandler.questBookKey, PersistentDataType.STRING, title);

        book.setItemMeta(bookMeta);

        return book;
    }

    /***
     * Checks to make sure the player can view
     * @param player
     * @return
     */
    @Nullable
    public ItemStack getBook(QuestPlayer player) {
        if(!player.getUnlocked(condition)) {
            //player does not have book unlocked
            player.getPlayer().sendMessage("Book not unlocked");
            return null;
        }
        if(player == null)
            return null;
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
    public QuestPage getPage(int index) throws IndexOutOfBoundsException {
        return pages.get(index);
    }

    public ItemStack getAdminBook() {
        //change the conditions in minecraft
        return null;
    }
    private QuestBook() {
    }

}
