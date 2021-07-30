package me.ender.highlands.exploration.commands;

import me.ender.highlands.Core;
import me.ender.highlands.ECommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QuestData extends ECommand {

    public QuestData(Core plugin) {
        super(plugin, "questdata");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length < 2 )
            return false;
        var p = getPlayerTarget(args[0]);
        if(p == null)
            sender.sendMessage("Specified player does not exist");
        switch(args[1]) {
            case "get":
            case "set":
            case "reset":
                break;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
