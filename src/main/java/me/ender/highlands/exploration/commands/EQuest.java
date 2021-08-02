package me.ender.highlands.exploration.commands;

import me.ender.highlands.ECommand;
import me.ender.highlands.exploration.ExplorationHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EQuest extends ECommand {
    private final ExplorationHandler handler;

    public EQuest(ExplorationHandler handler) {
        super(handler.getPlugin(), "equest");
        this.handler = plugin.getExplorationHandler();
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
                switch(strings[3]) {
                    case "rewards" -> {
                        var rewards = page.getRewards();
                        //todo: expand options
                        var index = Integer.parseInt(strings[4]);
                        if(index > rewards.size() -1) {
                            p.sendMessage("Reward number too big");
                            return false;
                        }
                        var reward = rewards.get(index);
                        if(handler.getRewardUnlocked(p, reward) || p.hasPermission("ender.exploration.admin"))
                            reward.registerReward(handler.getPlayer(p));
                    }
                }

            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }

}
