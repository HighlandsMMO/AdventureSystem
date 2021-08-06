package me.ender.highlands.exploration.commands;

import me.ender.highlands.ECommand;
import me.ender.highlands.exploration.ExplorationHandler;
import me.ender.highlands.exploration.book.QuestBook;
import me.ender.highlands.exploration.book.QuestComponent;
import me.ender.highlands.exploration.book.QuestPage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class EQuest extends ECommand {
    private final ExplorationHandler handler;

    public EQuest(ExplorationHandler handler) {
        super(handler.getPlugin(), "equest");
        this.handler = handler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        //most of these commands are not ment to be ran by players
        var p = (Player)commandSender;
        switch(strings[0]) {
            case "admin" -> {
                var obook = handler.getBooks().stream().filter(b-> b.title.equals(strings[1])).findFirst();
                if(obook.isEmpty())
                    return false;
                var book = obook.get();
                if(strings.length < 3) {
                    p.openBook(book.getAdminBook());
                    return true;
                }
                //else diving deeper
                var page = book.getPage(Integer.parseInt(strings[2]));
                if(page == null){
                    p.sendMessage("Page " + strings[2] + " not found in " + strings[1]);
                    return false;
                }
                if(strings.length < 4)
                    return false; //not enough args
                //region page management

            }
            case "reward" -> {
                //gui not supported?
                //args length name + page + line
                //equest player <name> <page> <line>
                //equest reward <name> <guid>
                if(strings.length == 3) {
                    var book = getBook(strings[1]);
                    try {
                        var guid = UUID.fromString(strings[2]);
                        var condition = book.getUnlockConditions().get(guid);
                        if(condition == null)
                            return false;
                        return condition.registerReward(handler.getPlayer(p));

                    } catch(Exception e) {

                    }
                }
                if(strings.length < 4)
                    return false; //no message because its not ment to be called by player
                var intPage = Integer.parseInt(strings[2]);
                var intLine = Integer.parseInt(strings[3]);
                var comp = getQuestComponent(strings[1], intPage, intLine);
                if(comp == null)
                    return false;
                var condition = comp.getCondition();
                if(condition == null)
                    return false;
                condition.registerReward(handler.getPlayer(p)); //this should take care of everything

            }
            case "get" -> {
                //get special empty book
                var book = getBook(strings[1]);
                if(book == null)
                    return false;
                p.getInventory().addItem(book.getEmptyBook());
            }
            case "open" -> {
                //need to check if access
                var book = getBook(strings[1]);
                var item = book.getBook(handler.getPlayer(p));
                if(item == null)
                    return false;
                else
                    p.openBook(item);
            }
        }
        return true;
    }
    //region utils


    @Nullable
    private QuestBook getBook(String name) {
        return handler.getBook(name);
    }
    private QuestPage getPage(String name, int page) {
        var book = getBook(name);
        if(book ==null)
            return null;
        return book.getPage(page);
    }
    private QuestComponent getQuestComponent(String name, int page, int line) {
        var p = getPage(name, page);
        if(p == null)
            return null;
        return p.getLine(line);
    }
    //endregion
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }

}
